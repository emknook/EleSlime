package nl.han.jefmk;

import com.github.hanyaeger.api.YaegerGame;
import nl.han.jefmk.scenes.GameScene;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends YaegerGame {
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
