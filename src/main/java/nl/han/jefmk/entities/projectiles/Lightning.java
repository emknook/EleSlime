package nl.han.jefmk.entities.projectiles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.DynamicCompositeEntity;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.mobs.EnemySlime;
import nl.han.jefmk.scenes.GameScene;

import java.util.ArrayList;

public class Lightning extends DynamicCompositeEntity {


    private static final double SPEED = 14d;

    private final int bouncesLeft;

    private final ArrayList<EnemySlime> potentialTargets;
    private final GameScene gameScene;

    public Lightning(final Coordinate2D coordinate2D, final double rotation, GameScene gameScene, int bouncesLeft) {
        super(coordinate2D);
        this.gameScene = gameScene;
        this.bouncesLeft = bouncesLeft;
        this.setRotate(-rotation);
        potentialTargets = new ArrayList<>();
        setMotion(SPEED, 90 - rotation);
    }

    @Override
    protected void setupEntities() {
        double chainColliderSize = EleSlime.TILE_SIZE * 5;
        var lightningChainCollider = new LightningChainZoneCollider(new  Coordinate2D(0, 0), this, chainColliderSize);
        addEntity(lightningChainCollider);
        var lightningCollider = new LightningCollider(new Coordinate2D(0, 0), this);
        addEntity(lightningCollider);
        var lightningSprite = new LightningSprite(new Coordinate2D(0, 0));
        addEntity(lightningSprite);
    }

    protected void addPotentialTarget(EnemySlime enemySlime) {
        this.potentialTargets.add(enemySlime);
    }

    protected void clearTargets() {
        this.potentialTargets.clear();
    }

    public void chainEffect() {
        for(EnemySlime target :potentialTargets) {
            double chainChance = Math.random();
            if(chainChance < 0.10) {
                double theta = Math.atan((target.getAnchorLocation().getY() - this.getAnchorLocation().getY()) /  (target.getAnchorLocation().getX() - this.getAnchorLocation().getX()));
                double angle = theta * (180/Math.PI);
                gameScene.createLightningBolt(this.getAnchorLocation(), angle, bouncesLeft - 1);
            }
        }
    }

    public void removePotentialTarget(EnemySlime enemySlime) {
        potentialTargets.remove(enemySlime);
    }
}
