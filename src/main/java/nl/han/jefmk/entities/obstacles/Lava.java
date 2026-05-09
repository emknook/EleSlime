package nl.han.jefmk.entities.obstacles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;

public class Lava extends Obstacle {

    public Lava(Coordinate2D initialLocation) {
        super("sprites/lava-still.png", initialLocation,
                new Size(EleSlime.TILE_SIZE, EleSlime.TILE_SIZE),
                new Coordinate2D(0, 0),
                20, 1
                );
        setAutoCycle(80);
        setCurrentFrameIndex(0);
    }

    @Override
    public void onPlayerCollision(Player player) {
        player.takeDamage();
        player.takeKnockback(this);
    }
}
