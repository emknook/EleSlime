package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;
import nl.han.jefmk.score.Score;

import java.util.List;

public class WinFlagCollider extends RectangleEntity implements Collider, Collided {

    private final double poleTopY;
    private final double poleHeight;
    private final Runnable onWin;
    private boolean triggered = false;

    public WinFlagCollider(Coordinate2D location, double width, double height, Runnable onWin) {
        super(location);
        this.poleTopY = location.getY();
        this.poleHeight = height;
        this.onWin = onWin;
        setWidth(width);
        setHeight(height);
        setFill(EleSlime.DEBUG ? Color.rgb(0, 100, 255, 0.4) : Color.TRANSPARENT);
        setStrokeWidth(0);
    }

    @Override
    public void onCollision(List<Collider> collidingObjects) {
        if (triggered) return;
        for (Collider collider : collidingObjects) {
            if (collider instanceof Player player) {
                triggered = true;
                double playerY = player.getAnchorLocation().getY();
                double ratio = (playerY - poleTopY) / poleHeight;
                int bonus = (int) Math.round(Math.clamp(1.0 - ratio, 0.0, 1.0) * 1000);
                Score.getInstance().addScore(bonus);
                this.remove();
                onWin.run();
                return;
            }
        }
    }
}
