package nl.han.jefmk.entities.mobs;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Newtonian;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import nl.han.jefmk.surfaces.SurfaceCollider;
import nl.han.jefmk.surfaces.Tile;

import java.util.List;

public class Mob extends DynamicSpriteEntity implements Newtonian, Collider, Collided {
    protected Mob(String resource, Coordinate2D initialLocation, Size size, int rows, int columns) {
        super(resource, initialLocation, size, rows, columns);
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
        switch (surface.getSurfaceDirection()) {
            case DOWN:
                this.setAnchorLocationY(tile.getAnchorLocation().getY() - this.getHeight());
                break;
            case UP:
                this.setAnchorLocationY(tile.getAnchorLocation().getY() + tile.getHeight());
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
            default:
                break;
        }
    }
}
