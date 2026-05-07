package nl.han.jefmk.entities.mobs;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import nl.han.jefmk.EleSlime;

public class Slime extends Mob {
    protected Slime(String resource, Coordinate2D initialLocation, int rows, int columns) {
        super(resource, initialLocation, new Size(EleSlime.TILE_SIZE), rows, columns);
    }
}
