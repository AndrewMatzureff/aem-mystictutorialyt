package io.github.andrewmatzureff.mystictutorialyt.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import io.github.andrewmatzureff.mystictutorialyt.ai.AnimationState;

public class FSM implements Component {
    public static final ComponentMapper<FSM> MAPPER = ComponentMapper.getFor(FSM.class);

    private final DefaultStateMachine<Entity, AnimationState> animationFSM;

    public FSM(Entity owner) {
        this.animationFSM = new DefaultStateMachine<>(owner, AnimationState.IDLE);
    }

    public DefaultStateMachine<Entity, AnimationState> getAnimationFSM() {
        return animationFSM;
    }
}
