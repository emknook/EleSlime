package nl.han.jefmk.entities.projectiles;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.RectangleEntity;
import javafx.scene.paint.Color;
import nl.han.jefmk.EleSlime;

public class LightningCollider extends RectangleEntity implements Collider {

    protected LightningCollider(Coordinate2D initialLocation, Direction direction) {
        super(initialLocation);
        this.setFill(EleSlime.DEBUG ? Color.RED : Color.TRANSPARENT);
        setHitBoxSize(direction);
    }

    private void setHitBoxSize(Direction direction) {
        switch (direction) {
            case LEFT, RIGHT:
                this.setWidth(EleSlime.TILE_SIZE);
                this.setHeight(20d);
            case UP, DOWN:
                this.setHeight(EleSlime.TILE_SIZE);
                this.setWidth(20d);
        }
    }
}
