package nl.han.jefmk.entities.obstacles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import nl.han.jefmk.entities.player.Player;

public class Stalagmite extends Obstacle {
    public Stalagmite(Coordinate2D initialLocation) {
        super("sprites/stalagmite.png", initialLocation);
    }

    @Override
    public void onCollision(Collider collider) {
        if(collider instanceof Player player) {
            player.takeDamage();
            player.takeKnockback(this);
        }
    }
}
