package nl.han.jefmk.scenes;

import com.github.hanyaeger.api.scenes.ScrollableDynamicScene;
import javafx.scene.paint.Color;

public class GameScene extends ScrollableDynamicScene {
    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(39, 39, 68));
    }

    @Override
    public void setupEntities() {
    }
}
