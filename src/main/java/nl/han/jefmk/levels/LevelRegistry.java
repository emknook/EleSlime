package nl.han.jefmk.levels;

import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.entities.YaegerEntity;

import java.util.HashMap;
import java.util.Map;

public class LevelRegistry {

    private static final LevelRegistry INSTANCE = new LevelRegistry();

    private final Map<String, EntitySupplier> suppliers = new HashMap<>();

    private LevelRegistry() {}

    public static LevelRegistry getInstance() {
        return INSTANCE;
    }

    public void register(String typeId, EntitySupplier supplier) {
        suppliers.put(typeId, supplier);
    }

    public YaegerEntity create(String typeId, Coordinate2D location) {
        EntitySupplier supplier = suppliers.get(typeId);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown entity type: " + typeId);
        }
        return supplier.create(location);
    }
}
