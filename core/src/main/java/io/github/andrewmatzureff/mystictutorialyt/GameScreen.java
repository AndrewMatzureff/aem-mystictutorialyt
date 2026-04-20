package io.github.andrewmatzureff.mystictutorialyt;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.andrewmatzureff.mystictutorialyt.asset.AssetService;
import io.github.andrewmatzureff.mystictutorialyt.asset.MapAsset;
import io.github.andrewmatzureff.mystictutorialyt.input.GameControllerState;
import io.github.andrewmatzureff.mystictutorialyt.input.KeyboardController;
import io.github.andrewmatzureff.mystictutorialyt.system.*;
import io.github.andrewmatzureff.mystictutorialyt.tiled.TiledAshleyConfigurator;
import io.github.andrewmatzureff.mystictutorialyt.tiled.TiledService;

import java.util.Arrays;

import static io.github.andrewmatzureff.mystictutorialyt.Main.UNIT_SCALE;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen extends ScreenAdapter {
    private final Engine engine;
    private final TiledService tiledService;
    private final TiledAshleyConfigurator tiledAshleyConfigurator;
    private final KeyboardController keyboardController;
    private final Main game;

    public GameScreen(Main game) {
        this.game = game;
        tiledService = new TiledService(game.getAssetService());
        engine = new Engine();
        tiledAshleyConfigurator = new TiledAshleyConfigurator(engine, game.getAssetService());
        keyboardController = new KeyboardController(GameControllerState.class, engine);

        engine.addSystem(new ControllerSystem());
        engine.addSystem(new MoveSystem());
        engine.addSystem(new FSMSystem());
        engine.addSystem(new FacingSystem());
        engine.addSystem(new AnimationSystem(game.getAssetService()));
        engine.addSystem(new RenderSystem(game.getBatch(), game.getViewport(), game.getCamera()));
//        engine.addSystem(new MoveSystem(batch, viewport, assetService));
//        engine.addSystem(new AnimationSystem(batch, viewport, assetService));
    }

    @Override
    public void show() {
        game.setInputProcessors(keyboardController);
        keyboardController.setActiveState(GameControllerState.class);

        tiledService.setMapChangeConsumer(engine.getSystem(RenderSystem.class)::setMap);
        tiledService.setLoadObjectConsumer(tiledAshleyConfigurator::onLoadObject);

        TiledMap tiledMap = tiledService.loadMap(MapAsset.MAIN);
        tiledService.setMap(tiledMap);
    }

    @Override
    public void hide() {
        engine.removeAllEntities();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1f / 30);
        engine.update(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            System.out.println("W");
        }
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
