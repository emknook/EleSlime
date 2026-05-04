package nl.han.jefmk.entities.mobs;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Animation;
import com.github.hanyaeger.api.entities.LoopingAnimation;

public class FriendlySlime extends Slime {

    private final Animation idleRightAnimation = new LoopingAnimation(0, 0, 0, 1);

    public FriendlySlime(Coordinate2D initialLocation) {
        super("sprites/friendly-spritesheet.png", initialLocation, 1, 2);
        setAutoCycle(300);
        idleRight();
    }

    public void idleRight() {
        playAnimation(idleRightAnimation);
    }
}
