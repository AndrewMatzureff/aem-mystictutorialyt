package io.github.andrewmatzureff.mystictutorialyt.tiled;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import io.github.andrewmatzureff.mystictutorialyt.asset.AssetService;
import io.github.andrewmatzureff.mystictutorialyt.asset.MapAsset;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class TiledService {
    private final AssetService assetService;
    private TiledMap currentMap;

    private Consumer<TiledMap> mapChangeConsumer;

    private Consumer<TiledMapTileMapObject> loadObjectConsumer;

    public TiledService(AssetService assetService) {
        this.assetService = assetService;
        mapChangeConsumer = null;
        loadObjectConsumer = null;
        currentMap = null;
    }

    public void setMapChangeConsumer(Consumer<TiledMap> mapChangeConsumer) {
        this.mapChangeConsumer = mapChangeConsumer;
    }

    public void setLoadObjectConsumer(Consumer<TiledMapTileMapObject> loadObjectConsumer) {
        this.loadObjectConsumer = loadObjectConsumer;
    }

    public TiledMap loadMap(MapAsset mapAsset) {
        TiledMap map = assetService.load(mapAsset);
        map.getProperties().put("mapAsset", mapAsset);
        return map;
    }

    public void setMap(TiledMap map) {
        if (currentMap != null) {
            assetService.unload(currentMap.getProperties().get("mapAsset", MapAsset.class));
        }

        currentMap = map;
        loadMapObjects(map);

        if (mapChangeConsumer != null) mapChangeConsumer.accept(map);
    }

    private void loadMapObjects(TiledMap map) {
        StreamSupport.stream(map.getLayers().spliterator(), false)
            .filter(layer -> "objects".equals(layer.getName()))
            .forEach(this::loadObjectLayer);
//            .findFirst()
//            .ifPresent(this::loadObjectLayer);
    }

    private void loadObjectLayer(MapLayer objectLayer) {
        if (loadObjectConsumer == null) return;

        StreamSupport.stream(objectLayer.getObjects().spliterator(), false)
            // NOTE: does not throw on non-tile map objects!
            .filter(TiledMapTileMapObject.class::isInstance)
            .map(TiledMapTileMapObject.class::cast)
            .forEach(loadObjectConsumer);
    }
}
