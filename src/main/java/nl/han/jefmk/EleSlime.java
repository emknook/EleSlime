package nl.han.jefmk;

import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.YaegerGame;

public class EleSlime extends YaegerGame {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void setupGame() {
        setGameTitle("EleSlime");
        setSize(new Size(1366, 768));
    }

    @Override
    public void setupScenes() {
        // add scenes here later
    }
}