package io.github.andrewmatzureff.mystictutorialyt.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.andrewmatzureff.mystictutorialyt.component.Graphic;
import io.github.andrewmatzureff.mystictutorialyt.component.Transform;

import java.util.Comparator;

import static io.github.andrewmatzureff.mystictutorialyt.Main.UNIT_SCALE;

public class RenderSystem extends SortedIteratingSystem implements Disposable {
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Batch batch;
    private final Viewport viewport;
    private final OrthographicCamera camera;

    public RenderSystem(Batch batch, Viewport viewport, OrthographicCamera camera) {
        super(Family.all(Transform.class, Graphic.class).get()
            , Comparator.comparing(Transform.MAPPER::get)
        );
        this.viewport = viewport;
        this.batch = batch;
        this.camera = camera;
        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, batch);
    }

    public void setMap(TiledMap tiledMap) {
        mapRenderer.setMap(tiledMap);
    }

    @Override
    public void update(float delta) {
        viewport.apply();
        batch.setColor(Color.WHITE);
        mapRenderer.setView(camera);
        mapRenderer.render();

        forceSort();
        batch.begin();
        super.update(delta);
        batch.end();
    }

    @Override
    public void dispose() {
        mapRenderer.dispose();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Transform transform = Transform.MAPPER.get(entity);
        Graphic graphic = Graphic.MAPPER.get(entity);

        if (graphic.getTextureRegion() == null) {
            return;
        }

        Vector2 position = transform.getPosition();
        Vector2 scale = transform.getScale();
        Vector2 size = transform.getSize();

        batch.setColor(graphic.getColor());
        batch.draw(graphic.getTextureRegion()
            , position.x - size.x * (1f - scale.x) * 0.5f
            , position.y - size.y * (1f - scale.y) * 0.5f
            , size.x * 0.5f
            , size.y * 0.5f
            , size.x
            , size.y
            , scale.x
            , scale.y
            , transform.getRotationDeg()
        );
    }
}
