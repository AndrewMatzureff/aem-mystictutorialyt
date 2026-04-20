package io.github.andrewmatzureff.mystictutorialyt.tiled;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import io.github.andrewmatzureff.mystictutorialyt.Main;
import io.github.andrewmatzureff.mystictutorialyt.asset.AssetService;
import io.github.andrewmatzureff.mystictutorialyt.asset.AtlasAsset;
import io.github.andrewmatzureff.mystictutorialyt.component.Controller;
import io.github.andrewmatzureff.mystictutorialyt.component.Graphic;
import io.github.andrewmatzureff.mystictutorialyt.component.Move;
import io.github.andrewmatzureff.mystictutorialyt.component.Transform;

public class TiledAshleyConfigurator {
    private final Engine engine;
    private final AssetService assetService;

    public TiledAshleyConfigurator(Engine engine, AssetService assetService) {
        this.engine = engine;
        this.assetService = assetService;
    }

    public void onLoadObject(TiledMapTileMapObject tileMapObject) {
        Entity entity = engine.createEntity();
        TiledMapTile tile = tileMapObject.getTile();
        TextureRegion textureRegion = getTextureRegion(tile);
        int z = tile.getProperties().get("z", 1, Integer.class);

        entity.add(new Graphic(Color.WHITE.cpy(), textureRegion));
        addEntityTransform(entity
            , tileMapObject.getX(), tileMapObject.getY(), z
            , textureRegion.getRegionWidth(), textureRegion.getRegionHeight()
            , tileMapObject.getScaleX(), tileMapObject.getScaleY());

        addEntityController(tileMapObject, entity);
        addEntityMove(tile, entity);

        engine.addEntity(entity);
    }

    private void addEntityMove(TiledMapTile tile, Entity entity) {
        float speed = tile.getProperties().get("speed", 0f, Float.class);
        if (speed == 0f) return;
        entity.add(new Move(speed));
    }

    private void addEntityController(TiledMapTileMapObject tileMapObject, Entity entity) {
        boolean controller = tileMapObject.getProperties().get("controller", false, Boolean.class);
        if (!controller) return;

        entity.add(new Controller());
    }

    private void addEntityTransform(Entity entity, float x, float y, int z, float w, float h, float scaleX, float scaleY) {
        Vector2 position = new Vector2(x, y);
        Vector2 size = new Vector2(w, h);
        Vector2 scale = new Vector2(scaleX, scaleY);

        position.scl(Main.UNIT_SCALE);
        size.scl(Main.UNIT_SCALE);

        entity.add(new Transform(position, z, size, scale, 0));
    }

    private TextureRegion getTextureRegion(TiledMapTile tile) {
        String atlasAssetStr = tile.getProperties().get("atlasAsset", AtlasAsset.OBJECTS.name(), String.class);
        AtlasAsset atlasAsset = AtlasAsset.valueOf(atlasAssetStr);
        TextureAtlas textureAtlas = assetService.get(atlasAsset);
        FileTextureData textureData = (FileTextureData) tile
            .getTextureRegion()
            .getTexture()
            .getTextureData();
        String atlasKey = textureData.getFileHandle().nameWithoutExtension();
        TextureAtlas.AtlasRegion region = textureAtlas.findRegion(atlasKey + "/" + atlasKey);

        if (region != null) {
            return region;
        }

        return tile.getTextureRegion();
    }
}
