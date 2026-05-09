package nl.han.jefmk.surfaces;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.CompositeEntity;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import nl.han.jefmk.EleSlime;

public class Tile extends CompositeEntity implements SurfaceOwner {

    private final TileType type;

    public Tile(Coordinate2D initialLocation, TileType type) {
        super(initialLocation);
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    @Override
    protected void setupEntities() {
        Coordinate2D thisLocation = new Coordinate2D(0, 0);
        switch (type) {
            case CEILING:
                addEntity(new SurfaceCollider(thisLocation, Direction.UP, this));
                break;
            case FLOOR:
                addEntity(new SurfaceCollider(thisLocation, Direction.DOWN, this));
                break;
            case WALL_LEFT:
                addEntity(new SurfaceCollider(thisLocation, Direction.LEFT, this));
                break;
            case WALL_RIGHT:
                addEntity(new SurfaceCollider(thisLocation, Direction.RIGHT, this));
                break;
            case CORNER_BOTTOM_LEFT:
                addEntity(new SurfaceCollider(thisLocation, Direction.UP, this));
                addEntity(new SurfaceCollider(thisLocation, Direction.RIGHT, this));
                break;
            case CORNER_BOTTOM_RIGHT:
                addEntity(new SurfaceCollider(thisLocation, Direction.UP, this));
                addEntity(new SurfaceCollider(thisLocation, Direction.LEFT, this));
                break;
            case CORNER_TOP_LEFT:
                addEntity(new SurfaceCollider(thisLocation, Direction.DOWN, this));
                addEntity(new SurfaceCollider(thisLocation, Direction.RIGHT, this));
                break;
            case CORNER_TOP_RIGHT:
                addEntity(new SurfaceCollider(thisLocation, Direction.DOWN, this));
                addEntity(new SurfaceCollider(thisLocation, Direction.LEFT, this));
                break;
            default:
                break;
        }
        SpriteEntity sprite = new TileSprite(thisLocation, type);
        addEntity(sprite);
    }

    @Override
    public double getHeight() {
        return EleSlime.TILE_SIZE;
    }

    @Override
    public double getWidth() {
        return EleSlime.TILE_SIZE;
    }
}
