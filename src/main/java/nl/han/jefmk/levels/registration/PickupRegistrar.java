package nl.han.jefmk.levels.registration;

import nl.han.jefmk.entities.pickups.BlueShroom;
import nl.han.jefmk.entities.pickups.GreenShroom;
import nl.han.jefmk.levels.EntitySupplier;
import nl.han.jefmk.levels.LevelRegistry;

import java.util.List;

public class PickupRegistrar {

    private static final List<PickupDefinition> PICKUP_DEFINITIONS = List.of(
            new PickupDefinition("blue_shroom", "sprites/blue-shroom.png", BlueShroom::new),
            new PickupDefinition("green_shroom", "sprites/green-shroom.png", GreenShroom::new)
    );

    private static final List<String> TYPE_IDS = PICKUP_DEFINITIONS.stream()
            .map(PickupDefinition::typeId)
            .toList();

    private static final List<String> SPRITE_RESOURCES = PICKUP_DEFINITIONS.stream()
            .map(PickupDefinition::spriteResource)
            .toList();

    public static void registerAll(LevelRegistry registry) {
        for (PickupDefinition definition : PICKUP_DEFINITIONS) {
            registry.register(definition.typeId(), definition.supplier());
        }
    }

    public static List<String> getTypeIds() {
        return TYPE_IDS;
    }

    public static List<String> getSpriteResources() {
        return SPRITE_RESOURCES;
    }

    private record PickupDefinition(String typeId, String spriteResource, EntitySupplier supplier) {}
}
