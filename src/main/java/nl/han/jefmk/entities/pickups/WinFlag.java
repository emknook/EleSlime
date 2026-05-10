package nl.han.jefmk.entities.pickups;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.player.Player;

public class WinFlag extends Pickup {

    private static final double SPRITE_WIDTH  = EleSlime.TILE_SIZE * 2;
    private static final double SPRITE_HEIGHT = EleSlime.TILE_SIZE * 8;
    // Thin strip centred on the pole for collision
    private static final double POLE_COLLIDER_WIDTH = 20;

    public WinFlag(Coordinate2D initialLocation, Runnable onWin) {
        super(
                "sprites/win-pole.png",
                initialLocation,
                new Size(SPRITE_WIDTH, SPRITE_HEIGHT),
                makeCollider(initialLocation, onWin)
        );
    }

    private static WinFlagCollider makeCollider(Coordinate2D loc, Runnable onWin) {
        double poleX = loc.getX() + SPRITE_WIDTH / 2.0 - POLE_COLLIDER_WIDTH / 2.0;
        return new WinFlagCollider(
                new Coordinate2D(poleX, loc.getY()),
                POLE_COLLIDER_WIDTH,
                SPRITE_HEIGHT,
                onWin
        );
    }

    @Override
    public void onPlayerCollision(Player player) {
        // Handled entirely by WinFlagCollider
    }
}

