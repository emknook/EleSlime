package nl.han.jefmk.scenes;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.Timer;
import com.github.hanyaeger.api.TimerContainer;
import com.github.hanyaeger.api.entities.Animation;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.LoopingAnimation;
import com.github.hanyaeger.api.entities.impl.DynamicRectangleEntity;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import com.github.hanyaeger.api.entities.impl.TextEntity;
import com.github.hanyaeger.api.scenes.DynamicScene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.decorational.TextButton;
import nl.han.jefmk.surfaces.TileSprite;
import nl.han.jefmk.surfaces.TileType;

public class MenuScene extends DynamicScene {

    private static final double PLATFORM_Y = 580;
    private static final int TILE_COUNT = 14;

    private final Runnable onPlay;
    private MenuPlayerSprite playerSprite;
    private boolean transitioning = false;

    public MenuScene(Runnable onPlay) {
        this.onPlay = onPlay;
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.rgb(39, 39, 68));
    }

    @Override
    public void setupEntities() {
        setupBackgroundLevel();

        TextEntity title = new TextEntity(new Coordinate2D(683, 180), "ELESLIME");
        title.setFont(Font.font("Roboto", FontWeight.BOLD, 80));
        title.setFill(Color.AQUAMARINE);
        title.setAnchorPoint(AnchorPoint.CENTER_CENTER);
        addEntity(title);

        addEntity(new TextButton(new Coordinate2D(683, 380), "»  PLAY  «", this::startTransition));
    }

    private void startTransition() {
        if (transitioning) return;
        transitioning = true;

        playerSprite.playJump();

        // Add fade overlay
        addEntity(new FadeOverlay(playerSprite, onPlay));
    }

    private void setupBackgroundLevel() {
        double ts = EleSlime.TILE_SIZE;

        // Floor row
        addEntity(new TileSprite(new Coordinate2D(0, PLATFORM_Y), TileType.CORNER_TOP_LEFT));
        for (int x = 1; x < TILE_COUNT - 1; x++) {
            addEntity(new TileSprite(new Coordinate2D(x * ts, PLATFORM_Y), TileType.FLOOR));
        }
        addEntity(new TileSprite(new Coordinate2D((TILE_COUNT - 1) * ts, PLATFORM_Y), TileType.CORNER_TOP_RIGHT));

        // Underground filling row
        addEntity(new TileSprite(new Coordinate2D(0, PLATFORM_Y + ts), TileType.WALL_RIGHT));
        for (int x = 1; x < TILE_COUNT - 1; x++) {
            addEntity(new TileSprite(new Coordinate2D(x * ts, PLATFORM_Y + ts), TileType.WALL_FILLING));
        }
        addEntity(new TileSprite(new Coordinate2D((TILE_COUNT - 1) * ts, PLATFORM_Y + ts), TileType.WALL_LEFT));

        // Decorative player (idle right)
        playerSprite = new MenuPlayerSprite(
                new Coordinate2D(250, PLATFORM_Y - EleSlime.MOB_SIZE));
        addEntity(playerSprite);

        // Decorative enemies
        addEntity(createDecorativeSprite(
                "sprites/enemy-spritesheet.png",
                new Coordinate2D(600, PLATFORM_Y - EleSlime.TILE_SIZE),
                new Size(EleSlime.TILE_SIZE), 2, 9,
                new LoopingAnimation(1, 7, 1, 8)));

        addEntity(createDecorativeSprite(
                "sprites/enemy-spritesheet.png",
                new Coordinate2D(900, PLATFORM_Y - EleSlime.TILE_SIZE),
                new Size(EleSlime.TILE_SIZE), 2, 9,
                new LoopingAnimation(0, 0, 0, 1)));

        addEntity(createDecorativeSprite(
                "sprites/enemy-spritesheet.png",
                new Coordinate2D(1100, PLATFORM_Y - EleSlime.TILE_SIZE),
                new Size(EleSlime.TILE_SIZE), 2, 9,
                new LoopingAnimation(1, 7, 1, 8)));
    }

    private DynamicSpriteEntity createDecorativeSprite(String resource, Coordinate2D location,
            Size size, int rows, int columns, Animation animation) {
        return new DynamicSpriteEntity(resource, location, size, rows, columns) {
            {
                setAutoCycle(300);
                playAnimation(animation);
            }
        };
    }

    private static class MenuPlayerSprite extends DynamicSpriteEntity {
        private final double groundY;

        MenuPlayerSprite(Coordinate2D location) {
            super("sprites/eleslime-spritesheet.png", location, new Size(EleSlime.MOB_SIZE), 2, 20);
            this.groundY = location.getY();
            setAutoCycle(300L);
            playAnimation(new LoopingAnimation(0, 0, 0, 1));
        }

        public double getGroundY() {
            return groundY;
        }

        public void playJump() {
            setAutoCycle(100L);
            // Jump frames only (no chained run yet)
            playAnimation(new LoopingAnimation(1, 5, 1, 15));
        }

        public void playRunLeft() {
            setAutoCycle(100L);
            playAnimation(new LoopingAnimation(1, 0, 1, 6));
            setMotion(10, Direction.LEFT);
        }
    }

    private static class FadeOverlay extends DynamicRectangleEntity implements TimerContainer {

        private static final long TICK_MS = 16;
        private static final double JUMP_DURATION_S = 0.5;
        private static final double JUMP_HEIGHT = 120;
        private static final double FADE_DURATION_S = 1.0;

        private final MenuPlayerSprite playerSprite;
        private final Runnable onComplete;
        private double elapsed = 0;
        private boolean landed = false;
        private double fadeElapsed = 0;

        FadeOverlay(MenuPlayerSprite playerSprite, Runnable onComplete) {
            super(new Coordinate2D(0, 0), new Size(1366, 768));
            this.playerSprite = playerSprite;
            this.onComplete = onComplete;
            setFill(Color.BLACK);
            setOpacity(0);
        }

        @Override
        public void setupTimers() {
            addTimer(new Timer(TICK_MS) {
                @Override
                public void onAnimationUpdate(long timestamp) {
                    elapsed += TICK_MS / 1000.0;

                    if (!landed) {
                        // Parabolic jump arc: t goes 0→1 over JUMP_DURATION_S
                        double t = Math.min(elapsed / JUMP_DURATION_S, 1.0);
                        // y offset: 0 at t=0, -JUMP_HEIGHT at t=0.5, 0 at t=1
                        double yOffset = -4 * JUMP_HEIGHT * t * (1 - t);
                        playerSprite.setAnchorLocationY(playerSprite.getGroundY() + yOffset);

                        if (t >= 1.0) {
                            landed = true;
                            playerSprite.setAnchorLocationY(playerSprite.getGroundY());
                            playerSprite.playRunLeft();
                        }
                    } else {
                        // Fade to black after landing
                        fadeElapsed += TICK_MS / 1000.0;
                        double progress = Math.min(fadeElapsed / FADE_DURATION_S, 1.0);
                        setOpacity(progress);

                        if (progress >= 1.0) {
                            pause();
                            javafx.application.Platform.runLater(onComplete);
                        }
                    }
                }
            });
        }
    }
}
