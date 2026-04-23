package nl.han.jefmk.scenes;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.scenes.ScrollableDynamicScene;
import javafx.scene.paint.Color;
import nl.han.jefmk.entities.decorational.InformationText;
import nl.han.jefmk.entities.player.Player;
import nl.han.jefmk.surfaces.Tile;
import nl.han.jefmk.surfaces.TileType;

public class ExampleScene extends ScrollableDynamicScene {
    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(39, 39, 68));
    }

    @Override
    public void setupEntities() {
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2,getViewportHeight() / 2), TileType.FLOOR));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2,getViewportHeight() / 2 - 400), TileType.CEILING));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 - 200, getViewportHeight() / 2 - 200), TileType.WALL_LEFT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 + 200,getViewportHeight() / 2 - 200), TileType.WALL_RIGHT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 - 100,getViewportHeight() / 2 - 100), TileType.CORNER_TOP_RIGHT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 + 100,getViewportHeight() / 2 - 100), TileType.CORNER_TOP_LEFT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 - 100,getViewportHeight() / 2 - 300), TileType.CORNER_BOTTOM_RIGHT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 + 100,getViewportHeight() / 2 - 300), TileType.CORNER_BOTTOM_LEFT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 + 100,getViewportHeight() / 2 - 400), TileType.INNER_CORNER_TOP_RIGHT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 + 200,getViewportHeight() / 2 - 300), TileType.INNER_CORNER_TOP_RIGHT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 + 200,getViewportHeight() / 2 - 100), TileType.INNER_CORNER_BOTTOM_RIGHT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 + 100,getViewportHeight() / 2), TileType.INNER_CORNER_BOTTOM_RIGHT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 - 100,getViewportHeight() / 2 - 400), TileType.INNER_CORNER_TOP_LEFT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 - 200,getViewportHeight() / 2 - 300), TileType.INNER_CORNER_TOP_LEFT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 - 200,getViewportHeight() / 2 - 100), TileType.INNER_CORNER_BOTTOM_LEFT));
        addEntity(new Tile(new Coordinate2D(getViewportWidth()/2 - 100,getViewportHeight() / 2), TileType.INNER_CORNER_BOTTOM_LEFT));

        addEntity(new Player(new Coordinate2D(getViewportWidth() / 2, getViewportHeight() / 2 - 100)));

        addEntity(new InformationText(new Coordinate2D(getViewportWidth()/2 + 50,getViewportHeight() / 2 - 200), "Testing grounds"));
    }
}
