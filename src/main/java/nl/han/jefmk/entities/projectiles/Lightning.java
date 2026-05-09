package nl.han.jefmk.entities.projectiles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.DynamicCompositeEntity;

public class Lightning extends DynamicCompositeEntity {

    private final Direction direction;

    public Lightning(final Coordinate2D coordinate2D, final Direction direction) {
        super(coordinate2D);
        this.direction = direction;
        setMotion(0, direction);
    }

    @Override
    protected void setupEntities() {
        var lightningCollider = new LightningCollider(this.getAnchorLocation(), this.direction);
        addEntity(lightningCollider);
        var lightningSprite = new LightningSprite( this.getAnchorLocation());
        addEntity(lightningSprite);
    }
}
