package nl.han.jefmk.surfaces;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;
import javafx.scene.paint.Color;

public class Tile extends RectangleEntity implements Collider {

    private TileType type;

    public Tile(Coordinate2D initialLocation, TileType type) {
        super(initialLocation);
        setFill(Color.rgb(119, 70, 155));
        setWidth(200);
        setHeight(200);
        this.type = type;
    }

    public TileType getType() {
        return type;
    }
}
