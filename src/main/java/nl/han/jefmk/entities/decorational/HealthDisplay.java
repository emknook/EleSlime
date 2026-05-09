package nl.han.jefmk.entities.decorational;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.entities.DynamicCompositeEntity;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import com.github.hanyaeger.api.entities.impl.SpriteEntity;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.HasHealth;

public class HealthDisplay extends DynamicCompositeEntity {

    private static final int MAX_HEARTS = 3;
    private static final double HEART_W = EleSlime.TILE_SIZE * 0.2;
    private static final double HEART_H = EleSlime.TILE_SIZE * 0.2;
    private static final double PADDING = EleSlime.TILE_SIZE * 0.04;

    private final HasHealth tracked;
    private HeartIcon[] hearts;

    public HealthDisplay(Coordinate2D location, HasHealth tracked) {
        super(location);
        this.tracked = tracked;
    }

    @Override
    protected void setupEntities() {
        hearts = new HeartIcon[MAX_HEARTS];
        for (int i = 0; i < MAX_HEARTS; i++) {
            hearts[i] = new HeartIcon(new Coordinate2D(i * (HEART_W + 4), PADDING));
            addEntity(hearts[i]);
        }

        updateHearts(tracked.getHealth());
        tracked.addHealthListener(this::updateHearts);
    }

    private void updateHearts(int health) {
        if (hearts == null) return;
        for (int i = 0; i < hearts.length; i++) {
            hearts[i].setFull(i < health);
        }
    }

    private static class HeartIcon extends DynamicSpriteEntity {
        HeartIcon(Coordinate2D location) {
            super("sprites/hearts.png", location, new Size(HEART_W * 2, HEART_H * 2), 1, 2);
            setCurrentFrameIndex(0);
        }

        void setFull(boolean full) {
            setCurrentFrameIndex(full ? 0 : 1);
        }
    }
}

