package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;

public class PlayerSprite extends DynamicSpriteEntity {
    public PlayerSprite(Size size, Coordinate2D initialLocation) {
        super("sprites/eleslime-spritesheet.png", initialLocation, size, 1, 2);
        setCurrentFrameIndex(0);
        setAutoCycle(300);
    }
}
