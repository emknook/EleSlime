package nl.han.jefmk.entities.mobs;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;

public abstract class Slime extends Mob {
    protected Slime(String resource, Coordinate2D initialLocation, int rows, int columns, int initialHealth) {
        super(resource, initialLocation, new Size(EleSlime.TILE_SIZE), rows, columns, initialHealth);
    }

    public abstract void onPlayerCollision(Player player);
}
