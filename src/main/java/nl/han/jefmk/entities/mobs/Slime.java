package nl.han.jefmk.entities.mobs;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;

public class Slime extends Mob {
    protected Slime(String resource, Coordinate2D initialLocation, int rows, int columns) {
        double bodyRadius = 40d;
        Size size = new Size(bodyRadius * 2);
        super(resource, initialLocation, size, rows, columns);
    }
}
