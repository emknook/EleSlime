package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.DynamicCompositeEntity;
import com.github.hanyaeger.api.entities.Newtonian;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class Player extends DynamicCompositeEntity implements KeyListener, Newtonian, Collider {

    private static final double AIR_MOVEMENT_SPEED = 3d;
    private static final double SURFACE_MOVEMENT_SPEED = 4d;
    private static final double JUMP_SPEED = 10d;
    private static final double GRAVITY = 0.8d;

    private final Set<Direction> touchingSurfaceDirections = new HashSet<>();
    private final Set<KeyCode> currentPressedKeys = new HashSet<>();

    private Direction attachedSurfaceDirection = null;

    private double horizontalSpeed = 0d;
    private double verticalSpeed = 0d;

    public Player(final Coordinate2D initialLocation) {
        super(initialLocation);
        setGravityConstant(GRAVITY);
    }

    @Override
    protected void setupEntities() {
        double bodyRadius = 20d;
        Size spriteSize = new Size(bodyRadius * 2);

        addEntity(new PlayerCollider(this, bodyRadius, new Coordinate2D(0, 0)));
        addEntity(new PlayerSprite(spriteSize, new Coordinate2D(0, 0)));
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        int horizontalDirection = 0;
        int verticalDirection = 0;

        if (pressedKeys.contains(KeyCode.LEFT)) {
            horizontalDirection -= 1;
        }
        if (pressedKeys.contains(KeyCode.RIGHT)) {
            horizontalDirection += 1;
        }
        if (pressedKeys.contains(KeyCode.UP)) {
            verticalDirection -= 1;
        }
        if (pressedKeys.contains(KeyCode.DOWN)) {
            verticalDirection += 1;
        }

        // No movement
        if (horizontalDirection == 0 && verticalDirection == 0) {
            setSpeed(0);
            return;
        }

        double angleInDegrees = Math.toDegrees(Math.atan2(horizontalDirection, -verticalDirection));
        setMotion(3, angleInDegrees);
    }

    private void applyInputMovement() {
        if (currentPressedKeys.contains(KeyCode.SPACE) && attachedSurfaceDirection != null) {
            jumpAwayFromSurface();
            return;
        }

        switch (attachedSurfaceDirection) {
            case null -> handleAirMovement(currentPressedKeys);
            case DOWN, UP -> handleHorizontalSurfaceMovement(currentPressedKeys);
            case LEFT, RIGHT -> handleVerticalSurfaceMovement(currentPressedKeys);
            default -> {}
        }
    }

    private void handleAirMovement(final Set<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.LEFT)) {
            horizontalSpeed = -AIR_MOVEMENT_SPEED;
        } else if (pressedKeys.contains(KeyCode.RIGHT)) {
            horizontalSpeed = AIR_MOVEMENT_SPEED;
        } else {
            horizontalSpeed = 0;
        }
    }

    private void handleHorizontalSurfaceMovement(final Set<KeyCode> pressedKeys) {
        horizontalSpeed = 0;

        if (pressedKeys.contains(KeyCode.LEFT)) {
            horizontalSpeed = -SURFACE_MOVEMENT_SPEED;
        } else if (pressedKeys.contains(KeyCode.RIGHT)) {
            horizontalSpeed = SURFACE_MOVEMENT_SPEED;
        }

        verticalSpeed = 0;
    }

    private void handleVerticalSurfaceMovement(final Set<KeyCode> pressedKeys) {
        verticalSpeed = 0;

        if (pressedKeys.contains(KeyCode.UP)) {
            verticalSpeed = -SURFACE_MOVEMENT_SPEED;
        } else if (pressedKeys.contains(KeyCode.DOWN)) {
            verticalSpeed = SURFACE_MOVEMENT_SPEED;
        }

        horizontalSpeed = 0;
    }

    public void jumpAwayFromSurface() {
        switch (attachedSurfaceDirection) {
            case DOWN -> verticalSpeed = -JUMP_SPEED;
            case UP -> verticalSpeed = JUMP_SPEED;
            case LEFT -> horizontalSpeed = JUMP_SPEED;
            case RIGHT -> horizontalSpeed = -JUMP_SPEED;
            default -> {}
        }

        attachedSurfaceDirection = null;
    }

    public void addTouchingSurfaceDirection(Direction direction) {
        touchingSurfaceDirections.add(direction);
    }

    public void clearTouchingSurfaceDirections() {
        touchingSurfaceDirections.clear();
    }

    public void updateAttachedSurface() {
        if (touchingSurfaceDirections.contains(Direction.DOWN)) {
            attachedSurfaceDirection = Direction.DOWN;
        } else if (touchingSurfaceDirections.contains(Direction.LEFT)) {
            attachedSurfaceDirection = Direction.LEFT;
        } else if (touchingSurfaceDirections.contains(Direction.RIGHT)) {
            attachedSurfaceDirection = Direction.RIGHT;
        } else if (touchingSurfaceDirections.contains(Direction.UP)) {
            attachedSurfaceDirection = Direction.UP;
        } else {
            attachedSurfaceDirection = null;
        }
    }

    public void setHorizontalSpeed(double horizontalSpeed) {
        this.horizontalSpeed = horizontalSpeed;
    }

    public void setVerticalSpeed(double verticalSpeed) {
        this.verticalSpeed = verticalSpeed;
    }
}