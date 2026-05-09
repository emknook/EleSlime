package nl.han.jefmk.entities.obstacles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;

public class Stalagmite extends Obstacle {
    public Stalagmite(Coordinate2D initialLocation) {
        super("sprites/stalagmite.png", initialLocation,
                new Size(EleSlime.TILE_SIZE, EleSlime.TILE_SIZE * 0.2),
                new Coordinate2D(0, EleSlime.TILE_SIZE * 0.8));
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.takeDamage();
        player.takeKnockback(this);
    }
}
