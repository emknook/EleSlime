package nl.han.jefmk.scenes;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.scenes.ScrollableDynamicScene;
import javafx.scene.paint.Color;
import nl.han.jefmk.entities.player.Player;
import nl.han.jefmk.surfaces.Tile;

public class GameScene extends ScrollableDynamicScene {
    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(63,39,81));
    }

    @Override
    public void setupEntities() {
        addEntity(new Player(new Coordinate2D(getViewportWidth() / 2 + 100, getViewportHeight() / 2)));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2,getViewportHeight() / 2 + 100)));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2,getViewportHeight() / 2 - 300)));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 - 200,getViewportHeight() / 2 - 100)));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 + 200,getViewportHeight() / 2 - 100)));
    }
}
