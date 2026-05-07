package nl.han.jefmk.levels;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.YaegerEntity;
import nl.han.jefmk.EleSlime;
import nl.han.jefmk.entities.decorational.InformationText;
import nl.han.jefmk.entities.pickups.Pickup;
import nl.han.jefmk.levels.model.GridEntry;
import nl.han.jefmk.levels.model.LevelData;
import nl.han.jefmk.levels.model.TextEntry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LevelBuilder {

    private final LevelRegistry registry;

    public LevelBuilder(LevelRegistry registry) {
        this.registry = registry;
    }

    public void buildFromData(LevelData data, BiConsumer<GridEntry, YaegerEntity> entityAdder) {
        buildFromData(data, entityAdder, null);
    }

    public void buildFromData(LevelData data, BiConsumer<GridEntry, YaegerEntity> entityAdder, Consumer<YaegerEntity> textAdder) {
        double tileSize = data.getTileSize();

        for (GridEntry entry : data.getTiles()) {
            entityAdder.accept(entry, build(entry, tileSize));
        }

        for (GridEntry entry : data.getPickups()) {
            YaegerEntity entity = build(entry, tileSize);
            if (entity instanceof Pickup pickup) {
                entityAdder.accept(entry, pickup.getPickupCollider());
            }
            entityAdder.accept(entry, entity);
        }

        for (GridEntry entry : data.getMobs()) {
            entityAdder.accept(entry, build(entry, tileSize));
        }

        if (textAdder != null && data.getTexts() != null) {
            for (TextEntry text : data.getTexts()) {
                Coordinate2D location = new Coordinate2D(
                        text.getGridX() * tileSize + tileSize / 2.0,
                        EleSlime.Y_OFFSET + text.getGridY() * tileSize + tileSize / 2.0);
                textAdder.accept(new InformationText(location, text.getText()));
            }
        }
    }

    public YaegerEntity build(GridEntry entry, double tileSize) {
        return registry.create(entry.getType(), toLocation(entry, tileSize));
    }

    private Coordinate2D toLocation(GridEntry entry, double tileSize) {
        return new Coordinate2D(entry.getGridX() * tileSize, EleSlime.Y_OFFSET + entry.getGridY() * tileSize);
    }
}
