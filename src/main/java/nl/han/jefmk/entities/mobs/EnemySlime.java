package nl.han.jefmk.entities.mobs;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Timer;
import com.github.hanyaeger.api.TimerContainer;
import com.github.hanyaeger.api.entities.Animation;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.LoopingAnimation;
import nl.han.jefmk.entities.player.Player;

import java.util.Random;
public class EnemySlime extends Slime implements TimerContainer {

    private static final String SPRITE_PATH = "sprites/enemy-spritesheet.png";

    private static final double MOVEMENT_SPEED = 3d;
    private static final double DISTANCE_TO_APPROACH = 300d;

    private static final int MAX_PATROL_OFFSET = 2;

    private static final long IDLE_ANIMATION_INTERVAL = 300;
    private static final long MOVE_ANIMATION_INTERVAL = 100;
    private static final long MOVEMENT_TIMER_INTERVAL = 700;

    private final Animation idleLeftAnimation = new LoopingAnimation(1, 7, 1, 8);
    private final Animation idleRightAnimation = new LoopingAnimation(0, 0, 0, 1);
    private final Animation moveLeftAnimation = new LoopingAnimation(1, 0, 1, 6);
    private final Animation moveRightAnimation = new LoopingAnimation(0, 2, 0, 8);

    private final Random random = new Random();
    private final Player player;

    private MovingState movingState = MovingState.IDLE_LEFT;
    private int patrolOffset = 0;

    public EnemySlime(Coordinate2D initialLocation, Player player) {
        super(SPRITE_PATH, initialLocation, 2, 9, 1);
        this.player = player;
        idleLeft();
    }

    public EnemySlime(Coordinate2D initialLocation) {
        this(initialLocation, null);
    }

    @Override
    public void setupTimers() {
        addTimer(new MovingTimer(MOVEMENT_TIMER_INTERVAL));
    }

    private void updateMovement() {
        if (approachPlayerIfNearby()) {
            return;
        }

        updatePatrolMovement();
    }

    private boolean approachPlayerIfNearby() {
        if (player == null) {
            return false;
        }

        if (isPlayerOnLeftWithinApproachDistance()) {
            moveLeft(true);
            return true;
        }

        if (isPlayerOnRightWithinApproachDistance()) {
            moveRight(true);
            return true;
        }

        return false;
    }

    private void updatePatrolMovement() {
        double changeBehaviourOption = random.nextDouble();

        if (changeBehaviourOption < 0.75) {
            switchIdleDirection();
        } else {
            continueOrStartMovingInFacingDirection();
        }
    }

    private void switchIdleDirection() {
        switch (movingState) {
            case IDLE_LEFT, MOVING_RIGHT -> idleRight();
            case IDLE_RIGHT, MOVING_LEFT -> idleLeft();
        }
    }

    private void continueOrStartMovingInFacingDirection() {
        switch (movingState) {
            case IDLE_LEFT, MOVING_LEFT -> moveLeft(false);
            case IDLE_RIGHT, MOVING_RIGHT -> moveRight(false);
        }
    }

    private void moveLeft(boolean ignorePatrolLimit) {
        if (patrolOffset <= -MAX_PATROL_OFFSET && !ignorePatrolLimit) {
            idleLeft();
            return;
        }

        patrolOffset--;
        movingState = MovingState.MOVING_LEFT;
        setMotion(MOVEMENT_SPEED, Direction.LEFT);
        setAutoCycle(MOVE_ANIMATION_INTERVAL);
        playAnimation(moveLeftAnimation);
    }

    private void moveRight(boolean ignorePatrolLimit) {
        if (patrolOffset >= MAX_PATROL_OFFSET && !ignorePatrolLimit) {
            idleRight();
            return;
        }

        patrolOffset++;
        movingState = MovingState.MOVING_RIGHT;
        setMotion(MOVEMENT_SPEED, Direction.RIGHT);
        setAutoCycle(MOVE_ANIMATION_INTERVAL);
        playAnimation(moveRightAnimation);
    }

    private void idleLeft() {
        movingState = MovingState.IDLE_LEFT;
        setMotion(0, 0);
        setAutoCycle(IDLE_ANIMATION_INTERVAL);
        playAnimation(idleLeftAnimation);
    }

    private void idleRight() {
        movingState = MovingState.IDLE_RIGHT;
        setMotion(0, 0);
        setAutoCycle(IDLE_ANIMATION_INTERVAL);
        playAnimation(idleRightAnimation);
    }

    private boolean isPlayerOnLeftWithinApproachDistance() {
        double playerMaxX = player.getBoundingBox().getMaxX();
        double enemyMinX = getBoundingBox().getMinX();

        return playerMaxX > enemyMinX - DISTANCE_TO_APPROACH
                && playerMaxX < enemyMinX;
    }

    private boolean isPlayerOnRightWithinApproachDistance() {
        double playerMinX = player.getBoundingBox().getMinX();
        double enemyMaxX = getBoundingBox().getMaxX();

        return playerMinX < enemyMaxX + DISTANCE_TO_APPROACH
                && playerMinX > enemyMaxX;
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.takeDamage();
        player.takeKnockback(this);
    }

    public void getHitByLightning() {
        takeDamage();

        if (getHealth() <= 0) {
            remove();
        }
    }

    private class MovingTimer extends Timer {

        protected MovingTimer(long intervalInMs) {
            super(intervalInMs);
        }

        @Override
        public void onAnimationUpdate(long timestamp) {
            updateMovement();
        }
    }
}