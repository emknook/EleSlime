package nl.han.jefmk.levels.registration;

import nl.han.jefmk.entities.mobs.FriendlySlime;
import nl.han.jefmk.entities.mobs.EnemySlime;
import nl.han.jefmk.levels.EntitySupplier;
import nl.han.jefmk.levels.LevelRegistry;

import java.util.List;

public class MobRegistrar {

    private static final List<MobDefinition> MOB_DEFINITIONS = List.of(
            new MobDefinition("friend_slime", "sprites/friendly-slime.png", FriendlySlime::new),
            new MobDefinition("enemy_slime", "sprites/enemy-slime.png", EnemySlime::new)
    );

    private static final List<String> TYPE_IDS = MOB_DEFINITIONS.stream()
            .map(MobDefinition::typeId)
            .toList();

    private static final List<String> SPRITE_RESOURCES = MOB_DEFINITIONS.stream()
            .map(MobDefinition::spriteResource)
            .toList();

    public static void registerAll(LevelRegistry registry) {
        for (MobDefinition definition : MOB_DEFINITIONS) {
            registry.register(definition.typeId(), definition.supplier());
        }
    }

    public static List<String> getTypeIds() {
        return TYPE_IDS;
    }

    public static List<String> getSpriteResources() {
        return SPRITE_RESOURCES;
    }

    private record MobDefinition(String typeId, String spriteResource, EntitySupplier supplier) {}
}
