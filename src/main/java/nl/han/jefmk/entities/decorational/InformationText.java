package nl.han.jefmk.entities.decorational;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class InformationText extends TextEntity {
    public InformationText(Coordinate2D initialLocation, String text) {
        super(initialLocation, text);
        setFont(Font.font("Roboto", FontWeight.NORMAL, 30));
        this.setFill(Color.AQUAMARINE);
        this.setAnchorPoint(AnchorPoint.CENTER_CENTER);
    }
}
