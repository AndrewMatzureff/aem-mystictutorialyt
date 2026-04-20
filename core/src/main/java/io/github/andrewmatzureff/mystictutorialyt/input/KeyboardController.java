package io.github.andrewmatzureff.mystictutorialyt.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class KeyboardController extends InputAdapter {
    private static final Map<Integer, Command> KEY_MAPPING = Map.of(
        Input.Keys.W, Command.UP,
        Input.Keys.S, Command.DOWN,
        Input.Keys.A, Command.LEFT,
        Input.Keys.D, Command.RIGHT,
        Input.Keys.SPACE, Command.SELECT,
        Input.Keys.ESCAPE, Command.CANCEL
    );

    private final boolean[] commandState;
    private final Map<Class<? extends ControllerState>, ControllerState> controllerStatesByClass;
    private ControllerState activeState;

    public KeyboardController(Class<? extends ControllerState> initialState, Engine engine) {
        this.controllerStatesByClass = new HashMap<>();
        activeState = null;
        commandState = new boolean[Command.values().length];

        controllerStatesByClass.put(IdleControllerState.class, new IdleControllerState());
        controllerStatesByClass.put(GameControllerState.class, new GameControllerState(engine));
        setActiveState(initialState);
    }

    public void setActiveState(Class<? extends ControllerState> stateClass) {
        ControllerState controllerState = controllerStatesByClass.get(stateClass);

        if (controllerState == null) {
            throw new GdxRuntimeException("No state with class " + stateClass + " found in the registered states");
        }

        Arrays.stream(Command.values())
            .forEach(command -> {
                if (activeState != null && commandState[command.ordinal()]) {
                    activeState.keyUp(command);
                }

                commandState[command.ordinal()] = false;
            });

        activeState = controllerState;
    }

    @Override
    public boolean keyDown(int keycode) {
        return Optional
            .of(keycode)
            .map(KEY_MAPPING::get)
            .stream()
            .peek(command -> commandState[command.ordinal()] = true)
            .peek(activeState::keyDown)
            .findAny()
            .isPresent();
    }

    @Override
    public boolean keyUp(int keycode) {
        return Optional
            .of(keycode)
            .map(KEY_MAPPING::get)
            .stream()
            .filter(command -> commandState[command.ordinal()])
            .peek(command -> commandState[command.ordinal()] = false)
            .peek(activeState::keyUp)
            .findAny()
            .isPresent();
    }
}
