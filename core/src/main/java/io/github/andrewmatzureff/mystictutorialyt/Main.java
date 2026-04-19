package io.github.andrewmatzureff.mystictutorialyt;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.andrewmatzureff.mystictutorialyt.asset.AssetService;
import io.github.andrewmatzureff.mystictutorialyt.screen.LoadingScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static final float WORLD_WIDTH = 16f;
    public static final float WORLD_HEIGHT = 9f;
    public static final float UNIT_SCALE = 1f / 16f;

    private Batch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private AssetService assetService;
    private GLProfiler glProfiler;
    private FPSLogger fpsLogger;

    public Batch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public AssetService getAssetService() {
        return assetService;
    }

    private final Map<Class<? extends Screen>, Screen> screensByClass = new HashMap<>();

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        assetService = new AssetService(new InternalFileHandleResolver());
        glProfiler = new GLProfiler(Gdx.graphics);
        glProfiler.enable();
        fpsLogger = new FPSLogger();

        addScreen(new LoadingScreen(this, assetService));
        setScreen(LoadingScreen.class);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        super.resize(width, height);
    }
    public void addScreen(Screen screen) {
        screensByClass.put(screen.getClass(), screen);
    }

    public void removeScreen(Screen screen) {
        screensByClass.remove(screen.getClass());
    }

    public void setScreen(Class<? extends Screen> screenClass) {
        Optional.of(screenClass)
            .map(screensByClass::get)
            .ifPresentOrElse(super::setScreen, () -> {
                throw new GdxRuntimeException("No screen with class '%s' has been registered.".formatted(screenClass));
            });
    }

    @Override
    public void render() {
        fpsLogger.log();
        glProfiler.reset();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        Gdx.graphics.setTitle("Mystic Tutorial - Draw Calls: " + glProfiler.getDrawCalls());
    }

    @Override
    public void dispose() {
        screensByClass.values().forEach(Screen::dispose);
        screensByClass.clear();
        batch.dispose();
        assetService.debugDiagnostics();
        assetService.dispose();
    }
}
