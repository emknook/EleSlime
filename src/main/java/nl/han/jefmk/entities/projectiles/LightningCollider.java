package nl.han.jefmk.entities.projectiles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.mobs.EnemySlime;
import nl.han.jefmk.entities.player.Player;
import nl.han.jefmk.entities.player.PlayerCollider;
import nl.han.jefmk.entities.player.PlayerStickyCollider;

import java.util.List;

public class LightningCollider extends RectangleEntity implements Collided {

    private final Lightning lightning;

    protected LightningCollider(Coordinate2D initialLocation, Lightning lightning) {
        super(initialLocation);
        this.lightning = lightning;
        this.setFill(EleSlime.DEBUG ? Color.RED : Color.TRANSPARENT);
        this.setWidth(EleSlime.TILE_SIZE);
        this.setHeight(20d);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            switch (collider) {
                case EnemySlime enemySlime -> {
                    enemySlime.getHitByLightning();
                    lightning.removePotentialTarget(enemySlime);
                    lightning.chainEffect();
                    lightning.remove();
                }
                case PlayerCollider playerCollider -> {
                }
                case PlayerStickyCollider playerStickyCollider -> {
                }
                case Player player -> {
                }
                case null, default -> lightning.remove();
            }
        }
    }
}
