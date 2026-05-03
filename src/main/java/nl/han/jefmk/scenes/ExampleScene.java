package nl.han.jefmk.scenes;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.ScrollableDynamicScene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import nl.han.jefmk.entities.decorational.InformationText;
import nl.han.jefmk.entities.player.Player;
import nl.han.jefmk.surfaces.Tile;
import nl.han.jefmk.surfaces.TileType;

public class ExampleScene extends ScrollableDynamicScene {

    private static final double CENTER_X = 600;
    private static final double CENTER_Y = 600;
    private static final double TILE_SIZE = 100;

    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(39, 39, 68));
    }

    @Override
    public void setupEntities() {
        buildRoom(CENTER_X, CENTER_Y, 7, 6);
        buildRoom(CENTER_X + 800, CENTER_Y - 100, 5, 5);

        buildPlatform(CENTER_X + 350, CENTER_Y - 150, 2);
        buildPlatform(CENTER_X + 550, CENTER_Y - 300, 2);

        addEntity(new Player(new Coordinate2D(CENTER_X, CENTER_Y - TILE_SIZE)));
        addEntity(new InformationText(new Coordinate2D(CENTER_X + 50, CENTER_Y - 300), "Testing grounds"));
    }

    private void buildRoom(double centerX, double centerY, int width, int height) {
        double left   = centerX - (width  / 2.0) * TILE_SIZE;
        double top    = centerY - (height / 2.0) * TILE_SIZE;
        double right  = left + (width  - 1) * TILE_SIZE;
        double bottom = top  + (height - 1) * TILE_SIZE;

        // Vloer (zonder hoektegels)
        for (int column = 1; column < width - 1; column++) {
            addTile(left + column * TILE_SIZE, bottom, TileType.FLOOR);
        }

        // Plafond (zonder hoektegels)
        for (int column = 1; column < width - 1; column++) {
            addTile(left + column * TILE_SIZE, top, TileType.CEILING);
        }

        // Linkermuur — binnenhoeken vervangen de eerste en laatste muurtegel
        addTile(left, top    + TILE_SIZE, TileType.INNER_CORNER_TOP_LEFT);
        addTile(left, bottom - TILE_SIZE, TileType.INNER_CORNER_BOTTOM_LEFT);
        for (int row = 2; row < height - 2; row++) {
            addTile(left, top + row * TILE_SIZE, TileType.WALL_LEFT);
        }

        // Rechtermuur — binnenhoeken vervangen de eerste en laatste muurtegel
        addTile(right, top    + TILE_SIZE, TileType.INNER_CORNER_TOP_RIGHT);
        addTile(right, bottom - TILE_SIZE, TileType.INNER_CORNER_BOTTOM_RIGHT);
        for (int row = 2; row < height - 2; row++) {
            addTile(right, top + row * TILE_SIZE, TileType.WALL_RIGHT);
        }

        // Buitenhoeken
        addTile(left,  top,    TileType.CORNER_TOP_LEFT);
        addTile(right, top,    TileType.CORNER_TOP_RIGHT);
        addTile(left,  bottom, TileType.CORNER_BOTTOM_LEFT);
        addTile(right, bottom, TileType.CORNER_BOTTOM_RIGHT);
    }

    private void buildPlatform(double startX, double startY, int length) {
        for (int column = 0; column < length; column++) {
            addTile(startX + column * TILE_SIZE, startY, TileType.FLOOR);
        }
    }

    private void addTile(double x, double y, TileType tileType) {
        addEntity(new Tile(new Coordinate2D(x, y), tileType));

        TextEntity debugLabel = new TextEntity(new Coordinate2D(x + 10, y + 10), tileType.name());
        debugLabel.setFill(Color.RED);
        debugLabel.setFont(Font.font(10));
        addEntity(debugLabel);
    }
}