package nl.han.jefmk.scenes;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.ScrollableDynamicScene;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.decorational.HealthDisplay;
import nl.han.jefmk.entities.decorational.ScoreDisplay;
import nl.han.jefmk.entities.mobs.EnemySlime;
import nl.han.jefmk.entities.pickups.WinFlag;
import nl.han.jefmk.entities.player.Player;
import nl.han.jefmk.entities.projectiles.Lightning;
import nl.han.jefmk.levels.LevelBuilder;
import nl.han.jefmk.score.Score;
import nl.han.jefmk.levels.LevelLoader;
import nl.han.jefmk.levels.LevelRegistry;
import nl.han.jefmk.levels.model.LevelData;
import nl.han.jefmk.levels.model.PickupEntry;
import nl.han.jefmk.levels.model.TileEntry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class GameScene extends ScrollableDynamicScene implements KeyListener {

    private static final double DEAD_ZONE_FRACTION = 0.30;

    private final String levelName;
    private final BiConsumer<String, Integer> onLevelCompleted;
    private final Runnable onPlayerDeath;
    private final Set<KeyCode> previousKeys = new HashSet<>();
    private Coordinate2D spawnWorldPos = null;

    public GameScene(String levelName, BiConsumer<String, Integer> onLevelCompleted) {
        this(levelName, onLevelCompleted, null);
    }

    public GameScene(String levelName, BiConsumer<String, Integer> onLevelCompleted, Runnable onPlayerDeath) {
        this.levelName = levelName;
        this.onLevelCompleted = onLevelCompleted;
        this.onPlayerDeath = onPlayerDeath;
    }

    public void handlePlayerDeath() {
        if (onPlayerDeath != null) {
            javafx.application.Platform.runLater(onPlayerDeath);
        }
    }

    private static final int WORLD_MARGIN = 2000;

    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(39, 39, 68));
        // Start small; expands as the camera follows the player outward
        setSize(new Size(WORLD_MARGIN * 2, WORLD_MARGIN * 2));
        Score.getInstance().resetForNewLevel();
        double vpWidth = getViewportWidth();
        addEntity(new ScoreDisplay(new Coordinate2D(vpWidth - 10, 10)), true);
    }

    @Override
    public void setupEntities() {
        LevelRegistry registry = LevelRegistry.getInstance();
        LevelLoader loader = new LevelLoader();
        LevelBuilder builder = new LevelBuilder(registry);

        LevelData data = loader.load(levelName);

        // Register win_flag with the win callback before building the level.
        // This overrides the null placeholder registered in PickupRegistrar.
        registry.register("win_flag", location ->
                new WinFlag(location, () -> javafx.application.Platform.runLater(this::completeLevel)));

        double tileSize = data.getTileSize();
        // Expand the world to fit every placed tile and pickup so nothing is clipped on load
        for (TileEntry tile : data.getTiles()) {
            expandWorldIfNeeded(tile.getGridX() * tileSize, EleSlime.Y_OFFSET + tile.getGridY() * tileSize);
        }
        for (PickupEntry pickup : data.getPickups()) {
            expandWorldIfNeeded(pickup.getGridX() * tileSize, EleSlime.Y_OFFSET + pickup.getGridY() * tileSize);
        }

        if (data.getSpawn() != null) {
            spawnWorldPos = new Coordinate2D(
                    data.getSpawn().getGridX() * tileSize,
                    EleSlime.Y_OFFSET + data.getSpawn().getGridY() * tileSize
            );
            expandWorldIfNeeded(spawnWorldPos.getX(), spawnWorldPos.getY());
            Player player = new Player(spawnWorldPos, 3, this);
            addEntity(new HealthDisplay(new Coordinate2D(10, 3), player), true);
            player.setPositionListener(pos -> {
                expandWorldIfNeeded(pos.getX(), pos.getY());
                updateCameraPosition(pos);
            });

            if (EleSlime.DEBUG) {
                TextEntity debugOverlay = new TextEntity(new Coordinate2D(10, 80));
                debugOverlay.setFont(Font.font("Monospaced", FontWeight.BOLD, 12));
                debugOverlay.setFill(Color.LIME);
                addEntity(debugOverlay, true);
                player.setDebugListener(debugOverlay::setText);
            }
            addEntity(player);


            registry.register("enemy_slime", location -> new EnemySlime(location, player));
        }
        builder.buildFromData(data, (entry, entity) -> addEntity(entity), this::addEntity);
    }

    public void createLightningBolt(final Coordinate2D coordinate2D, final double rotation, final int bouncesLeft) {
        var lightningBolt = new Lightning(coordinate2D, rotation, this, bouncesLeft);
        addEntity(lightningBolt);
    }

    private void expandWorldIfNeeded(double worldX, double worldY) {
        double neededW = Math.max(getWidth(), worldX + WORLD_MARGIN);
        double neededH = Math.max(getHeight(), worldY + WORLD_MARGIN);
        if (neededW > getWidth() || neededH > getHeight()) {
            // Preserve pixel scroll offset so the viewport doesn't drift as the world grows
            double scrollableW = getWidth() - getViewportWidth();
            double scrollableH = getHeight() - getViewportHeight();
            double pixelX = scrollableW > 0 ? getHorizontalRelativeScrollPosition() * scrollableW : 0;
            double pixelY = scrollableH > 0 ? getVerticalRelativeScrollPosition() * scrollableH : 0;
            setSize(new Size(neededW, neededH));
            double newScrollableW = neededW - getViewportWidth();
            double newScrollableH = neededH - getViewportHeight();
            if (newScrollableW > 0) setHorizontalRelativeScrollPosition(pixelX / newScrollableW);
            if (newScrollableH > 0) setVerticalRelativeScrollPosition(pixelY / newScrollableH);
        }
    }

    @Override
    public void postActivate() {
        super.postActivate();
        // Center viewport on spawn so the player is always in the middle when the level loads
        if (spawnWorldPos != null) {
            setScrollPosition(new Coordinate2D(
                    Math.max(0, spawnWorldPos.getX() - getViewportWidth() / 2.0),
                    Math.max(0, spawnWorldPos.getY() - getViewportHeight() / 2.0)
            ));
        }
    }

    private void updateCameraPosition(Coordinate2D playerPos) {
        double sceneW = getWidth();
        double sceneH = getHeight();
        double vpW = getViewportWidth();
        double vpH = getViewportHeight();

        if (sceneW <= vpW && sceneH <= vpH) {
            return; // scene fits in viewport, no scrolling needed
        }

        double scrollableW = sceneW - vpW;
        double scrollableH = sceneH - vpH;

        // Current scroll offset in pixels
        double offsetX = getHorizontalRelativeScrollPosition() * scrollableW;
        double offsetY = getVerticalRelativeScrollPosition() * scrollableH;

        // Player position relative to viewport
        double playerScreenX = playerPos.getX() - offsetX;
        double playerScreenY = playerPos.getY() - offsetY;

        // Dead zone bounds (center portion of viewport)
        double zoneHalfW = vpW * DEAD_ZONE_FRACTION / 2.0;
        double zoneHalfH = vpH * DEAD_ZONE_FRACTION / 2.0;
        double centerX = vpW / 2.0;
        double centerY = vpH / 2.0;

        double leftBound = centerX - zoneHalfW;
        double rightBound = centerX + zoneHalfW;
        double topBound = centerY - zoneHalfH;
        double bottomBound = centerY + zoneHalfH;

        // Adjust horizontal scroll
        if (scrollableW > 0) {
            if (playerScreenX < leftBound) {
                offsetX -= (leftBound - playerScreenX);
            } else if (playerScreenX > rightBound) {
                offsetX += (playerScreenX - rightBound);
            }
            offsetX = Math.clamp(offsetX, 0, scrollableW);
            setHorizontalRelativeScrollPosition(offsetX / scrollableW);
        }

        // Adjust vertical scroll
        if (scrollableH > 0) {
            if (playerScreenY < topBound) {
                offsetY -= (topBound - playerScreenY);
            } else if (playerScreenY > bottomBound) {
                offsetY += (playerScreenY - bottomBound);
            }
            offsetY = Math.clamp(offsetY, 0, scrollableH);
            setVerticalRelativeScrollPosition(offsetY / scrollableH);
        }
    }

    private void completeLevel() {
        int score = Score.getInstance().getScore();
        onLevelCompleted.accept(levelName, score);
    }

    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        Set<KeyCode> freshKeys = new HashSet<>(pressedKeys);
        freshKeys.removeAll(previousKeys);
        previousKeys.clear();
        previousKeys.addAll(pressedKeys);

        if (freshKeys.contains(KeyCode.F1)) {
            javafx.application.Platform.runLater(this::completeLevel);
        }
    }
}
