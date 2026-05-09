package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.mobs.Slime;
import nl.han.jefmk.surfaces.SurfaceCollider;
import nl.han.jefmk.surfaces.SurfaceOwner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerCollider extends CircleEntity implements Collided {
    private final Player player;

    protected PlayerCollider(Player player, double radius, Coordinate2D initialLocation) {
        super(initialLocation);
        this.player = player;
        setRadius(radius);

        setFill(EleSlime.DEBUG ? Color.BLUE : Color.TRANSPARENT);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        // Resolve vertical surfaces first; track which tiles were vertically resolved so
        // horizontal surfaces on the same tile (corner tiles) don't incorrectly snap the player sideways.
        Set<SurfaceOwner> verticallyResolved = new HashSet<>();

        for (Collider collider : collidingObjects) {
            if (collider instanceof SurfaceCollider surface) {
                Direction dir = surface.getSurfaceDirection();
                if (dir == Direction.UP || dir == Direction.DOWN) {
                    handleSurfaceCollision(surface);
                    verticallyResolved.add(surface.getOwner());
                }
            }
        }

        for (Collider collider : collidingObjects) {
            if (collider instanceof SurfaceCollider surface) {
                Direction dir = surface.getSurfaceDirection();
                if ((dir == Direction.LEFT || dir == Direction.RIGHT)
                        && !verticallyResolved.contains(surface.getOwner())) {
                    handleSurfaceCollision(surface);
                }
            }
            if (collider instanceof Slime) {
                handleSlimeCollision((Slime) collider);
            }
        }
    }

    private void handleSurfaceCollision(SurfaceCollider surface) {
        SurfaceOwner owner = surface.getOwner();
        if (EleSlime.DEBUG) {
            player.addCollidingTile(owner.getClass().getSimpleName() + " ← " + surface.getSurfaceDirection().name());
        }
        switch(surface.getSurfaceDirection()) {
            case Direction.UP:
                player.setAnchorLocationY(owner.getAnchorLocation().getY() + owner.getHeight());
                break;
            case Direction.DOWN:
                player.setAnchorLocationY(owner.getAnchorLocation().getY() - player.getBodyHeight());
                if (player.isFalling()) {
                    player.endKnockback();
                }
                break;
            case Direction.LEFT:
                player.setAnchorLocationX(owner.getAnchorLocation().getX() + owner.getWidth());
                break;
            case Direction.RIGHT:
                player.setAnchorLocationX(owner.getAnchorLocation().getX() - player.getBodyWidth());
                break;
        }
    }

    private void handleSlimeCollision(Slime slime) {
        slime.onPlayerCollision(player);
    }
}