package io.github.andrewmatzureff.mystictutorialyt.utils;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
    static void main(String[] args) {
        String inputDir = "../raw_assets/objects";
        String outputDir = "../assets/packed";
        String packFileName = "objects";

        TexturePacker.process(inputDir, outputDir, packFileName);
    }
}
