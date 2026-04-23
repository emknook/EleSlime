package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.obstacles.Obstacle;
import nl.han.jefmk.entities.pickups.Pickup;
import nl.han.jefmk.surfaces.SurfaceCollider;
import nl.han.jefmk.surfaces.Tile;

import java.util.List;

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
        for (Collider collider : collidingObjects) {
            if (collider instanceof SurfaceCollider) {
                handleSurfaceCollision((SurfaceCollider) collider);
            }
            if (collider instanceof Obstacle) {
                handleObstacleCollision((Obstacle) collider);
            }
            if  (collider instanceof Pickup) {
                handlePickupCollision((Pickup) collider);
            }
        }
    }

    private void handleSurfaceCollision(SurfaceCollider surface) {
        Tile tile = surface.getTile();
        switch(surface.getSurfaceDirection()) {
            case Direction.UP:
                player.setAnchorLocationY(tile.getAnchorLocation().getY() + tile.getHeight());
                break;
            case Direction.DOWN:
                player.setAnchorLocationY(tile.getAnchorLocation().getY() - player.getHeight());
                break;
            case Direction.LEFT:
                player.setAnchorLocationX(tile.getAnchorLocation().getX() + tile.getWidth());
                break;
            case Direction.RIGHT:
                player.setAnchorLocationX(tile.getAnchorLocation().getX() - player.getWidth());
                break;
        }
    }

    private void handleObstacleCollision(Obstacle obstacle) {
        //damage, sulphur, stalagmite
    }

    private void handlePickupCollision(Pickup pickup) {
        //blueshroom, greenshroom
    }

}