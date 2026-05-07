package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.DynamicCompositeEntity;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import nl.han.jefmk.EleSlime;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Player extends DynamicCompositeEntity implements KeyListener, Collider {

    private static final double AIR_MOVEMENT_SPEED = 400d;    // px/s
    private static final double SURFACE_MOVEMENT_SPEED = 400d; // px/s
    private static final double JUMP_SPEED = 850d;             // px/s
    private static final double WALL_JUMP_HORIZONTAL_SPEED = 750d; // px/s
    private static final double WALL_JUMP_VERTICAL_SPEED = 300d; //px/s
    private static final double GRAVITY = 2880d;               // px/s²
    private static final double MAX_DELTA = 1.0 / 20.0;       // clamp to 20 fps minimum

    private long lastTimestamp = -1;

    private final Set<Direction> touchingSurfaceDirections = new HashSet<>();
    private final Set<KeyCode> currentPressedKeys = new HashSet<>();

    private Direction attachedSurfaceDirection = null;

    private double horizontalSpeed = 0d;
    private double verticalSpeed = 0d;

    private int health;
    private int score;

    private Consumer<Coordinate2D> positionListener;
    private Consumer<String> debugListener;
    // Populated each frame by PlayerCollider; cleared at end of update
    private final Set<String> collidingTileDescriptions = new LinkedHashSet<>();

    private PlayerSprite playerSprite;
    private final Coordinate2D spawn;

    public Player(final Coordinate2D initialLocation) {
        super(initialLocation);
        this.spawn = initialLocation;
        health = 3;
        score = 0;
    }

    public void setPositionListener(Consumer<Coordinate2D> listener) {
        this.positionListener = listener;
    }

    public void setDebugListener(Consumer<String> listener) {
        this.debugListener = listener;
    }

    public void addCollidingTile(String description) {
        collidingTileDescriptions.add(description);
    }

    @Override
    protected void setupEntities() {
        double bodyRadius = EleSlime.MOB_SIZE / 2;
        Size spriteSize = new Size(bodyRadius * 2);
        double stickyRadius = bodyRadius * 1.04;
        double stickyOffset = stickyRadius - bodyRadius;

        addEntity(new PlayerStickyCollider(this, stickyRadius, new Coordinate2D(0 - stickyOffset, 0 - stickyOffset)));
        addEntity(new PlayerCollider(this, bodyRadius, new Coordinate2D(0, 0)));
        playerSprite = new PlayerSprite(spriteSize, new Coordinate2D(0, 0));
        addEntity(playerSprite);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        currentPressedKeys.clear();
        currentPressedKeys.addAll(pressedKeys);
    }

    private void handleAirMovement(final Set<KeyCode> pressedKeys) {
        if (pressedKeys.contains(KeyCode.LEFT)) {
            horizontalSpeed = Math.max(horizontalSpeed - AIR_MOVEMENT_SPEED * 0.15, -AIR_MOVEMENT_SPEED);
        } else if (pressedKeys.contains(KeyCode.RIGHT)) {
            horizontalSpeed = Math.min(horizontalSpeed + AIR_MOVEMENT_SPEED * 0.15, AIR_MOVEMENT_SPEED);
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
        if (attachedSurfaceDirection == null) {
            return;
        }

        switch (attachedSurfaceDirection) {
            case DOWN -> verticalSpeed = -JUMP_SPEED;
            case UP -> verticalSpeed = JUMP_SPEED;

            case LEFT -> {
                horizontalSpeed = WALL_JUMP_HORIZONTAL_SPEED;
                verticalSpeed = -WALL_JUMP_VERTICAL_SPEED;
            }
            case RIGHT -> {
                horizontalSpeed = -WALL_JUMP_HORIZONTAL_SPEED;
                verticalSpeed = -WALL_JUMP_VERTICAL_SPEED;
            }
            default -> {
            }
        }

        playerSprite.jump();
        attachedSurfaceDirection = null;
    }

    public void addTouchingSurfaceDirection(Direction direction) {
        touchingSurfaceDirections.add(direction);
    }

    public void takeDamage() {
        health--;
        this.setAnchorLocation(new Coordinate2D(spawn.getX(), spawn.getY() - this.getHeight()));
    }

    public void regainHealth() {
        health++;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public int getHealth() {
        return health;
    }

    public void updateAttachedSurface() {
        if (touchingSurfaceDirections.contains(Direction.LEFT) && (currentPressedKeys.contains(KeyCode.LEFT) || attachedSurfaceDirection == Direction.LEFT) && !currentPressedKeys.contains(KeyCode.RIGHT)) {
            attachedSurfaceDirection = Direction.LEFT;
        } else if (touchingSurfaceDirections.contains(Direction.RIGHT) && (currentPressedKeys.contains(KeyCode.RIGHT) || attachedSurfaceDirection == Direction.RIGHT) && !currentPressedKeys.contains(KeyCode.LEFT)) {
            attachedSurfaceDirection = Direction.RIGHT;
        } else if (touchingSurfaceDirections.contains(Direction.UP) && (currentPressedKeys.contains(KeyCode.UP) || attachedSurfaceDirection == Direction.UP) && !currentPressedKeys.contains(KeyCode.DOWN)) {
            attachedSurfaceDirection = Direction.UP;
        } else if (touchingSurfaceDirections.contains(Direction.DOWN)) {
            attachedSurfaceDirection = Direction.DOWN;
        } else {
            attachedSurfaceDirection = null;
        }
    }

    public void updateRotationBasedOnAttachedSurface() {
        switch (attachedSurfaceDirection) {
            case null -> {}
            case UP -> playerSprite.setRotate(180);
            case LEFT -> playerSprite.setRotate(-90);
            case RIGHT -> playerSprite.setRotate(90);
            default -> playerSprite.setRotate(0);
        }
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
            default -> {
            }
        }
    }

    private void applyGravity(double dt) {
        if (attachedSurfaceDirection == null) {
            verticalSpeed += GRAVITY * dt;
        }
    }

    @Override
    public void update(long timestamp) {
        if (lastTimestamp < 0) {
            lastTimestamp = timestamp;
            return;
        }
        double dt = Math.min((timestamp - lastTimestamp) / 1_000_000_000.0, MAX_DELTA);
        lastTimestamp = timestamp;

        updateAttachedSurface();
        updateRotationBasedOnAttachedSurface();
        applyInputMovement();
        applyGravity(dt);

        setAnchorLocation(new Coordinate2D(
                getAnchorLocation().getX() + horizontalSpeed * dt,
                getAnchorLocation().getY() + verticalSpeed * dt
        ));

        if (positionListener != null) {
            positionListener.accept(getAnchorLocation());
        }

        determineSpriteAnimation();

        if (debugListener != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("attached : ").append(attachedSurfaceDirection).append("\n");
            sb.append("touching : ").append(touchingSurfaceDirections).append("\n");
            if (!collidingTileDescriptions.isEmpty()) {
                sb.append("collisions:\n");
                collidingTileDescriptions.forEach(d -> sb.append("  ").append(d).append("\n"));
            }
            debugListener.accept(sb.toString());
        }

        clearTouchingSurfaceDirections();
    }

    private void determineSpriteAnimation() {
        if (horizontalSpeed == 0 && verticalSpeed == 0) {
            playerSprite.setIdle();
        } else {
            if (attachedSurfaceDirection == null) {
                return;
            }
            if (horizontalSpeed > 0) {
                switch (attachedSurfaceDirection) {
                    case Direction.DOWN -> playerSprite.moveRight();
                    case Direction.UP -> playerSprite.moveLeft();
                }
            } else if (horizontalSpeed < 0) {
                switch (attachedSurfaceDirection) {
                    case Direction.DOWN -> playerSprite.moveLeft();
                    case Direction.UP -> playerSprite.moveRight();
                }
            }
            if (verticalSpeed > 0) {
                switch (attachedSurfaceDirection) {
                    case LEFT -> playerSprite.moveRight();
                    case RIGHT -> playerSprite.moveLeft();
                }
            } else {
                switch (attachedSurfaceDirection) {
                    case LEFT -> playerSprite.moveLeft();
                    case RIGHT -> playerSprite.moveRight();
                }
            }
        }
    }

    public void clearTouchingSurfaceDirections() {
        touchingSurfaceDirections.clear();
        collidingTileDescriptions.clear();
    }
}