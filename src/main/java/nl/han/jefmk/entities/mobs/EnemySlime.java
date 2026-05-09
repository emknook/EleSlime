package nl.han.jefmk.entities.mobs;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Timer;
import com.github.hanyaeger.api.TimerContainer;
import com.github.hanyaeger.api.entities.Animation;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.LoopingAnimation;
import nl.han.jefmk.entities.player.Player;

public class EnemySlime extends Slime implements TimerContainer {

    private final Animation idleLeftAnimation = new LoopingAnimation(1, 7, 1, 8);
    private final Animation idleRightAnimation = new LoopingAnimation(0, 0, 0, 1);

    private final Animation leftAnimation = new LoopingAnimation(1, 0, 1, 6);
    private final Animation rightAnimation = new LoopingAnimation(0, 2, 0, 8);

    private MovingState movingState = MovingState.IDLE_LEFT;

    private int xLocation = 0;

    public EnemySlime(Coordinate2D initialLocation) {
        super("sprites/enemy-spritesheet.png", initialLocation, 2, 9, 1);
        idleLeft();
    }

    public void idleLeft() {
        setMotion(0,0);
        movingState = MovingState.IDLE_LEFT;
        setAutoCycle(300);
        playAnimation(idleLeftAnimation);
    }

    public void idleRight() {
        setMotion(0,0);
        movingState = MovingState.IDLE_RIGHT;
        setAutoCycle(300);
        playAnimation(idleRightAnimation);
    }

    public void left() {
        if (xLocation <= -2) { //only move left 2 times
            idleLeft();
            return;
        }
        xLocation--;
        addToMotion(3, Direction.LEFT);
        movingState = MovingState.MOVING_LEFT;
        setAutoCycle(100);
        playAnimation(leftAnimation);
    }

    public void right() {
        if (xLocation >= 2) { //only move right 2 times
            idleRight();
            return;
        }
        xLocation++;
        addToMotion(3, Direction.RIGHT);
        movingState = MovingState.MOVING_RIGHT;
        setAutoCycle(100);
        playAnimation(rightAnimation);
    }

    @Override
    public void setupTimers() {
        addTimer(new MovingTimer(700));
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.takeDamage();
    }

    public void getHitByLightning() {
        takeDamage();
        //TODO: add death animation, then remove entity
        if (this.getHealth() <= 0) {
            this.remove();
        }
    }

    private class MovingTimer extends Timer {
        /**
         * Create a new instance of {@link Timer} for the given interval in milliseconds.
         *
         * @param intervalInMs the interval in milliseconds
         */
        protected MovingTimer(long intervalInMs) {
            super(intervalInMs);
        }

        @Override
        public void onAnimationUpdate(long timestamp) {
            double changeBehaviourOption = Math.random();
            if(changeBehaviourOption < 0.75) {
                //if moving right, or idling left, can decide to idle right
                //if moving left, or idling left, can decide to idle left
                switch (movingState) {
                    case IDLE_LEFT, MOVING_RIGHT:
                        idleRight();
                        break;
                    case IDLE_RIGHT, MOVING_LEFT:
                        idleLeft();
                        break;
                }
            } else {
                //if moving left, or idling left, can decide to continue left, or start moving left
                //if moving right, or idling right, can decide to continue right, or start moving right
                switch (movingState) {
                    case IDLE_LEFT, MOVING_LEFT:
                        left();
                        break;
                    case IDLE_RIGHT, MOVING_RIGHT:
                        right();
                        break;
                }
            }
        }
    }
}
