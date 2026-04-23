package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;

public abstract class Pickup extends SpriteEntity {
    protected Pickup(String resource, Coordinate2D initialLocation) {
        super(resource, initialLocation);
    }
}
