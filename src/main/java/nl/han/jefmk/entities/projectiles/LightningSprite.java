package nl.han.jefmk.entities.projectiles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;

public class LightningSprite extends DynamicSpriteEntity {
    protected LightningSprite(Coordinate2D initialLocation) {
        super("sprites/lightning.png", initialLocation, 1, 5);
        setCurrentFrameIndex(0);
        setAutoCycle(100);
    }
}
