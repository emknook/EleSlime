package nl.han.jefmk;

import com.github.hanyaeger.api.YaegerGame;
import nl.han.jefmk.scenes.GameScene;

public class EleSlime extends YaegerGame {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setupGame() {

    }

    @Override
    public void setupScenes() {
        addScene(1, new GameScene());
    }
}