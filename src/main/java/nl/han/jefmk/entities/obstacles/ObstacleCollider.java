package nl.han.jefmk.entities.obstacles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;

import java.util.List;

public class ObstacleCollider extends RectangleEntity implements Collider, Collided {

    private final Obstacle obstacle;

    public ObstacleCollider(Obstacle obstacle, Size size, Coordinate2D location) {
        super(location);
        this.obstacle = obstacle;
        setWidth(size.width());
        setHeight(size.height());
        setFill(EleSlime.DEBUG ? Color.rgb(255, 100, 0, 0.5) : Color.TRANSPARENT);
        setStrokeWidth(0);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        for (Collider collider : collidingObjects) {
            if (collider instanceof Player player) {
                obstacle.onPlayerCollision(player);
                return;
            }
        }
    }
}
