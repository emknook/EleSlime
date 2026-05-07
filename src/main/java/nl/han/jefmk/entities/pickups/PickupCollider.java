package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;

import java.util.List;

public class PickupCollider extends CircleEntity implements Collider, Collided {

    private final Pickup pickup;

    public PickupCollider(Pickup pickup, double radius, Coordinate2D location) {
        super(location);
        this.pickup = pickup;
        setRadius(radius);

        setFill(EleSlime.DEBUG ? Color.RED : Color.TRANSPARENT);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            if (collider instanceof Player player) {
                pickup.onPlayerCollision(player);
                pickup.remove();
                this.remove();
                return;
            }
        }
    }
}
