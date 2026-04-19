package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;

public class BlueShroom extends SpriteEntity implements Collider {

    protected BlueShroom(Coordinate2D initialLocation) {
        super("sprites/blue-shroom.png",initialLocation);
    }
}
