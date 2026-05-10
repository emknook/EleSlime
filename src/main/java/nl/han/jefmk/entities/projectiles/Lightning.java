package nl.han.jefmk.entities.projectiles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.DynamicCompositeEntity;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.mobs.EnemySlime;
import nl.han.jefmk.scenes.GameScene;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Lightning extends DynamicCompositeEntity {


    private static final double SPEED = 14d;
    private static final double CHAIN_RANGE_IN_TILES = 5d;
    private static final double CHAIN_CHANCE = 0.10d;

    private final int bouncesLeft;

    private final Set<EnemySlime> potentialTargets = new HashSet<>();
    private final GameScene gameScene;
    private final Random random = new Random();

    public Lightning(final Coordinate2D coordinate2D, final double rotation, GameScene gameScene, int bouncesLeft) {
        super(coordinate2D);
        this.gameScene = gameScene;
        this.bouncesLeft = bouncesLeft;
        this.setRotate(-rotation);
        setMotion(SPEED, 90 - rotation);
    }

    @Override
    protected void setupEntities() {
        double chainColliderSize = EleSlime.TILE_SIZE * CHAIN_RANGE_IN_TILES;
        Coordinate2D relativeOrigin = new  Coordinate2D(0, 0);
        var lightningChainCollider = new LightningChainZoneCollider(relativeOrigin, this, chainColliderSize);
        addEntity(lightningChainCollider);
        var lightningCollider = new LightningCollider(relativeOrigin, this);
        addEntity(lightningCollider);
        var lightningSprite = new LightningSprite(relativeOrigin);
        addEntity(lightningSprite);
    }

    protected void addPotentialTarget(EnemySlime enemySlime) {
        this.potentialTargets.add(enemySlime);
    }

    protected void clearTargets() {
        this.potentialTargets.clear();
    }

    public void chainEffect() {
        if (bouncesLeft <= 0) {
            return;
        }
        for(EnemySlime target :potentialTargets) {
            double chainChance = random.nextDouble();
            if(chainChance < CHAIN_CHANCE) {
                double deltaY = target.getAnchorLocation().getY() - getAnchorLocation().getY();
                double deltaX = target.getAnchorLocation().getX() - getAnchorLocation().getX();

                double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));
                gameScene.createLightningBolt(this.getAnchorLocation(), angle, bouncesLeft - 1);
            }
        }
    }

    public void removePotentialTarget(EnemySlime enemySlime) {
        potentialTargets.remove(enemySlime);
    }
}
