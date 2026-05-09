package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;
import nl.han.jefmk.entities.player.Player;

public class GreenShroom extends Pickup {

    public GreenShroom(Coordinate2D initialLocation) {
        super("sprites/green-shroom.png", initialLocation, 20, ColliderPreset.BOTTOM_CENTER);
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.regainHealth();
    }
}
