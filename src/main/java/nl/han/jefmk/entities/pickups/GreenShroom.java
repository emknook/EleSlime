package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;

public class GreenShroom extends SpriteEntity implements Collider {

    protected GreenShroom(Coordinate2D initialLocation) {
        super("sprites/green-shroom.png",initialLocation);
    }
}
