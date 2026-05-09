package nl.han.jefmk.entities.projectiles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.DynamicCompositeEntity;

public class Lightning extends DynamicCompositeEntity {

    private final Direction direction;

    private static final double SPEED = 10d;

    public Lightning(final Coordinate2D coordinate2D, final Direction direction) {
        super(coordinate2D);
        this.direction = direction;
        setMotion(SPEED, direction);
    }

    @Override
    protected void setupEntities() {
        var lightningCollider = new LightningCollider(new Coordinate2D(0, 0), this.direction);
        addEntity(lightningCollider);
        var lightningSprite = new LightningSprite(new Coordinate2D(0, 0));
        addEntity(lightningSprite);
    }
}
