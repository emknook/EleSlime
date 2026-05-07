package nl.han.jefmk.entities;

import java.util.function.Consumer;

public interface HasHealth {
    int getHealth();
    void takeDamage();
    void regainHealth();
    void addHealthListener(Consumer<Integer> listener);
}
