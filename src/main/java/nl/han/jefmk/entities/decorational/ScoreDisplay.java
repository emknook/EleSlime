package nl.han.jefmk.entities.decorational;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Timer;
import com.github.hanyaeger.api.TimerContainer;
import com.github.hanyaeger.api.entities.impl.DynamicTextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.jefmk.score.Score;

public class ScoreDisplay extends DynamicTextEntity implements TimerContainer {

    public ScoreDisplay(Coordinate2D location) {
        super(location);
        setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
        setFill(Color.rgb(220, 200, 255));
        setAnchorPoint(AnchorPoint.TOP_RIGHT);
        updateDisplay();
    }

    @Override
    public void setupTimers() {
        addTimer(new Timer(200) {
            @Override
            public void onAnimationUpdate(long timestamp) {
                updateDisplay();
            }
        });
    }

    private void updateDisplay() {
        setText("SCORE " + Score.getInstance().getScore());
    }
}

