package nl.han.jefmk.entities.obstacles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;

public abstract class Obstacle extends SpriteEntity implements Collider {

    protected Obstacle(String resource, Coordinate2D initialLocation) {
        super(resource, initialLocation, new Size(EleSlime.TILE_SIZE));
    }

    public abstract void onCollision(Collider collider);
}
