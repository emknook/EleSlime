package nl.han.jefmk.entities;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;

public class Player extends DynamicSpriteEntity {

    public Player(Coordinate2D initialLocation) {
        super("sprites/eleslime-idle.png", initialLocation, new Size(64, 64), 1, 2);

        setAutoCycle(800);
    }
}
