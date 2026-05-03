package nl.han.jefmk;

import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;
import javafx.application.Platform;

import java.util.Arrays;

import nl.han.jefmk.levels.LevelRegistry;
import nl.han.jefmk.levels.registration.PickupRegistrar;
import nl.han.jefmk.levels.registration.TileRegistrar;
import nl.han.jefmk.scenes.ExampleScene;
import nl.han.jefmk.scenes.GameScene;
import nl.han.jefmk.levels.editor.LevelEditorScene;
import nl.han.jefmk.levels.editor.LevelSelectScene;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

public class EleSlime extends YaegerGame {

    public final static boolean DEBUG = true;
    public final static int TILE_SIZE = 100;

    // Scene is 12000px tall; Y_OFFSET is the pixel position of grid-row 0.
    // This gives us 6000px above (negative grid Y) and 6000 below (positive grid Y).
    public final static int Y_OFFSET = 6000;

    public static void main(String[] args) {
        launch(withDefaultYaegerArgs(args));
    }

    private static String[] withDefaultYaegerArgs(String[] args) {
        if (Arrays.asList(args).contains("--enableScroll")) {
            return args;
        }

        String[] launchArgs = Arrays.copyOf(args, args.length + 1);
        launchArgs[args.length] = "--enableScroll";
        return launchArgs;
    }

    @Override
    public void setupGame() {
        setSize(new Size(1366, 768));

        LevelRegistry registry = LevelRegistry.getInstance();
        TileRegistrar.registerAll(registry);
        PickupRegistrar.registerAll(registry);
    }

    @Override
    public void setupScenes() {
        addScene(0, new LevelSelectScene(selection -> {
            Platform.runLater(() -> {
                if (selection.startsWith("edit:")) {
                    String levelName = selection.substring(5);
                    addScene(2, new LevelEditorScene(levelName, () -> setActiveScene(0)));
                    setActiveScene(2);
                } else {
                    addScene(1, new GameScene(selection, () -> setActiveScene(0)));
                    setActiveScene(1);
                }
            });
        }));
    }
}