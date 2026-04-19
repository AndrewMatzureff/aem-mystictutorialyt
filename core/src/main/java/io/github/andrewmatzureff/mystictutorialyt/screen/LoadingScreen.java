package io.github.andrewmatzureff.mystictutorialyt.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import io.github.andrewmatzureff.mystictutorialyt.GameScreen;
import io.github.andrewmatzureff.mystictutorialyt.Main;
import io.github.andrewmatzureff.mystictutorialyt.asset.AssetService;
import io.github.andrewmatzureff.mystictutorialyt.asset.AtlasAsset;

import java.util.Arrays;

public class LoadingScreen extends ScreenAdapter {
    private final Main game;
    private final AssetService assetService;

    public LoadingScreen(Main game, AssetService assetService) {
        this.game = game;
        this.assetService = assetService;
    }

    @Override
    public void show() {
        Arrays.stream(AtlasAsset.values())
            .forEach(assetService::queue);
    }

    @Override
    public void render(float delta) {
        if (assetService.update()) {
            Gdx.app.debug("LoadingScreen", "Finished asset loading");
            createScreens();
            game.removeScreen(this);
            dispose();
            game.setScreen(GameScreen.class);
        }
    }

    private void createScreens() {
        game.addScreen(new GameScreen(game));
    }
}
