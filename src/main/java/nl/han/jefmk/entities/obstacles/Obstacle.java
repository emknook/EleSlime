package nl.han.jefmk.entities.obstacles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;

public abstract class Obstacle extends DynamicSpriteEntity {

    private final ObstacleCollider obstacleCollider;

    protected Obstacle(String resource, Coordinate2D initialLocation) {
        this(resource, initialLocation, new Size(EleSlime.TILE_SIZE), new Coordinate2D(0, 0));
    }

    protected Obstacle(String resource, Coordinate2D initialLocation, Size colliderSize, Coordinate2D colliderOffset) {
        super(resource, initialLocation, new Size(EleSlime.TILE_SIZE), 1, 1);
        Coordinate2D colliderLocation = new Coordinate2D(
                initialLocation.getX() + colliderOffset.getX(),
                initialLocation.getY() + colliderOffset.getY()
        );
        obstacleCollider = new ObstacleCollider(this, colliderSize, colliderLocation);
    }

    protected Obstacle(String resource, Coordinate2D initialLocation, Size colliderSize, Coordinate2D colliderOffset, int rows, int cols) {
        super(resource, initialLocation, new Size(EleSlime.TILE_SIZE),  rows, cols);
        Coordinate2D colliderLocation = new Coordinate2D(
                initialLocation.getX() + colliderOffset.getX(),
                initialLocation.getY() + colliderOffset.getY()
        );
        obstacleCollider = new ObstacleCollider(this, colliderSize, colliderLocation);
    }

    public ObstacleCollider getObstacleCollider() {
        return obstacleCollider;
    }

    public abstract void onPlayerCollision(Player player);
}
