package nl.han.jefmk.entities.decorational;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Timer;
import com.github.hanyaeger.api.TimerContainer;
import com.github.hanyaeger.api.entities.impl.DynamicTextEntity;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.jefmk.entities.player.Player;

public class HealthDisplay extends DynamicTextEntity implements TimerContainer {

    private final Player player;

    public HealthDisplay(Coordinate2D initialLocation, Player player) {
        super(initialLocation);
        this.player = player;
        setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
        setFill(Color.RED);
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
        int health = player.getHealth();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < health; i++) {
            sb.append("♥ ");
        }
        setText(sb.toString().stripTrailing());
    }
}
