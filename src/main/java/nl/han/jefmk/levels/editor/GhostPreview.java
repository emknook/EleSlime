package nl.han.jefmk.levels.editor;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import nl.han.jefmk.EleSlime;

public class GhostPreview extends DynamicSpriteEntity {

    private static final int[] TILE_FRAME_MAP = {
            3,  // floor
            13, // ceiling
            9,  // wall_left
            7,  // wall_right
            2,  // corner_top_left
            4,  // corner_top_right
            12, // corner_bottom_left
            14, // corner_bottom_right
            0,  // inner_corner_top_left
            1,  // inner_corner_top_right
            5,  // inner_corner_bottom_left
            6,  // inner_corner_bottom_right
            8,  // wall_filling
    };

    private final String spriteResource;
    private final boolean isTileGhost;

    /**
     * Tile ghost: uses tileset with frame map.
     */
    public GhostPreview(Coordinate2D initialLocation) {
        super("sprites/tileset.png", initialLocation, new Size(EleSlime.TILE_SIZE), 3, 5);
        setOpacity(0.3);
        setCurrentFrameIndex(TILE_FRAME_MAP[0]);
        this.spriteResource = "sprites/tileset.png";
        this.isTileGhost = true;
    }

    /**
     * Pickup/spawn ghost: uses a single sprite image.
     */
    public GhostPreview(String spriteResource, Coordinate2D initialLocation) {
        super(spriteResource, initialLocation, new Size(EleSlime.TILE_SIZE));
        setOpacity(0.3);
        this.spriteResource = spriteResource;
        this.isTileGhost = false;
    }

    public void updatePosition(Coordinate2D snappedPosition) {
        setAnchorLocation(snappedPosition);
    }

    public void showTileFrame(int tileTypeIndex) {
        if (isTileGhost && tileTypeIndex >= 0 && tileTypeIndex < TILE_FRAME_MAP.length) {
            setCurrentFrameIndex(TILE_FRAME_MAP[tileTypeIndex]);
        }
    }
}
