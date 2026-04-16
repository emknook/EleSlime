package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.surfaces.Tile;

import java.util.List;

public class PlayerCollider extends CircleEntity implements Collided {
    private final Player player;

    protected PlayerCollider(Player player, double radius, Coordinate2D initialLocation) {
        super(initialLocation);
        this.player = player;
        setFill(Color.BLUE);
        setRadius(radius);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            if (!(collider instanceof Tile tile)) {
                continue;
            }

            Direction touchingDirection = determineTouchingDirection(tile);

            if (touchingDirection == null) {
                continue;
            }

            pushPlayerOutOfTile(tile, touchingDirection);
            player.addTouchingSurfaceDirection(touchingDirection);
        }
    }

    private Direction determineTouchingDirection(Tile tile) {
        double playerCenterX = player.getAnchorLocation().getX() + (player.getWidth() / 2);
        double playerCenterY = player.getAnchorLocation().getY() + (player.getHeight() / 2);

        double tileCenterX = tile.getAnchorLocation().getX() + (tile.getWidth() / 2);
        double tileCenterY = tile.getAnchorLocation().getY() + (tile.getHeight() / 2);

        double deltaX = tileCenterX - playerCenterX;
        double deltaY = tileCenterY - playerCenterY;

        if (deltaX > deltaY) {
            return deltaX > 0 ? Direction.RIGHT : Direction.LEFT;
        }

        return deltaY > 0 ? Direction.DOWN : Direction.UP;
    }

    private void pushPlayerOutOfTile(Tile tile, Direction touchingDirection) {
        switch (touchingDirection) {
            case DOWN -> {
                player.setAnchorLocationY(tile.getAnchorLocation().getY() - player.getHeight());
                player.setVerticalSpeed(0);
            }
            case UP -> {
                player.setAnchorLocationY(tile.getAnchorLocation().getY() + tile.getHeight());
                player.setVerticalSpeed(0);
            }
            case LEFT -> {
                player.setAnchorLocationX(tile.getAnchorLocation().getX() + tile.getWidth());
                player.setHorizontalSpeed(0);
            }
            case RIGHT -> {
                player.setAnchorLocationX(tile.getAnchorLocation().getX() - player.getWidth());
                player.setHorizontalSpeed(0);
            }
            default -> {}
        }
    }
}