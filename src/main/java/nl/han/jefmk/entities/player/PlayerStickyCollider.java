package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.surfaces.SurfaceCollider;
import nl.han.jefmk.surfaces.SurfaceOwner;

import java.util.HashSet;
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
        // First pass: register vertical surface contacts and track which tiles provided them.
        // This prevents a corner tile's horizontal surface from overriding the vertical attachment.
        HashSet<SurfaceOwner> verticallyTouched = new HashSet<>();
        for (Collider collider : collidingObjects) {
            if (collider instanceof SurfaceCollider surface) {
                var dir = surface.getSurfaceDirection();
                if (dir == Direction.UP || dir == Direction.DOWN) {
                    player.addTouchingSurfaceDirection(dir);
                    verticallyTouched.add(surface.getOwner());
                    if (dir == Direction.DOWN) {
                        player.setStandingOwner(surface.getOwner());
                    }
                }
            }
        }
        // Second pass: only register horizontal contacts for owners not already handled vertically.
        for (Collider collider : collidingObjects) {
            if (collider instanceof SurfaceCollider surface) {
                var dir = surface.getSurfaceDirection();
                if ((dir == Direction.LEFT || dir == Direction.RIGHT)
                        && !verticallyTouched.contains(surface.getOwner())) {
                    player.addTouchingSurfaceDirection(dir);
                }
            }
        }
    }

}