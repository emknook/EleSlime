package nl.han.jefmk.entities.projectiles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.mobs.EnemySlime;

import java.util.List;

public class LightningChainZoneCollider extends CircleEntity implements Collided {

    private final Lightning lightning;

    public LightningChainZoneCollider(final Coordinate2D initialLocation, Lightning lightning, double size) {
        super(new Coordinate2D(initialLocation.getX() - size, initialLocation.getY() - size));
        this.setRadius(size);
        this.setFill(EleSlime.DEBUG ? Color.BLUEVIOLET : Color.TRANSPARENT);
        this.lightning = lightning;
    }


    @Override
    public void onCollision(List<Collider> collidingObjects) {
        lightning.clearTargets();
        for (Collider collider : collidingObjects) {
            if (collider instanceof EnemySlime enemySlime) {
                lightning.addPotentialTarget(enemySlime);
            }
        }
    }
}
