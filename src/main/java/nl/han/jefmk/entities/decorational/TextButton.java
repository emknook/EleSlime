package nl.han.jefmk.entities.decorational;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import com.github.hanyaeger.api.userinput.MouseEnterListener;
import com.github.hanyaeger.api.userinput.MouseExitListener;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextButton extends TextEntity implements MouseButtonPressedListener, MouseEnterListener, MouseExitListener {

    private static final Color NORMAL_COLOR = Color.AQUAMARINE;
    private static final Color HOVER_COLOR = Color.GOLD;

    private final Runnable onClick;

    public TextButton(Coordinate2D position, String text, Runnable onClick) {
        super(position, text);
        this.onClick = onClick;
        setFont(Font.font("Roboto", FontWeight.BOLD, 40));
        setFill(NORMAL_COLOR);
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        if (button == MouseButton.PRIMARY) {
            onClick.run();
        }
    }

    @Override
    public void onMouseEntered() {
        setFill(HOVER_COLOR);
    }

    @Override
    public void onMouseExited() {
        setFill(NORMAL_COLOR);
    }
}
