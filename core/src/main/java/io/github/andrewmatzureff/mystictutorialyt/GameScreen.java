package io.github.andrewmatzureff.mystictutorialyt;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.andrewmatzureff.mystictutorialyt.asset.AssetService;
import io.github.andrewmatzureff.mystictutorialyt.asset.MapAsset;
import io.github.andrewmatzureff.mystictutorialyt.system.RenderSystem;

import java.util.Arrays;

import static io.github.andrewmatzureff.mystictutorialyt.Main.UNIT_SCALE;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen extends ScreenAdapter {
    private final Main game;
    private final Batch batch;
    private final AssetService assetService;
    private final Viewport viewport;
    private final OrthographicCamera camera;
    private final Engine engine;

    public GameScreen(Main game) {
        this.game = game;
        assetService = game.getAssetService();
        viewport = game.getViewport();
        camera = game.getCamera();
        batch = game.getBatch();
        engine = new Engine();

        engine.addSystem(new RenderSystem(batch, viewport, assetService));
//        engine.addSystem(new MoveSystem(batch, viewport, assetService));
//        engine.addSystem(new AnimationSystem(batch, viewport, assetService));
    }

    @Override
    public void show() {
        assetService.load(MapAsset.MAIN);
        engine.getSystem(RenderSystem.class).setMap(assetService.get(MapAsset.MAIN));
    }

    @Override
    public void hide() {
        engine.removeAllEntities();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1f / 30);
        engine.update(delta);
    }

    @Override
    public void dispose() {
        Arrays
            .stream(engine.getSystems().toArray())
            .filter(es -> es instanceof Disposable)
            .map(Disposable.class::cast)
            .forEach(Disposable::dispose);
    }
}
