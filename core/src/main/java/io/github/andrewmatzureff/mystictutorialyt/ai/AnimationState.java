package io.github.andrewmatzureff.mystictutorialyt.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import io.github.andrewmatzureff.mystictutorialyt.component.Animation2D;
import io.github.andrewmatzureff.mystictutorialyt.component.FSM;
import io.github.andrewmatzureff.mystictutorialyt.component.Move;

public enum AnimationState implements State<Entity> {
    IDLE {
        @Override
        public void enter(Entity entity) {
            Animation2D.MAPPER.get(entity).setType(Animation2D.AnimationType.IDLE);
        }

        @Override
        public void update(Entity entity) {
            Move move = Move.MAPPER.get(entity);

            if (move != null && !move.isRooted() && !move.getDirection().isZero()) {
                FSM.MAPPER.get(entity).getAnimationFSM().changeState(WALK);
                return;
            }
        }

        @Override
        public void exit(Entity entity) {

        }

        @Override
        public boolean onMessage(Entity entity, Telegram telegram) {
            return false;
        }
    },

    WALK {
        @Override
        public void enter(Entity entity) {
            Animation2D.MAPPER.get(entity).setType(Animation2D.AnimationType.WALK);
        }

        @Override
        public void update(Entity entity) {
            Move move = Move.MAPPER.get(entity);

            if (move == null || move.isRooted() || move.getDirection().isZero()) {
                FSM.MAPPER.get(entity).getAnimationFSM().changeState(IDLE);
            }
        }

        @Override
        public void exit(Entity entity) {

        }

        @Override
        public boolean onMessage(Entity entity, Telegram telegram) {
            return false;
        }
    }
}
