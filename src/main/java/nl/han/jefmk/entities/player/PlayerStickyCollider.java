package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.surfaces.SurfaceCollider;

import java.util.List;

public class PlayerStickyCollider extends CircleEntity implements Collided {
    private final Player player;

    protected PlayerStickyCollider(Player player, double radius, Coordinate2D initialLocation) {
        super(initialLocation);
        this.player = player;
        setRadius(radius);

        setFill(EleSlime.DEBUG ? Color.GREEN : Color.TRANSPARENT);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            if (collider instanceof SurfaceCollider) {
                handleSurfaceCollision((SurfaceCollider) collider);
            }
        }
    }

    private void handleSurfaceCollision(SurfaceCollider surface) {
        player.addTouchingSurfaceDirection(surface.getSurfaceDirection());
    }

}