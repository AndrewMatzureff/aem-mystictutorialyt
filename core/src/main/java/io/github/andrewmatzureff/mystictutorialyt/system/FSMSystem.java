package io.github.andrewmatzureff.mystictutorialyt.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.andrewmatzureff.mystictutorialyt.component.FSM;

public class FSMSystem extends IteratingSystem {
    public FSMSystem() {
        super(Family.all(FSM.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FSM.MAPPER.get(entity).getAnimationFSM().update();
    }
}
