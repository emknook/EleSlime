package nl.han.jefmk.entities.player;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.CircleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.mobs.EnemySlime;
import nl.han.jefmk.entities.mobs.Slime;
import nl.han.jefmk.entities.obstacles.Obstacle;
import nl.han.jefmk.entities.pickups.BlueShroom;
import nl.han.jefmk.entities.pickups.GreenShroom;
import nl.han.jefmk.entities.pickups.Pickup;
import nl.han.jefmk.score.Score;
import nl.han.jefmk.surfaces.SurfaceCollider;
import nl.han.jefmk.surfaces.Tile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerCollider extends CircleEntity implements Collided {
    private final Player player;

    protected PlayerCollider(Player player, double radius, Coordinate2D initialLocation) {
        super(initialLocation);
        this.player = player;
        setRadius(radius);

        setFill(EleSlime.DEBUG ? Color.BLUE : Color.TRANSPARENT);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        // Resolve vertical surfaces first; track which tiles were vertically resolved so
        // horizontal surfaces on the same tile (corner tiles) don't incorrectly snap the player sideways.
        Set<Tile> verticallyResolved = new HashSet<>();

        for (Collider collider : collidingObjects) {
            if (collider instanceof SurfaceCollider surface) {
                Direction dir = surface.getSurfaceDirection();
                if (dir == Direction.UP || dir == Direction.DOWN) {
                    handleSurfaceCollision(surface);
                    verticallyResolved.add(surface.getTile());
                }
            } else if (collider instanceof Obstacle) {
                handleObstacleCollision();
            } else if (collider instanceof Pickup pickup) {
                handlePickupCollision(pickup);
            }
        }

        for (Collider collider : collidingObjects) {
            if (collider instanceof SurfaceCollider surface) {
                Direction dir = surface.getSurfaceDirection();
                if ((dir == Direction.LEFT || dir == Direction.RIGHT)
                        && !verticallyResolved.contains(surface.getTile())) {
                    handleSurfaceCollision(surface);
                }
            }
            if (collider instanceof Slime) {
                handleSlimeCollision((Slime) collider);
            }
        }
    }

    private void handleSurfaceCollision(SurfaceCollider surface) {
        Tile tile = surface.getTile();
        if (EleSlime.DEBUG) {
            player.addCollidingTile(tile.getType().name() + " ← " + surface.getSurfaceDirection().name());
        }
        switch(surface.getSurfaceDirection()) {
            case Direction.UP:
                player.setAnchorLocationY(tile.getAnchorLocation().getY() + tile.getHeight());
                break;
            case Direction.DOWN:
                player.setAnchorLocationY(tile.getAnchorLocation().getY() - player.getHeight());
                break;
            case Direction.LEFT:
                player.setAnchorLocationX(tile.getAnchorLocation().getX() + tile.getWidth());
                break;
            case Direction.RIGHT:
                player.setAnchorLocationX(tile.getAnchorLocation().getX() - player.getWidth());
                break;
        }
    }

    private void handleSlimeCollision(Slime slime) {
        if (slime instanceof EnemySlime) {
            player.takeDamage();
        }
    }

    private void handleObstacleCollision() {
        player.takeDamage();
    }

    private void handlePickupCollision(Pickup pickup) {
        if (pickup instanceof GreenShroom) {
            player.regainHealth();
        } else if (pickup instanceof BlueShroom) {
            Score.getInstance().addScore(100);
        }
    }

}