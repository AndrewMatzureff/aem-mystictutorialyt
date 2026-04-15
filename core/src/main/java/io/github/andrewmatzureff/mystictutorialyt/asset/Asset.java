package io.github.andrewmatzureff.mystictutorialyt.asset;

import com.badlogic.gdx.assets.AssetDescriptor;

public interface Asset<T> {
    AssetDescriptor<T> getDescriptor();
}
