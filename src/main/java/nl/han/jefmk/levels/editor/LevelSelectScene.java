package nl.han.jefmk.levels.editor;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.jefmk.levels.LevelLoader;
import nl.han.jefmk.levels.model.LevelMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class LevelSelectScene extends DynamicScene implements KeyListener {

    private List<LevelMeta> levels = List.of();
    private int selectedIndex = 0;
    private TextEntity selectionIndicator;
    private final Consumer<String> onLevelSelected;
    private final Set<KeyCode> previousKeys = new HashSet<>();

    public LevelSelectScene(Consumer<String> onLevelSelected) {
        this.onLevelSelected = onLevelSelected;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(15, 15, 30));
    }

    @Override
    public void setupEntities() {
        LevelLoader loader = new LevelLoader();
        levels = loader.getAvailableLevels();

        TextEntity title = new TextEntity(new Coordinate2D(400, 60), "Select Level");
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 32));
        title.setFill(Color.WHITE);
        title.setAnchorPoint(AnchorPoint.TOP_CENTER);
        addEntity(title);

        TextEntity controls = new TextEntity(new Coordinate2D(400, 100),
                "[UP/DOWN] Navigate   [ENTER] Play   [E] Edit");
        controls.setFont(Font.font("Roboto", FontWeight.NORMAL, 14));
        controls.setFill(Color.LIGHTGRAY);
        controls.setAnchorPoint(AnchorPoint.TOP_CENTER);
        addEntity(controls);

        if (levels.isEmpty()) {
            TextEntity emptyState = new TextEntity(new Coordinate2D(400, 180), "No levels found");
            emptyState.setFont(Font.font("Roboto", FontWeight.NORMAL, 20));
            emptyState.setFill(Color.LIGHTGRAY);
            emptyState.setAnchorPoint(AnchorPoint.TOP_CENTER);
            addEntity(emptyState);
            return;
        }

        for (int i = 0; i < levels.size(); i++) {
            String label = levels.get(i).getName();
            TextEntity entry = new TextEntity(new Coordinate2D(200, 160 + i * 40), label);
            entry.setFont(Font.font("Roboto", FontWeight.NORMAL, 20));
            entry.setFill(Color.WHITE);
            addEntity(entry);
        }

        selectionIndicator = new TextEntity(new Coordinate2D(170, 160), ">");
        selectionIndicator.setFont(Font.font("Roboto", FontWeight.BOLD, 20));
        selectionIndicator.setFill(Color.YELLOW);
        addEntity(selectionIndicator);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        if (levels.isEmpty()) return;

        Set<KeyCode> freshKeys = new HashSet<>(pressedKeys);
        freshKeys.removeAll(previousKeys);

        if (freshKeys.contains(KeyCode.DOWN)) {
            moveSelection(1);
        } else if (freshKeys.contains(KeyCode.UP)) {
            moveSelection(-1);
        }

        if (freshKeys.contains(KeyCode.ENTER)) {
            selectCurrent(false);
        }

        if (freshKeys.contains(KeyCode.E)) {
            selectCurrent(true);
        }

        previousKeys.clear();
        previousKeys.addAll(pressedKeys);
    }

    private void moveSelection(int delta) {
        selectedIndex = Math.floorMod(selectedIndex + delta, levels.size());
        updateIndicator();
    }

    private void selectCurrent(boolean editMode) {
        String prefix = editMode ? "edit:" : "";
        Platform.runLater(() -> onLevelSelected.accept(prefix + levels.get(selectedIndex).getLevelId()));
    }

    private void updateIndicator() {
        selectionIndicator.setAnchorLocation(new Coordinate2D(170, 160 + selectedIndex * 40));
    }
}
