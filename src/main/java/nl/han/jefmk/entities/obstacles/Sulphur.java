package nl.han.jefmk.entities.obstacles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;

public class Sulphur extends Obstacle {
    public Sulphur(Coordinate2D initialLocation) {
        super("sprites/sulphur.png", initialLocation,
                new Size(EleSlime.TILE_SIZE * 0.5, EleSlime.TILE_SIZE * 0.8),
                new Coordinate2D(EleSlime.TILE_SIZE * 0.25, EleSlime.TILE_SIZE * 0.2));
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.takeDamage();
        player.takeKnockback(this);
    }
}
