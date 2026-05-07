package nl.han.jefmk.score;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Singleton that tracks the player's score for the current level.
 * Score is based on points collected minus a time penalty (-1 point/second).
 * Reset when a new level starts or the player dies.
 */
public class Score {

    private static final int TIME_PENALTY_PER_SECOND = 1;
    private static final int INITIAL_SCORE = 120;

    private static Score instance;

    private int baseScore;
    private long levelStartNanos;
    private final List<Consumer<Integer>> listeners = new ArrayList<>();

    private Score() {
        resetForNewLevel();
    }

    public static Score getInstance() {
        if (instance == null) {
            instance = new Score();
        }
        return instance;
    }

    /**
     * Call when a new level is loaded. Resets score and starts the timer.
     */
    public void resetForNewLevel() {
        baseScore = INITIAL_SCORE;
        levelStartNanos = System.nanoTime();
        notifyListeners();
    }

    /**
     * Call when the player dies. Score resets and timer restarts.
     */
    public void resetForDeath() {
        resetForNewLevel();
    }

    /**
     * Add points (e.g. pickup collected, enemy defeated, level completed).
     */
    public void addScore(int points) {
        baseScore += points;
        notifyListeners();
    }

    /**
     * Returns the current effective score: base points minus elapsed time penalty.
     * Clamped to zero so score never goes negative.
     */
    public int getScore() {
        long elapsedSeconds = getElapsedSeconds();
        int penalty = (int) (elapsedSeconds * TIME_PENALTY_PER_SECOND);
        return Math.max(0, baseScore - penalty);
    }

    /**
     * Returns the elapsed time in whole seconds since the level or death reset.
     */
    public long getElapsedSeconds() {
        return (System.nanoTime() - levelStartNanos) / 1_000_000_000L;
    }

    /**
     * Register a listener that is notified whenever the score changes.
     */
    public void addListener(Consumer<Integer> listener) {
        listeners.add(listener);
    }

    /**
     * Remove a previously registered listener.
     */
    public void removeListener(Consumer<Integer> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        int current = getScore();
        for (Consumer<Integer> listener : listeners) {
            listener.accept(current);
        }
    }
}
