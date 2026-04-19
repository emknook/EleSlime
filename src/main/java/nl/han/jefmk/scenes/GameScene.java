package nl.han.jefmk.scenes;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.scenes.ScrollableDynamicScene;
import javafx.scene.paint.Color;
import nl.han.jefmk.entities.Player;

public class GameScene extends ScrollableDynamicScene {
    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(63,39,81));
    }

    @Override
    public void setupEntities() {
        Coordinate2D startingPosition = new Coordinate2D(0,0);
        Player player = new Player(startingPosition);

        addEntity(player);
    }
}
