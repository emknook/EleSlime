package nl.han.jefmk;

import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;
import javafx.application.Platform;

import nl.han.jefmk.levels.LevelRegistry;
import nl.han.jefmk.levels.registration.MobRegistrar;
import nl.han.jefmk.levels.registration.ObstacleRegistrar;
import nl.han.jefmk.levels.registration.PickupRegistrar;
import nl.han.jefmk.levels.registration.TileRegistrar;
import nl.han.jefmk.scenes.GameScene;
import nl.han.jefmk.levels.editor.LevelEditorScene;
import nl.han.jefmk.levels.editor.LevelSelectScene;

public class EleSlime extends YaegerGame {

    public final static boolean DEBUG = true;
    public final static double TILE_SIZE = 100;
    public final static double MOB_SIZE = 80;

    // Used to roughly offset the world so we have more space above
    public final static int Y_OFFSET = 6000;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setupGame() {
        setSize(new Size(1366, 768));

        LevelRegistry registry = LevelRegistry.getInstance();
        TileRegistrar.registerAll(registry);
        PickupRegistrar.registerAll(registry);
        MobRegistrar.registerAll(registry);
        ObstacleRegistrar.registerAll(registry);
    }

    @Override
    public void setupScenes() {
        addScene(0, new LevelSelectScene(selection -> {
            // Using JavaFX Platform.runLater() so it runs after everything is done iterating and java doesn't get mad.
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