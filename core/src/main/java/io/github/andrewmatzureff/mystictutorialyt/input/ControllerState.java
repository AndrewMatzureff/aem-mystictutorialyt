package io.github.andrewmatzureff.mystictutorialyt.input;

public interface ControllerState {
    void keyDown(Command command);

    default void keyUp(Command command) {}
}
