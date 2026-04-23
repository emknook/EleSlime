package nl.han.jefmk.surfaces;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;

public class SurfaceCollider extends RectangleEntity implements Collider {

    private final Direction surfaceDirection;
    private final Tile tile;

    protected SurfaceCollider(Coordinate2D initialLocation, Direction surfaceDirection, Tile tile) {
        super(initialLocation);
        this.surfaceDirection = surfaceDirection;
        this.tile = tile;
        setFill(EleSlime.DEBUG ?
                (surfaceDirection == Direction.LEFT ?
                    Color.RED :
                surfaceDirection == Direction.RIGHT ?
                    Color.GREEN :
                surfaceDirection == Direction.UP ?
                    Color.BLUE :
                    Color.YELLOW)
                : Color.TRANSPARENT);
        switch (surfaceDirection) {
            case LEFT, RIGHT -> setHeight(EleSlime.TILE_SIZE);
            case DOWN, UP -> setWidth(EleSlime.TILE_SIZE);
        }
        switch(surfaceDirection) {
            case LEFT -> setAnchorLocationX(initialLocation.getX() + EleSlime.TILE_SIZE);
            case UP -> setAnchorLocationY(initialLocation.getY() + EleSlime.TILE_SIZE);
        }
    }

    public Tile getTile() {
        return tile;
    }

    public Direction getSurfaceDirection() {
        return surfaceDirection;
    }
}
