package nl.han.jefmk.entities.obstacles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;

public class Obstacle extends SpriteEntity implements Collider {

    protected Obstacle(String resource, Coordinate2D initialLocation) {
        super(resource, initialLocation);
    }

}
