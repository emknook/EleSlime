package nl.han.jefmk;

import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;
import javafx.application.Platform;
import nl.han.jefmk.levels.LevelRegistry;
import nl.han.jefmk.levels.editor.LevelEditorScene;
import nl.han.jefmk.levels.editor.LevelSelectScene;
import nl.han.jefmk.levels.registration.MobRegistrar;
import nl.han.jefmk.levels.registration.ObstacleRegistrar;
import nl.han.jefmk.levels.registration.PickupRegistrar;
import nl.han.jefmk.levels.registration.TileRegistrar;
import nl.han.jefmk.scenes.DeathScene;
import nl.han.jefmk.scenes.GameScene;
import nl.han.jefmk.scenes.MenuScene;

public class EleSlime extends YaegerGame {

    public static final boolean DEBUG = false;

    public static final double TILE_SIZE = 100;
    public static final double MOB_SIZE = 80;

    public static final int Y_OFFSET = 6000;

    private static final int MENU_SCENE_ID = 0;
    private static final int GAME_SCENE_ID = 1;
    private static final int LEVEL_EDITOR_SCENE_ID = 2;
    private static final int LEVEL_SELECT_SCENE_ID = 3;
    private static final int DEATH_SCENE_ID = 4;

    private static final String EDIT_PREFIX = "edit:";

    private static final String[] LEVEL_ORDER = {
            "lvl_1",
            "lvl_2",
            "lvl_3",
            "lvl_end"
    };

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
        if (DEBUG) {
            setupDebugScenes();
            return;
        }

        setupPlayScenes();
    }

    private void setupDebugScenes() {
        addScene(LEVEL_SELECT_SCENE_ID, new LevelSelectScene(selection -> Platform.runLater(() -> handleDebugLevelSelection(selection))));

        setActiveScene(LEVEL_SELECT_SCENE_ID);
    }

    private void setupPlayScenes() {
        loadMenuScene();
    }

    private void handleDebugLevelSelection(String selection) {
        if (selection.startsWith(EDIT_PREFIX)) {
            String levelName = selection.substring(EDIT_PREFIX.length());

            addScene(
                    LEVEL_EDITOR_SCENE_ID,
                    new LevelEditorScene(levelName, () -> setActiveScene(LEVEL_SELECT_SCENE_ID))
            );

            setActiveScene(LEVEL_EDITOR_SCENE_ID);
            return;
        }

        addScene(
                GAME_SCENE_ID,
                new GameScene(selection, this::handleLevelCompletedInDebugMode)
        );

        setActiveScene(GAME_SCENE_ID);
    }

    private void loadMenuScene() {
        addScene(MENU_SCENE_ID, new MenuScene(() -> loadGameScene(LEVEL_ORDER[0])));
        setActiveScene(MENU_SCENE_ID);
    }

    private void loadDeathScene(String failedLevelName) {
        addScene(DEATH_SCENE_ID, new DeathScene(
                () -> loadGameScene(failedLevelName),
                this::loadMenuScene
        ));
        setActiveScene(DEATH_SCENE_ID);
    }

    private void loadGameScene(String levelName) {
        addScene(
                GAME_SCENE_ID,
                new GameScene(levelName, this::handleLevelCompletedInPlayMode, () -> Platform.runLater(() -> loadDeathScene(levelName)))
        );

        setActiveScene(GAME_SCENE_ID);
    }

    private String getNextLevelName(String completedLevelName) {
        for (int levelIndex = 0; levelIndex < LEVEL_ORDER.length - 1; levelIndex++) {
            if (LEVEL_ORDER[levelIndex].equals(completedLevelName)) {
                return LEVEL_ORDER[levelIndex + 1];
            }
        }

        return null;
    }

    private void handleFinalLevelCompleted() {
        loadMenuScene();
    }

    private void handleLevelCompletedInDebugMode(String completedLevelName, int score) {
        LevelEditorScene.registerHighScore(completedLevelName, score);
        setActiveScene(LEVEL_SELECT_SCENE_ID);
    }

    private void handleLevelCompletedInPlayMode(String completedLevelName, int score) {
        LevelEditorScene.registerHighScore(completedLevelName, score);

        String nextLevelName = getNextLevelName(completedLevelName);

        if (nextLevelName == null) {
            handleFinalLevelCompleted();
            return;
        }

        loadGameScene(nextLevelName);
    }
}