package nl.han.jefmk.surfaces;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import nl.han.jefmk.EleSlime;

public class TileSprite extends SpriteEntity {

    private final int frameIndex;

    public TileSprite(Coordinate2D initialLocation, TileType type) {
        super("sprites/tileset.png", initialLocation, new Size(EleSlime.TILE_SIZE), 3, 5);
        frameIndex = switch (type) {
            case OUTER_CORNER_TOP_LEFT     -> 0;
            case OUTER_CORNER_TOP_RIGHT    -> 1;
            case CORNER_TOP_LEFT           -> 2;
            case FLOOR                     -> 3;
            case CORNER_TOP_RIGHT          -> 4;
            case OUTER_CORNER_BOTTOM_LEFT  -> 5;
            case OUTER_CORNER_BOTTOM_RIGHT -> 6;
            case WALL_RIGHT                -> 7;
            case WALL_FILLING              -> 8;
            case WALL_LEFT                 -> 9;
            case CORNER_BOTTOM_LEFT        -> 12;
            case CEILING                   -> 13;
            case CORNER_BOTTOM_RIGHT       -> 14;
        };
        this.setCurrentFrameIndex(frameIndex);
    }
}