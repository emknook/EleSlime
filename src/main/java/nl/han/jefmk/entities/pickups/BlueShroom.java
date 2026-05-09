package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;
import nl.han.jefmk.entities.player.Player;
import nl.han.jefmk.score.Score;

public class BlueShroom extends Pickup {

    public BlueShroom(Coordinate2D initialLocation) {
        super("sprites/blue-shroom.png", initialLocation, 20, ColliderPreset.BOTTOM_CENTER);
    }

    @Override
    public void onPlayerCollision(Player player) {
        Score.getInstance().addScore(100);
    }
}
