package nl.han.jefmk.entities.obstacles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.DynamicCompositeEntity;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.surfaces.SurfaceCollider;
import nl.han.jefmk.surfaces.SurfaceOwner;

public class MovingPlatform extends DynamicCompositeEntity implements SurfaceOwner {

    private static final double PLATFORM_WIDTH = EleSlime.TILE_SIZE * 3;
    private static final double PLATFORM_HEIGHT = EleSlime.TILE_SIZE / 2;
    private static final double SURFACE_THICKNESS = 8;

    private Coordinate2D currentTarget;
    private Coordinate2D returnTarget;
    private final double speedPixelsPerSecond;

    private long lastTimestamp = -1;
    private double frameDisplacementX = 0;
    private double frameDisplacementY = 0;

    public MovingPlatform(Coordinate2D startLocation, Coordinate2D endLocation, double speedTilesPerSecond) {
        super(new Coordinate2D(startLocation.getX(), startLocation.getY() + PLATFORM_HEIGHT));
        // Offset to the bottom half of the tile the editor places it on
        this.currentTarget  = new Coordinate2D(endLocation.getX(),   endLocation.getY()   + PLATFORM_HEIGHT);
        this.returnTarget   = new Coordinate2D(startLocation.getX(), startLocation.getY() + PLATFORM_HEIGHT);
        this.speedPixelsPerSecond = speedTilesPerSecond * EleSlime.TILE_SIZE;
    }

    @Override
    protected void setupEntities() {
        Coordinate2D origin = new Coordinate2D(0, 0);
        Coordinate2D bottomOrigin = new Coordinate2D(0, PLATFORM_HEIGHT - SURFACE_THICKNESS);
        // Thin strip at the top — landing surface
        addEntity(new SurfaceCollider(origin, Direction.DOWN, this, new Size(PLATFORM_WIDTH, SURFACE_THICKNESS)));
        // Thin strip at the bottom — ceiling surface to bump/stick against from below
        addEntity(new SurfaceCollider(bottomOrigin, Direction.UP, this, new Size(PLATFORM_WIDTH, SURFACE_THICKNESS)));
        addEntity(new SpriteEntity("sprites/moving-platform.png", origin, new Size(PLATFORM_WIDTH, PLATFORM_HEIGHT)) {});
    }

    @Override
    public void update(long timestamp) {
        if (lastTimestamp < 0) {
            lastTimestamp = timestamp;
            return;
        }

        double secondsElapsed = (timestamp - lastTimestamp) / 1_000_000_000.0;
        lastTimestamp = timestamp;

        double toTargetX = currentTarget.getX() - getAnchorLocation().getX();
        double toTargetY = currentTarget.getY() - getAnchorLocation().getY();
        double distanceToTarget = Math.sqrt(toTargetX * toTargetX + toTargetY * toTargetY);
        double stepSize = speedPixelsPerSecond * secondsElapsed;

        double newX, newY;
        if (stepSize >= distanceToTarget) {
            // Reached (or passed) the target — clamp and swap targets to reverse direction
            newX = currentTarget.getX();
            newY = currentTarget.getY();
            Coordinate2D swap = currentTarget;
            currentTarget = returnTarget;
            returnTarget = swap;
        } else {
            newX = getAnchorLocation().getX() + (toTargetX / distanceToTarget) * stepSize;
            newY = getAnchorLocation().getY() + (toTargetY / distanceToTarget) * stepSize;
        }

        frameDisplacementX = newX - getAnchorLocation().getX();
        frameDisplacementY = newY - getAnchorLocation().getY();
        setAnchorLocation(new Coordinate2D(newX, newY));
    }

    @Override
    public double getWidth() {
        return PLATFORM_WIDTH;
    }

    @Override
    public double getHeight() {
        return PLATFORM_HEIGHT;
    }

    @Override
    public double getDeltaX() {
        return frameDisplacementX;
    }

    @Override
    public double getDeltaY() {
        return frameDisplacementY;
    }
}
