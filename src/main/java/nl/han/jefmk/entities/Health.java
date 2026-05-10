package nl.han.jefmk.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Health {

    private static final long DEFAULT_DAMAGE_COOLDOWN_NS = 1_000_000_000L; // 1 s
    private static final long DEFAULT_REGAIN_COOLDOWN_NS = 500_000_000L;   // 0.5 s

    private int value;
    private final int initialHealth;

    private final List<Consumer<Integer>> listeners = new ArrayList<>();

    private final long damageCooldownNs;
    private final long regainCooldownNs;
    private long lastDamageTime;
    private long lastRegainTime;

    public Health(int initialHealth) {
        this(initialHealth, DEFAULT_DAMAGE_COOLDOWN_NS, DEFAULT_REGAIN_COOLDOWN_NS);
    }

    public Health(int initialHealth, long damageCooldownNs, long regainCooldownNs) {
        this.value = initialHealth;
        this.initialHealth = initialHealth;
        this.damageCooldownNs = damageCooldownNs;
        this.regainCooldownNs = regainCooldownNs;
        long now = System.nanoTime();
        this.lastDamageTime = now - damageCooldownNs;
        this.lastRegainTime = now - regainCooldownNs;
    }

    public int get() {
        return value;
    }

    public boolean canTakeDamage() {
        return System.nanoTime() - lastDamageTime >= damageCooldownNs;
    }

    /**
     *
     */
    public void resetForDeath() {
        this.value = initialHealth;
    }

    /** Applies damage only if the damage cooldown has elapsed. Returns {@code true} if damage was applied. */
    public boolean damage() {
        if (!canTakeDamage()) return false;
        lastDamageTime = System.nanoTime();

        value--;
        notifyListeners();
        return true;
    }

    /** Applies health gain only if the regain cooldown has elapsed. Returns {@code true} if health was applied. */
    public void regain() {
        if (System.nanoTime() - lastRegainTime < regainCooldownNs) return;
        lastRegainTime = System.nanoTime();
        value++;
        notifyListeners();
    }

    public void addListener(Consumer<Integer> listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        listeners.forEach(l -> l.accept(value));
    }
}
