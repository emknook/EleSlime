package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.*;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import nl.han.jefmk.entities.mobs.MovingState;

public class PlayerSprite extends DynamicSpriteEntity {

    private final Animation idleLeftAnimation = new LoopingAnimation(1, 17, 1, 18);
    private final Animation idleRightAnimation = new LoopingAnimation(0, 0, 0, 1);
    private final Animation movingRightAnimation = new LoopingAnimation(0, 12, 0, 18);
    private final Animation movingLeftAnimation = new LoopingAnimation(1, 0, 1, 6);

    private final Animation jumpingRightAnimation;
    private final Animation jumpingLeftAnimation;

    private MovingState movingState = MovingState.IDLE_RIGHT;
    private boolean isJumping = false;

    public PlayerSprite(Size size, Coordinate2D initialLocation) {
        super("sprites/eleslime-spritesheet.png", initialLocation, size, 2, 20);
        setCurrentFrameIndex(0);
        setAutoCycle(300);
        idleRight();

        PlayerJumpCallBack playerJumpCallBack = new  PlayerJumpCallBack(this);
        jumpingLeftAnimation = new LinkedAnimationWithCallBack(1, 5, 1, 15, idleLeftAnimation, playerJumpCallBack);
        jumpingRightAnimation = new LinkedAnimationWithCallBack(0, 2, 0, 12, idleRightAnimation, playerJumpCallBack);
    }

    public MovingState getMovingState() {
        return movingState;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setIdle() {
        setAutoCycle(300);

        switch (movingState) {
            case MOVING_RIGHT, IDLE_RIGHT, JUMPING_RIGHT -> idleRight();
            case MOVING_LEFT, IDLE_LEFT, JUMPING_LEFT   -> idleLeft();
        }
    }

    public void idleLeft() {
        if (movingState == MovingState.IDLE_LEFT) return;
        playAnimation(idleLeftAnimation);
        movingState = MovingState.IDLE_LEFT;
    }

    public void idleRight() {
        if (movingState == MovingState.IDLE_RIGHT) return;
        playAnimation(idleRightAnimation);
        movingState = MovingState.IDLE_RIGHT;
    }

    public void moveRight() {
        if (movingState == MovingState.MOVING_RIGHT) return;
        setAutoCycle(100);
        playAnimation(movingRightAnimation);
        movingState = MovingState.MOVING_RIGHT;
    }

    public void moveLeft() {
        if (movingState == MovingState.MOVING_LEFT) return;
        setAutoCycle(100);
        playAnimation(movingLeftAnimation);
        movingState = MovingState.MOVING_LEFT;
    }

    public void isNoLongerJumping() {
        isJumping = false;
    }

    public void jump(Direction fromSurface) {
        if (isJumping) {
            return;
        }
        isJumping = true;
        setAutoCycle(100);
        switch (fromSurface) {
            case LEFT -> {
                movingState = MovingState.JUMPING_RIGHT;
                playAnimation(jumpingRightAnimation);
            }
            case RIGHT -> {
                movingState = MovingState.JUMPING_LEFT;
                playAnimation(jumpingLeftAnimation);
            }
            default -> {
                switch (movingState) {
                    case MOVING_RIGHT, IDLE_RIGHT -> {
                        movingState = MovingState.JUMPING_RIGHT;
                        playAnimation(jumpingRightAnimation);
                    }
                    case MOVING_LEFT, IDLE_LEFT -> {
                        movingState = MovingState.JUMPING_LEFT;
                        playAnimation(jumpingLeftAnimation);
                    }
                    default -> {}
                }
            }
        }
    }
}
