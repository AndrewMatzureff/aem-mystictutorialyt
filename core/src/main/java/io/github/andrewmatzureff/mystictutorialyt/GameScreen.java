package io.github.andrewmatzureff.mystictutorialyt;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.andrewmatzureff.mystictutorialyt.asset.AssetService;
import io.github.andrewmatzureff.mystictutorialyt.asset.MapAsset;

import static io.github.andrewmatzureff.mystictutorialyt.Main.UNIT_SCALE;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final Batch batch;
    private final AssetService assetService;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final OrthogonalTiledMapRenderer mapRenderer;

    public GameScreen(Main game) {
        this.game = game;
        assetService = game.getAssetService();
        viewport = game.getViewport();
        camera = game.getCamera();
        batch = game.getBatch();
        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, batch);
    }

    @Override
    public void show() {
        assetService.load(MapAsset.MAIN);
        mapRenderer.setMap(assetService.get(MapAsset.MAIN));
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        batch.setColor(Color.WHITE);
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
    }
}
