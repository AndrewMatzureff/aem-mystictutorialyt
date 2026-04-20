package io.github.andrewmatzureff.mystictutorialyt.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.andrewmatzureff.mystictutorialyt.component.Controller;
import io.github.andrewmatzureff.mystictutorialyt.component.Move;

import java.util.Optional;

public class ControllerSystem extends IteratingSystem {
    public ControllerSystem() {
        super(Family.all(Controller.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Controller controller = Controller.MAPPER.get(entity);

        if (controller.getPressedCommands().isEmpty() && controller.getReleasedCommands().isEmpty()) {
            return;
        }

        controller.getPressedCommands().forEach(command -> {
            switch (command) {
                case UP -> moveEntity(entity, 0f, 1f);
                case DOWN -> moveEntity(entity, 0f, -1f);
                case LEFT -> moveEntity(entity, -1f, 0f);
                case RIGHT -> moveEntity(entity, 1f, 0f);
            }
        });

        controller.getPressedCommands().clear();

        controller.getReleasedCommands().forEach(command -> {
            switch (command) {
                case UP -> moveEntity(entity, 0f, -1f);
                case DOWN -> moveEntity(entity, 0f, 1f);
                case LEFT -> moveEntity(entity, 1f, 0f);
                case RIGHT -> moveEntity(entity, -1f, 0f);
            }
        });

        controller.getReleasedCommands().clear();
    }

    private void moveEntity(Entity entity, float x, float y) {
        Optional.of(entity)
            .map(Move.MAPPER::get)
            .ifPresent(move -> {
                move.getDirection().x += x;
                move.getDirection().y += y;
            });
    }
}
