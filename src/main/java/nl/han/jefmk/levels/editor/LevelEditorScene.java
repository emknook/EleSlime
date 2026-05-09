package nl.han.jefmk.levels.editor;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.YaegerEntity;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.ScrollableDynamicScene;
import com.github.hanyaeger.api.userinput.MouseButtonPressedListener;
import com.github.hanyaeger.api.userinput.MouseMovedListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.decorational.InformationText;
import nl.han.jefmk.entities.player.PlayerSprite;
import nl.han.jefmk.levels.LevelBuilder;
import nl.han.jefmk.levels.LevelLoader;
import nl.han.jefmk.levels.LevelRegistry;
import nl.han.jefmk.levels.model.*;
import nl.han.jefmk.levels.registration.MobRegistrar;
import nl.han.jefmk.levels.registration.ObstacleRegistrar;
import nl.han.jefmk.levels.registration.PickupRegistrar;
import nl.han.jefmk.levels.registration.TileRegistrar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class LevelEditorScene extends ScrollableDynamicScene implements MouseButtonPressedListener, MouseMovedListener {

    private static final double TILE_SIZE = EleSlime.TILE_SIZE;
    private static final List<String> TILE_TYPE_IDS = TileRegistrar.getTypeIds();
    private static final List<String> PICKUP_TYPE_IDS = PickupRegistrar.getTypeIds();
    private static final List<String> PICKUP_GHOST_SPRITES = PickupRegistrar.getSpriteResources();
    private static final List<String> MOB_TYPE_IDS = MobRegistrar.getTypeIds();
    private static final List<String> MOB_GHOST_SPRITES = MobRegistrar.getSpriteResources();
    private static final List<String> OBSTACLE_TYPE_IDS = ObstacleRegistrar.getTypeIds();
    private static final List<String> OBSTACLE_GHOST_SPRITES = ObstacleRegistrar.getSpriteResources();

    private EditorMode mode = EditorMode.TILES;
    private int currentTileIndex = 0;
    private int currentPickupIndex = 0;
    private int currentMobIndex = 0;
    private int currentObstacleIndex = 0;

    private final Set<KeyCode> heldKeys = new HashSet<>();

    private final Map<String, YaegerEntity> placedEntities = new HashMap<>();
    private final List<TileEntry> tileEntries = new ArrayList<>();
    private final List<PickupEntry> pickupEntries = new ArrayList<>();
    private final List<TextEntry> textEntries = new ArrayList<>();
    private final List<MobEntry> mobEntries = new ArrayList<>();
    private final List<ObstacleEntry> obstacleEntries = new ArrayList<>();
    private SpawnPoint spawn = new SpawnPoint(1, 1);
    private PlayerSprite spawnMarker;

    private TextEntity currentTypeLabel;
    private TextEntity modeLabel;

    private GhostPreview tileGhost;
    private GhostPreview[] pickupGhosts;
    private GhostPreview[] mobGhosts;
    private GhostPreview[] obstacleGhosts;
    private GhostPreview spawnGhost;

    private final String levelName;
    private final Runnable switchBack;
    private final LevelLoader loader = new LevelLoader();
    private final LevelBuilder levelBuilder = new LevelBuilder(LevelRegistry.getInstance());
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String displayName;

    public LevelEditorScene(String levelName, Runnable switchBack) {
        this.levelName = levelName;
        this.displayName = levelName;
        this.switchBack = switchBack;
    }

    // Margin (in pixels) kept around the furthest placed entity so there is always room to scroll
    private static final int WORLD_MARGIN = 2000;

    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(20, 20, 40));
        // Start with just the margin; expands automatically as entities are placed
        setSize(new Size(WORLD_MARGIN * 2, WORLD_MARGIN * 2));
    }

    @Override
    public void setupEntities() {
        loadExistingLevel();
        // Expand world to fit all tiles/pickups that were loaded from disk
        for (TileEntry tile : tileEntries) {
            expandWorldIfNeeded(tile.getGridX(), tile.getGridY());
        }
        for (PickupEntry pickup : pickupEntries) {
            expandWorldIfNeeded(pickup.getGridX(), pickup.getGridY());
        }
        for (MobEntry mob : mobEntries) {
            expandWorldIfNeeded(mob.getGridX(), mob.getGridY());
        }
        for (ObstacleEntry obstacle : obstacleEntries) {
            expandWorldIfNeeded(obstacle.getGridX(), obstacle.getGridY());
        }
        expandWorldIfNeeded(spawn.getGridX(), spawn.getGridY());
        levelBuilder.buildFromData(toLevelData(), this::addPlacedEntity, this::addEntity);
        setupUI();
        setupGhosts();

        // Show spawn marker as player sprite, offset one tile up so it sits above the floor
        spawnMarker = new PlayerSprite(
                new Size(EleSlime.TILE_SIZE),
                spawnDisplayPixel(spawn.getGridX(), spawn.getGridY()));
        addEntity(spawnMarker);
    }

    private void setupGhosts() {
        Coordinate2D origin = new Coordinate2D(0, EleSlime.Y_OFFSET);

        // Tile ghost
        tileGhost = new GhostPreview(origin);
        tileGhost.showTileFrame(currentTileIndex);
        addEntity(tileGhost);

        // Pickup ghosts (one per type, only one visible at a time)
        pickupGhosts = new GhostPreview[PICKUP_GHOST_SPRITES.size()];
        for (int i = 0; i < PICKUP_GHOST_SPRITES.size(); i++) {
            pickupGhosts[i] = new GhostPreview(PICKUP_GHOST_SPRITES.get(i), origin);
            pickupGhosts[i].setVisible(false);
            addEntity(pickupGhosts[i]);
        }

        // Mob ghosts
        mobGhosts = new GhostPreview[MOB_GHOST_SPRITES.size()];
        for (int i = 0; i < MOB_GHOST_SPRITES.size(); i++) {
            mobGhosts[i] = new GhostPreview(MOB_GHOST_SPRITES.get(i), origin);
            mobGhosts[i].setVisible(false);
            addEntity(mobGhosts[i]);
        }

        // Obstacle ghosts
        obstacleGhosts = new GhostPreview[OBSTACLE_GHOST_SPRITES.size()];
        for (int i = 0; i < OBSTACLE_GHOST_SPRITES.size(); i++) {
            obstacleGhosts[i] = new GhostPreview(OBSTACLE_GHOST_SPRITES.get(i), origin);
            obstacleGhosts[i].setVisible(false);
            addEntity(obstacleGhosts[i]);
        }

        // Spawn ghost (green "S" represented as the player sprite)
        spawnGhost = new GhostPreview("sprites/eleslime.png", origin);
        spawnGhost.setVisible(false);
        addEntity(spawnGhost);

        updateGhostVisibility();
    }

    private void loadExistingLevel() {
        try {
            LevelData data = loader.load(levelName);
            if (data.getName() != null && !data.getName().isBlank()) {
                displayName = data.getName();
            }
            tileEntries.addAll(data.getTiles());
            pickupEntries.addAll(data.getPickups());
            if (data.getTexts() != null) {
                textEntries.addAll(data.getTexts());
            }
            if (data.getMobs() != null) {
                mobEntries.addAll(data.getMobs());
            }
            if (data.getObstacles() != null) {
                obstacleEntries.addAll(data.getObstacles());
            }
            if (data.getSpawn() != null) {
                spawn = data.getSpawn();
            }
        } catch (IllegalArgumentException e) {
            // No existing level — start fresh
        }
    }

    private void setupUI() {
        currentTypeLabel = new TextEntity(new Coordinate2D(10, 30), "Tile: " + currentTileTypeId());
        currentTypeLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
        currentTypeLabel.setFill(Color.YELLOW);
        addEntity(currentTypeLabel, true);

        modeLabel = new TextEntity(new Coordinate2D(10, 55), "Mode: TILES");
        modeLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 16));
        modeLabel.setFill(Color.CYAN);
        addEntity(modeLabel, true);

        TextEntity helpLabel = new TextEntity(new Coordinate2D(10, 80),
                "[Q/E] Cycle  [TAB] Mode  [Ctrl+S] Save  [WASD] Scroll  [ESC] Back");
        helpLabel.setFont(Font.font("Roboto", FontWeight.NORMAL, 12));
        helpLabel.setFill(Color.LIGHTGRAY);
        addEntity(helpLabel, true);
    }

    // --- Mouse handling ---

    @Override
    public void onMouseMoved(Coordinate2D coordinate2D) {
        int gridX = (int) Math.floor(coordinate2D.getX() / TILE_SIZE);
        int gridY = (int) Math.floor((coordinate2D.getY() - EleSlime.Y_OFFSET) / TILE_SIZE);
        Coordinate2D lastGridPixel = new Coordinate2D(gridX * TILE_SIZE, EleSlime.Y_OFFSET + gridY * TILE_SIZE);
        tileGhost.updatePosition(lastGridPixel);
        for (GhostPreview pg : pickupGhosts) {
            pg.updatePosition(lastGridPixel);
        }
        for (GhostPreview pg : mobGhosts) {
            pg.updatePosition(lastGridPixel);
        }
        for (GhostPreview pg : obstacleGhosts) {
            pg.updatePosition(lastGridPixel);
        }
        spawnGhost.updatePosition(spawnDisplayPixel(gridX, gridY));
    }

    @Override
    public void onMouseButtonPressed(MouseButton button, Coordinate2D coordinate2D) {
        int gridX = (int) Math.floor(coordinate2D.getX() / TILE_SIZE);
        int gridY = (int) Math.floor((coordinate2D.getY() - EleSlime.Y_OFFSET) / TILE_SIZE);

        if (button == MouseButton.PRIMARY) {
            switch (mode) {
                case TILES -> placeTile(gridX, gridY);
                case PICKUPS -> placePickup(gridX, gridY);
                case OBSTACLES -> placeObstacle(gridX, gridY);
                case TEXT -> placeText(gridX, gridY);
                case SPAWN -> placeSpawn(gridX, gridY);
                case MOBS -> placeMob(gridX, gridY);
            }
        } else if (button == MouseButton.SECONDARY) {
            removeTileOrPickup(gridX, gridY);
        }
    }

    // --- Placement ---

    private void placeTile(int gridX, int gridY) {
        removeTileAt(gridX, gridY);
        expandWorldIfNeeded(gridX, gridY);
        TileEntry entry = new TileEntry(gridX, gridY, currentTileTypeId());
        tileEntries.add(entry);
        addPlacedEntity(entry, levelBuilder.build(entry, TILE_SIZE));
    }

    private void placePickup(int gridX, int gridY) {
        removePickupAt(gridX, gridY);
        expandWorldIfNeeded(gridX, gridY);
        PickupEntry entry = new PickupEntry(gridX, gridY, currentPickupTypeId());
        pickupEntries.add(entry);
        addPlacedEntity(entry, levelBuilder.build(entry, TILE_SIZE));
    }

    private void placeSpawn(int gridX, int gridY) {
        expandWorldIfNeeded(gridX, gridY);
        spawn = new SpawnPoint(gridX, gridY);
        spawnMarker.setAnchorLocation(spawnDisplayPixel(gridX, gridY));
    }

    private void placeMob(int gridX, int gridY) {
        removeMobAt(gridX, gridY);
        expandWorldIfNeeded(gridX, gridY);
        MobEntry entry = new MobEntry(gridX, gridY, currentMobTypeId());
        mobEntries.add(entry);
        addPlacedEntity(entry, levelBuilder.build(entry, TILE_SIZE));
    }

    private void placeObstacle(int gridX, int gridY) {
        removeObstacleAt(gridX, gridY);
        expandWorldIfNeeded(gridX, gridY);
        ObstacleEntry entry = new ObstacleEntry(gridX, gridY, currentObstacleTypeId());
        obstacleEntries.add(entry);
        addPlacedEntity(entry, levelBuilder.build(entry, TILE_SIZE));
    }

    private void placeText(int gridX, int gridY) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Place Text");
        dialog.setHeaderText(null);
        dialog.setContentText("Text to place:");
        dialog.showAndWait().ifPresent(text -> {
            if (!text.isBlank()) {
                removeTextAt(gridX, gridY);
                expandWorldIfNeeded(gridX, gridY);
                TextEntry entry = new TextEntry(gridX, gridY, text);
                textEntries.add(entry);
                Coordinate2D location = new Coordinate2D(
                    gridX * TILE_SIZE + TILE_SIZE / 2.0,
                    EleSlime.Y_OFFSET + gridY * TILE_SIZE + TILE_SIZE / 2.0);
                InformationText textEntity = new InformationText(location, text);
                placedEntities.put(key("text", gridX, gridY), textEntity);
                addEntity(textEntity);
            }
        });
    }

    // --- Removal ---

    private void removeTileOrPickup(int gridX, int gridY) {
        removeTileAt(gridX, gridY);
        removePickupAt(gridX, gridY);
        removeTextAt(gridX, gridY);
        removeMobAt(gridX, gridY);
        removeObstacleAt(gridX, gridY);
    }

    private void removeTileAt(int gridX, int gridY) {
        removeEntity("tile", gridX, gridY);
        tileEntries.removeIf(e -> e.getGridX() == gridX && e.getGridY() == gridY);
    }

    private void removePickupAt(int gridX, int gridY) {
        removeEntity("pickup", gridX, gridY);
        pickupEntries.removeIf(e -> e.getGridX() == gridX && e.getGridY() == gridY);
    }

    private void removeTextAt(int gridX, int gridY) {
        removeEntity("text", gridX, gridY);
        textEntries.removeIf(e -> e.getGridX() == gridX && e.getGridY() == gridY);
    }

    private void removeMobAt(int gridX, int gridY) {
        removeEntity("mob", gridX, gridY);
        mobEntries.removeIf(e -> e.getGridX() == gridX && e.getGridY() == gridY);
    }

    private void removeObstacleAt(int gridX, int gridY) {
        removeEntity("obstacle", gridX, gridY);
        obstacleEntries.removeIf(e -> e.getGridX() == gridX && e.getGridY() == gridY);
    }

    private void removeEntity(String prefix, int gridX, int gridY) {
        YaegerEntity existing = placedEntities.remove(key(prefix, gridX, gridY));
        if (existing != null) {
            existing.remove();
        }
    }

    // --- Keyboard handling ---

    private static final double SCROLL_STEP = 0.05;
    private double zoom = 1.0;

    private void expandWorldIfNeeded(int gridX, int gridY) {
        double neededW = Math.max(getWidth(), gridX * TILE_SIZE + WORLD_MARGIN);
        double neededH = Math.max(getHeight(), EleSlime.Y_OFFSET + gridY * TILE_SIZE + WORLD_MARGIN);
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
        // Center viewport on the spawn point so the player always starts in view
        double spawnWorldX = spawn.getGridX() * TILE_SIZE;
        double spawnWorldY = EleSlime.Y_OFFSET + spawn.getGridY() * TILE_SIZE;
        setScrollPosition(new Coordinate2D(
                Math.max(0, spawnWorldX - getViewportWidth() / 2.0),
                Math.max(0, spawnWorldY - getViewportHeight() / 2.0)
        ));

        // Bypass Yaeger's key handling to avoid ConcurrentModificationException.
        // Raw JavaFX event filters handle editor keys and consume them before Yaeger's key delegate.
        getScene().addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            boolean fresh = heldKeys.add(e.getCode());
            handleKeyPress(e.getCode(), fresh);
            e.consume();
        });
        getScene().addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            heldKeys.remove(e.getCode());
            e.consume();
        });

        // Ctrl+scroll to zoom the editor content
        getScene().addEventFilter(ScrollEvent.SCROLL, e -> {
            if (!e.isControlDown()) return;
            zoom = Math.clamp(zoom + (e.getDeltaY() > 0 ? 0.1 : -0.1), 0.25, 3.0);
            getScene().getRoot().lookupAll(".scroll-pane").stream()
                    .filter(n -> n instanceof ScrollPane)
                    .map(n -> (ScrollPane) n)
                    .findFirst()
                    .ifPresent(sp -> {
                        sp.getContent().setScaleX(zoom);
                        sp.getContent().setScaleY(zoom);
                    });
            e.consume();
        });
    }

    private void handleKeyPress(KeyCode keyCode, boolean fresh) {
        if (keyCode == KeyCode.W) {
            setVerticalRelativeScrollPosition(Math.max(0, getVerticalRelativeScrollPosition() - SCROLL_STEP));
        }
        if (keyCode == KeyCode.A) {
            setHorizontalRelativeScrollPosition(Math.max(0, getHorizontalRelativeScrollPosition() - SCROLL_STEP));
        }
        if (keyCode == KeyCode.S && !heldKeys.contains(KeyCode.CONTROL)) {
            setVerticalRelativeScrollPosition(Math.min(1, getVerticalRelativeScrollPosition() + SCROLL_STEP));
        }
        if (keyCode == KeyCode.D) {
            setHorizontalRelativeScrollPosition(Math.min(1, getHorizontalRelativeScrollPosition() + SCROLL_STEP));
        }

        if (!fresh) {
            return;
        }

        if (keyCode == KeyCode.E) {
            cycleType(1);
        } else if (keyCode == KeyCode.Q) {
            cycleType(-1);
        }
        if (keyCode == KeyCode.TAB) {
            cycleMode();
        }
        if (keyCode == KeyCode.S && heldKeys.contains(KeyCode.CONTROL)) {
            saveLevel();
        }
        if (keyCode == KeyCode.ESCAPE) {
            switchBack.run();
        }
    }

    private void cycleMode() {
        EditorMode[] modes = EditorMode.values();
        int next = (mode.ordinal() + 1) % modes.length;
        mode = modes[next];
        updateLabels();
    }

    private void cycleType(int delta) {
        switch (mode) {
            case TILES -> currentTileIndex = Math.floorMod(currentTileIndex + delta, TILE_TYPE_IDS.size());
            case PICKUPS -> currentPickupIndex = Math.floorMod(currentPickupIndex + delta, PICKUP_TYPE_IDS.size());
            case OBSTACLES -> currentObstacleIndex = Math.floorMod(currentObstacleIndex + delta, OBSTACLE_TYPE_IDS.size());
            case MOBS -> currentMobIndex = Math.floorMod(currentMobIndex + delta, MOB_TYPE_IDS.size());
            default -> {}
        }
        updateLabels();
    }

    private void updateLabels() {
        switch (mode) {
            case TILES -> {
                currentTypeLabel.setText("Tile: " + currentTileTypeId());
                modeLabel.setText("Mode: TILES");
                tileGhost.showTileFrame(currentTileIndex);
            }
            case PICKUPS -> {
                currentTypeLabel.setText("Pickup: " + currentPickupTypeId());
                modeLabel.setText("Mode: PICKUPS");
            }
            case OBSTACLES -> {
                currentTypeLabel.setText("Obstacle: " + currentObstacleTypeId());
                modeLabel.setText("Mode: OBSTACLES");
            }
            case MOBS -> {
                currentTypeLabel.setText("Mob: " + currentMobTypeId());
                modeLabel.setText("Mode: MOBS");
            }
            case TEXT -> {
                currentTypeLabel.setText("Text: (click to place)");
                modeLabel.setText("Mode: TEXT");
            }
            case SPAWN -> {
                currentTypeLabel.setText("Click to place spawn");
                modeLabel.setText("Mode: SPAWN");
            }
        }
        updateGhostVisibility();
    }

    private void updateGhostVisibility() {
        tileGhost.setVisible(mode == EditorMode.TILES);
        spawnGhost.setVisible(mode == EditorMode.SPAWN);
        for (int i = 0; i < pickupGhosts.length; i++) {
            pickupGhosts[i].setVisible(mode == EditorMode.PICKUPS && i == currentPickupIndex);
        }
        for (int i = 0; i < obstacleGhosts.length; i++) {
            obstacleGhosts[i].setVisible(mode == EditorMode.OBSTACLES && i == currentObstacleIndex);
        }
        for (int i = 0; i < mobGhosts.length; i++) {
            mobGhosts[i].setVisible(mode == EditorMode.MOBS && i == currentMobIndex);
        }
        // TEXT mode has no ghost preview
    }

    // --- Save ---

    private void saveLevel() {
        String json = gson.toJson(toLevelData());

        String relativePath = String.join("/", "levels", levelName + ".json");

        // Write to source resources (for permanent storage)
        try {
            Path srcPath = Path.of("src/main/resources", relativePath);
            Files.createDirectories(srcPath.getParent());
            Files.writeString(srcPath, json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save level to src: " + e.getMessage(), e);
        }

        // Also write to the classpath location so the game can load it immediately
        try {
            java.net.URL url = getClass().getClassLoader().getResource("levels");
            if (url != null) {
                Path classpathDir = Path.of(url.toURI());
                Files.writeString(classpathDir.resolve(levelName + ".json"), json);
            }
        } catch (Exception e) {
            // Classpath write failed (e.g. inside a jar) — src copy is still saved
        }
    }

    private LevelData toLevelData() {
        LevelData data = new LevelData();
        data.setName(displayName);
        data.setTileSize(TILE_SIZE);
        data.setSpawn(spawn);
        data.setTiles(new ArrayList<>(tileEntries));
        data.setPickups(new ArrayList<>(pickupEntries));
        data.setMobs(new ArrayList<>(mobEntries));
        data.setObstacles(new ArrayList<>(obstacleEntries));
        data.setTexts(new ArrayList<>(textEntries));
        return data;
    }

    private String currentTileTypeId() {
        return TILE_TYPE_IDS.get(currentTileIndex);
    }

    private String currentPickupTypeId() {
        return PICKUP_TYPE_IDS.get(currentPickupIndex);
    }

    private String currentMobTypeId() { return MOB_TYPE_IDS.get(currentMobIndex); }

    private String currentObstacleTypeId() { return OBSTACLE_TYPE_IDS.get(currentObstacleIndex); }

    private void addPlacedEntity(GridEntry entry, YaegerEntity entity) {
        String prefix = entry instanceof PickupEntry ? "pickup"
                : entry instanceof ObstacleEntry ? "obstacle"
                : entry instanceof MobEntry ? "mob"
                : "tile";
        placedEntities.put(key(prefix, entry.getGridX(), entry.getGridY()), entity);
        addEntity(entity);
    }

    private String key(String prefix, int gridX, int gridY) {
        return prefix + ":" + gridX + "," + gridY;
    }

    // Spawn marker/ghost is displayed one tile above the stored grid row so it sits on top of the floor.
    private Coordinate2D spawnDisplayPixel(int gridX, int gridY) {
        return new Coordinate2D(gridX * TILE_SIZE, EleSlime.Y_OFFSET + (gridY - 1) * TILE_SIZE);
    }
}
