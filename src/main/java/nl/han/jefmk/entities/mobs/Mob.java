package nl.han.jefmk.entities.mobs;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.Newtonian;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import nl.han.jefmk.entities.HasHealth;
import nl.han.jefmk.entities.Health;
import nl.han.jefmk.surfaces.SurfaceCollider;
import nl.han.jefmk.surfaces.Tile;

import java.util.List;
import java.util.function.Consumer;

public class Mob extends DynamicSpriteEntity implements Newtonian, Collider, Collided, HasHealth {
    private final Health health;

    protected Mob(String resource, Coordinate2D initialLocation, Size size, int rows, int columns, int initialHealth) {
        super(resource, initialLocation, size, rows, columns);
        this.health = new Health(initialHealth);
    }

    @Override
    public int getHealth() {
        return health.get();
    }

    @Override
    public void takeDamage() {
        health.damage();
    }

    @Override
    public void regainHealth() {
        health.regain();
    }

    @Override
    public void addHealthListener(Consumer<Integer> listener) {
        health.addListener(listener);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            if (collider instanceof SurfaceCollider) {
                handleSurfaceCollision((SurfaceCollider) collider);
            }
        }
    }

    private void handleSurfaceCollision(SurfaceCollider surface) {
        Tile tile = surface.getTile();
        switch(surface.getSurfaceDirection()) {
            case Direction.UP:
                this.setAnchorLocationY(tile.getAnchorLocation().getY() + tile.getHeight());
                break;
            case Direction.DOWN:
                this.setAnchorLocationY(tile.getAnchorLocation().getY() - this.getHeight());
                break;
            case Direction.LEFT:
                this.setAnchorLocationX(tile.getAnchorLocation().getX() + tile.getWidth());
                break;
            case Direction.RIGHT:
                this.setAnchorLocationX(tile.getAnchorLocation().getX() - this.getWidth());
                break;
        }
    }
}
