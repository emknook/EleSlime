package nl.han.jefmk.surfaces;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;

public class SurfaceCollider extends RectangleEntity implements Collider {

    private final Direction surfaceDirection;
    private final SurfaceOwner owner;

    public SurfaceCollider(Coordinate2D initialLocation, Direction surfaceDirection, SurfaceOwner owner, Size size) {
        super(initialLocation, size);
        this.surfaceDirection = surfaceDirection;
        this.owner = owner;
        if (EleSlime.DEBUG) {
            setFill(switch (surfaceDirection) {
                case DOWN -> Color.RED;
                case RIGHT -> Color.GREEN;
                case UP -> Color.YELLOW;
                case LEFT -> Color.BLUE;
                default -> Color.TRANSPARENT;
            });
        }
    }

    public SurfaceCollider(Coordinate2D initialLocation, Direction surfaceDirection, SurfaceOwner owner) {
        super(initialLocation);
        this.surfaceDirection = surfaceDirection;
        this.owner = owner;
        double collisionMargin = 5; // margin to not have the surfaces overlap within one tile, which may cause confusing behavior
        switch (surfaceDirection) {
            case LEFT, RIGHT -> setHeight(EleSlime.TILE_SIZE - collisionMargin);
            case DOWN, UP -> setWidth(EleSlime.TILE_SIZE - collisionMargin);
        }
        switch(surfaceDirection) {
            case LEFT -> setAnchorLocationX(initialLocation.getX() + EleSlime.TILE_SIZE);
            case UP -> setAnchorLocationY(initialLocation.getY() + EleSlime.TILE_SIZE);
            case DOWN -> setAnchorLocationY(initialLocation.getY() - 2);
            case RIGHT ->  setAnchorLocationX(initialLocation.getX() - 2);
        }
        switch (surfaceDirection) {
            case UP, DOWN -> setAnchorLocationX(this.getAnchorLocation().getX() + collisionMargin / 2);
            case RIGHT, LEFT -> setAnchorLocationY(this.getAnchorLocation().getY() + collisionMargin / 2);
        }
        if (EleSlime.DEBUG) {
            setFill(switch (surfaceDirection) {
                case DOWN -> Color.RED;
                case RIGHT -> Color.GREEN;
                case UP -> Color.YELLOW;
                case LEFT -> Color.BLUE;
                default -> Color.TRANSPARENT;
            });
        }
    }

    public SurfaceOwner getOwner() {
        return owner;
    }

    public Direction getSurfaceDirection() {
        return surfaceDirection;
    }
}
