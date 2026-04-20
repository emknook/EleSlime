package nl.han.jefmk;

import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;
import nl.han.jefmk.scenes.ExampleScene;
import nl.han.jefmk.scenes.GameScene;

public class EleSlime extends YaegerGame {

    public final static boolean DEBUG = true;
    public final static int TILE_SIZE = 100;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setupGame() {
        setSize(new Size(1366, 768));

    }

    @Override
    public void setupScenes() {
        if (DEBUG) {
            addScene(1, new ExampleScene());
        } else {
            //startScene
            addScene(1, new GameScene());
            //endScene
        }
    }
}