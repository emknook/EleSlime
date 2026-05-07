package nl.han.jefmk.levels.registration;

import nl.han.jefmk.entities.obstacles.Stalagmite;
import nl.han.jefmk.entities.obstacles.Sulphur;
import nl.han.jefmk.levels.EntitySupplier;
import nl.han.jefmk.levels.LevelRegistry;

import java.util.List;

public class ObstacleRegistrar {

    private static final List<ObstacleDefinition> OBSTACLE_DEFINITIONS = List.of(
            new ObstacleDefinition("stalagmite", "sprites/stalagmite.png", Stalagmite::new),
            new ObstacleDefinition("sulphur", "sprites/sulphur.png", Sulphur::new)
    );

    private static final List<String> TYPE_IDS = OBSTACLE_DEFINITIONS.stream()
            .map(ObstacleDefinition::typeId)
            .toList();

    private static final List<String> SPRITE_RESOURCES = OBSTACLE_DEFINITIONS.stream()
            .map(ObstacleDefinition::spriteResource)
            .toList();

    public static void registerAll(LevelRegistry registry) {
        for (ObstacleDefinition definition : OBSTACLE_DEFINITIONS) {
            registry.register(definition.typeId(), definition.supplier());
        }
    }

    public static List<String> getTypeIds() {
        return TYPE_IDS;
    }

    public static List<String> getSpriteResources() {
        return SPRITE_RESOURCES;
    }

    private record ObstacleDefinition(String typeId, String spriteResource, EntitySupplier supplier) {}
}
