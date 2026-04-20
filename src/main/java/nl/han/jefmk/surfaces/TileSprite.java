package nl.han.jefmk.surfaces;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import nl.han.jefmk.EleSlime;

public class TileSprite extends SpriteEntity {
    protected TileSprite(Coordinate2D initialLocation, TileType type) {
        super("sprites/tileset.png", initialLocation, new Size(EleSlime.TILE_SIZE), 3, 5);
        switch (type) {
            case CORNER_TOP_LEFT ->  this.setCurrentFrameIndex(2);
            case FLOOR -> this.setCurrentFrameIndex(3);
            case CORNER_TOP_RIGHT ->  this.setCurrentFrameIndex(4);
            case WALL_RIGHT ->  this.setCurrentFrameIndex(7);
            case WALL_LEFT ->  this.setCurrentFrameIndex(9);
            case CORNER_BOTTOM_LEFT ->   this.setCurrentFrameIndex(12);
            case CEILING -> this.setCurrentFrameIndex(13);
            case CORNER_BOTTOM_RIGHT ->  this.setCurrentFrameIndex(14);
        }
    }
}
