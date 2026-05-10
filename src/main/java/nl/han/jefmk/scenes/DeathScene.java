package nl.han.jefmk.scenes;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.DynamicScene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.jefmk.entities.decorational.TextButton;

public class DeathScene extends DynamicScene {

    private final Runnable onRetry;
    private final Runnable onMenu;

    public DeathScene(Runnable onRetry, Runnable onMenu) {
        this.onRetry = onRetry;
        this.onMenu = onMenu;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(50, 20, 20));
    }

    @Override
    public void setupEntities() {
        TextEntity title = new TextEntity(new Coordinate2D(683, 200), "GAME OVER");
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 72));
        title.setFill(Color.CRIMSON);
        title.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        addEntity(title);

        TextEntity subtitle = new TextEntity(new Coordinate2D(683, 280), "You died.");
        subtitle.setFont(Font.font("Roboto", FontWeight.NORMAL, 24));
        subtitle.setFill(Color.LIGHTGRAY);
        subtitle.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        addEntity(subtitle);

        addEntity(new TextButton(new Coordinate2D(683, 400), "[ RETRY ]", () ->
                javafx.application.Platform.runLater(onRetry)));

        addEntity(new TextButton(new Coordinate2D(683, 460), "[ MAIN MENU ]", () ->
                javafx.application.Platform.runLater(onMenu)));
    }
}
