package nl.han.jefmk.entities.decorational;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Timer;
import com.github.hanyaeger.api.TimerContainer;
import com.github.hanyaeger.api.entities.impl.DynamicTextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.jefmk.score.Score;

/**
 * HUD text entity that displays the current score.
 * Updates every second via a timer and reflects the time-penalty-based score.
 */
public class ScoreDisplay extends DynamicTextEntity implements TimerContainer {

    public ScoreDisplay(Coordinate2D initialLocation) {
        super(initialLocation);
        setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
        setFill(Color.WHITE);
        updateDisplay();
    }

    @Override
    public void setupTimers() {
        addTimer(new Timer(1000) {
            @Override
            public void onAnimationUpdate(long timestamp) {
                updateDisplay();
            }
        });
    }

    private void updateDisplay() {
        setText("Score: " + Score.getInstance().getScore());
    }
}
