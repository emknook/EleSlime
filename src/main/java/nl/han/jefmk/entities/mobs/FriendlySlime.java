package nl.han.jefmk.entities.mobs;

import com.github.hanyaeger.api.Coordinate2D;

public class FriendlySlime extends Slime {
    public FriendlySlime(Coordinate2D initialLocation) {
        super("sprites/friendly-spritesheet.png", initialLocation, 2, 2);
    }
}
