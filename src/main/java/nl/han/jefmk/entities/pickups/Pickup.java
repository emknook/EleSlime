package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import nl.han.jefmk.EleSlime;

public abstract class Pickup extends SpriteEntity {
    protected Pickup(String resource, Coordinate2D initialLocation) {
        super(resource, initialLocation, new Size(EleSlime.TILE_SIZE));
    }
}
