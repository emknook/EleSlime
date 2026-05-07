package nl.han.jefmk.entities.decorational;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.DynamicTextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.jefmk.entities.HasHealth;

public class HealthDisplay extends DynamicTextEntity {

    public HealthDisplay(Coordinate2D initialLocation, HasHealth entity) {
        super(initialLocation);
        setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
        setFill(Color.RED);
        setAnchorPoint(AnchorPoint.TOP_RIGHT);
        entity.addHealthListener(this::updateDisplay);
        updateDisplay(entity.getHealth());
    }

    private void updateDisplay(int health) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < health; i++) {
            sb.append("<3 ");
        }
        setText(sb.toString().stripTrailing());
    }
}
