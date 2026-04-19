package nl.han.jefmk.scenes;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.scenes.ScrollableDynamicScene;
import javafx.scene.paint.Color;
import nl.han.jefmk.entities.player.Player;
import nl.han.jefmk.surfaces.Tile;
import nl.han.jefmk.surfaces.TileType;

public class GameScene extends ScrollableDynamicScene {
    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(63,39,81));
    }

    @Override
    public void setupEntities() {
        addEntity(new Player(new Coordinate2D(getViewportWidth() / 2 + 100, getViewportHeight() / 2)));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2,getViewportHeight() / 2 + 100), TileType.FLOOR));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2,getViewportHeight() / 2 - 300), TileType.CEILING));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 - 200,getViewportHeight() / 2 - 100), TileType.WALL_LEFT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 + 200,getViewportHeight() / 2 - 100), TileType.WALL_RIGHT));
    }
}
