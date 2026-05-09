package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.DynamicCompositeEntity;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.HasHealth;
import nl.han.jefmk.entities.Health;
import nl.han.jefmk.entities.obstacles.Obstacle;
import nl.han.jefmk.scenes.GameScene;
import nl.han.jefmk.score.Score;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Player extends DynamicCompositeEntity implements KeyListener, Collider, HasHealth {

    private static final double AIR_MOVEMENT_SPEED = 400d;    // px/s
    private static final double SURFACE_MOVEMENT_SPEED = 400d; // px/s
    private static final double JUMP_SPEED = 850d;             // px/s
    private static final double WALL_JUMP_HORIZONTAL_SPEED = 750d; // px/s
    private static final double WALL_JUMP_VERTICAL_SPEED = 300d; //px/s
    private static final double GRAVITY = 2880d;               // px/s²
    private static final double MAX_DELTA = 1.0 / 20.0;       // clamp to 20 fps minimum
    private static final double STICKY_RADIUS_MULTIPLIER = 1.04d;
    private static final double SPRITE_COLLIDER_BOTTOM_OFFSET = 7d;

    private long lastTimestamp = -1;

    private final Set<Direction> touchingSurfaceDirections = new HashSet<>();
    private final Set<KeyCode> currentPressedKeys = new HashSet<>();

    private Direction attachedSurfaceDirection = null;

    private double horizontalSpeed = 0d;
    private double verticalSpeed = 0d;
    private double knockbackTime = 0d;  // seconds
    private double shootingTime = 0d;

    private final Health health;

    private Consumer<Coordinate2D> positionListener;
    private Consumer<String> debugListener;
    // Populated each frame by PlayerCollider; cleared at end of update
    private final Set<String> collidingTileDescriptions = new LinkedHashSet<>();

    private PlayerSprite playerSprite;
    private final Coordinate2D spawn;
    private final GameScene level;

    public Player(final Coordinate2D initialLocation, int initialHealth, GameScene level) {
        super(initialLocation);
        this.spawn = initialLocation;
        this.health = new Health(initialHealth);
        this.level = level;
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
        Size spriteSize = createPlayerSpriteSize(bodyRadius);
        double stickyRadius = bodyRadius * STICKY_RADIUS_MULTIPLIER;
        double stickyOffset = stickyRadius - bodyRadius;

        addEntity(new PlayerStickyCollider(this, stickyRadius, new Coordinate2D(0 - stickyOffset, 0 - stickyOffset)));
        addEntity(new PlayerCollider(this, bodyRadius, new Coordinate2D(0, 0)));
        playerSprite = new PlayerSprite(spriteSize, new Coordinate2D(0, calculateSpriteOffsetY(bodyRadius, spriteSize)));
        addEntity(playerSprite);
    }

    private Size createPlayerSpriteSize(double bodyRadius) {
        return new Size(bodyRadius * 2);
    }

    private double calculateSpriteOffsetY(double bodyRadius, Size spriteSize) {
        double colliderDiameter = bodyRadius * 2;
        return colliderDiameter - spriteSize.height() + SPRITE_COLLIDER_BOTTOM_OFFSET;
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

        playerSprite.jump(attachedSurfaceDirection);
        attachedSurfaceDirection = null;
    }

    public void addTouchingSurfaceDirection(Direction direction) {
        touchingSurfaceDirections.add(direction);
    }

    public boolean isFalling() {
        return verticalSpeed > 0;
    }

    public void endKnockback() {
        knockbackTime = 0;
        horizontalSpeed = 0;
    }

    @Override
    public void takeDamage() {
        if (health.damage()) {
            checkForDeath();
        }
    }

    private void checkForDeath() {
        if (health.get() <= 0) {
            Score.getInstance().resetForDeath();
            this.setAnchorLocation(new Coordinate2D(spawn.getX(), spawn.getY() - this.getHeight()));

            verticalSpeed = 0;
            horizontalSpeed = 0;
        }
    }

    @Override
    public void regainHealth() {
        health.regain();
    }

    @Override
    public int getHealth() {
        return health.get();
    }

    @Override
    public void addHealthListener(Consumer<Integer> listener) {
        health.addListener(listener);
    }

    public void updateAttachedSurface() {
        // Block surface attachment while in knockback state
        if (knockbackTime > 0) {
            attachedSurfaceDirection = null;
            return;
        }
        
        Direction previousAttached = attachedSurfaceDirection;
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
        if (previousAttached == null && attachedSurfaceDirection != null) {
            playerSprite.isNoLongerJumping();
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
        if (knockbackTime > 0) return;

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

        knockbackTime -= dt;
        shootingTime -= dt;

        if (knockbackTime > 0) {
            // Gradually reduce horizontal velocity during knockback
            horizontalSpeed *= Math.pow(0.02, dt);
        }
        if (shootingTime < 0) {
            checkShootingPressed();
        }

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

    private void checkShootingPressed() {
        if (currentPressedKeys.contains(KeyCode.Z)) {
            spawnLightningBolt();
        }
    }

    private void spawnLightningBolt() {
        shootingTime = 1.0d; //can shoot once per second
        level.createLightningBolt(new Coordinate2D(getAnchorLocation().getX() + getWidth(), getAnchorLocation().getY() + getHeight() / 2), Direction.RIGHT);
    }

    private void determineSpriteAnimation() {
        if (playerSprite.isJumping()) return;

        if (horizontalSpeed == 0 && verticalSpeed == 0) {
            playerSprite.setIdle();
            return;
        }

        if (attachedSurfaceDirection == null) return;

        switch (attachedSurfaceDirection) {
            case DOWN -> {
                if (horizontalSpeed > 0) playerSprite.moveRight();
                else if (horizontalSpeed < 0) playerSprite.moveLeft();
            }
            case UP -> {
                if (horizontalSpeed > 0) playerSprite.moveLeft();
                else if (horizontalSpeed < 0) playerSprite.moveRight();
            }
            case LEFT -> {
                if (verticalSpeed > 0) playerSprite.moveRight();
                else if (verticalSpeed < 0) playerSprite.moveLeft();
            }
            case RIGHT -> {
                if (verticalSpeed > 0) playerSprite.moveLeft();
                else if (verticalSpeed < 0) playerSprite.moveRight();
            }
        }
    }

    public void clearTouchingSurfaceDirections() {
        touchingSurfaceDirections.clear();
        collidingTileDescriptions.clear();
    }

    public void takeKnockback(Obstacle obstacle) {
        // Calculate horizontal knockback direction (away from obstacle)
        double obstacleX = obstacle.getAnchorLocation().getX();
        double playerX = this.getAnchorLocation().getX();
        
        if (obstacleX < playerX) {
            // Obstacle is to the left, knock player right
            horizontalSpeed = AIR_MOVEMENT_SPEED * 1.5;
        } else {
            // Obstacle is to the right, knock player left
            horizontalSpeed = -AIR_MOVEMENT_SPEED * 1.5;
        }
        
        // Vertical knockback: throw upward at jump speed
        verticalSpeed = -JUMP_SPEED;
        
        // Clear attached surface so player enters air state and gravity applies
        attachedSurfaceDirection = null;
        knockbackTime = 1.5;  // Knockback duration in seconds
        touchingSurfaceDirections.clear();
    }
}