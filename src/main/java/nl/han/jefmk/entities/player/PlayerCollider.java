package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.entities.obstacles.Obstacle;
import nl.han.jefmk.entities.pickups.Pickup;
import nl.han.jefmk.surfaces.Tile;
import nl.han.jefmk.surfaces.TileType;

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
            if (collider instanceof Tile) {
                handleTileCollision((Tile) collider);
            }
            if (collider instanceof Obstacle) {
                handleObstacleCollision((Obstacle) collider);
            }
            if  (collider instanceof Pickup) {
                handlePickupCollision((Pickup) collider);
            }
        }
    }

    private void handleTileCollision(Tile tile) {
        switch(tile.getType()) {
            case TileType.CEILING:
                player.setAnchorLocationY(tile.getAnchorLocation().getY() + tile.getHeight());
                player.addTouchingSurfaceDirection(Direction.UP);
                break;
            case TileType.FLOOR:
                player.setAnchorLocationY(tile.getAnchorLocation().getY() - player.getHeight());
                player.addTouchingSurfaceDirection(Direction.DOWN);
                break;
            case TileType.WALL_LEFT:
                player.setAnchorLocationX(tile.getAnchorLocation().getX() + tile.getWidth());
                player.addTouchingSurfaceDirection(Direction.LEFT);
                break;
            case TileType.WALL_RIGHT:
                player.setAnchorLocationX(tile.getAnchorLocation().getX() - player.getWidth());
                player.addTouchingSurfaceDirection(Direction.RIGHT);
                break;
            default:
                handleCornerCollision(tile);
        }
    }

    private void handleCornerCollision(Tile tile) {
        switch(tile.getType()) {
            case TileType.CORNER_BOTTOM_LEFT ->
                System.out.println("Might have to give this a skip because this is difficult, could potentially solve it with 2 separate colliders per block?");
            case TileType.CORNER_BOTTOM_RIGHT ->
                System.out.println("Might have to give this a skip because this is difficult, could potentially solve it with 2 separate colliders per block?");
            case TileType.CORNER_TOP_LEFT ->
                System.out.println("Might have to give this a skip because this is difficult, could potentially solve it with 2 separate colliders per block?");
            case TileType.CORNER_TOP_RIGHT ->
                System.out.println("Might have to give this a skip because this is difficult, could potentially solve it with 2 separate colliders per block?");
        }
    }

    private void handleObstacleCollision(Obstacle obstacle) {
        //damage, sulphur, stalagmite
    }

    private void handlePickupCollision(Pickup pickup) {
        //blueshroom, greenshroom
    }

}