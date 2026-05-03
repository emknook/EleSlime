package nl.han.jefmk.levels.registration;

import nl.han.jefmk.levels.LevelRegistry;
import nl.han.jefmk.surfaces.Tile;
import nl.han.jefmk.surfaces.TileType;

import java.util.List;

public class TileRegistrar {

    private static final List<TileDefinition> TILE_DEFINITIONS = List.of(
            new TileDefinition("floor", TileType.FLOOR),
            new TileDefinition("ceiling", TileType.CEILING),
            new TileDefinition("wall_left", TileType.WALL_LEFT),
            new TileDefinition("wall_right", TileType.WALL_RIGHT),
            new TileDefinition("corner_top_left", TileType.CORNER_TOP_LEFT),
            new TileDefinition("corner_top_right", TileType.CORNER_TOP_RIGHT),
            new TileDefinition("corner_bottom_left", TileType.CORNER_BOTTOM_LEFT),
            new TileDefinition("corner_bottom_right", TileType.CORNER_BOTTOM_RIGHT),
            new TileDefinition("inner_corner_top_left", TileType.INNER_CORNER_TOP_LEFT),
            new TileDefinition("inner_corner_top_right", TileType.INNER_CORNER_TOP_RIGHT),
            new TileDefinition("inner_corner_bottom_left", TileType.INNER_CORNER_BOTTOM_LEFT),
            new TileDefinition("inner_corner_bottom_right", TileType.INNER_CORNER_BOTTOM_RIGHT)
    );

    private static final List<String> TYPE_IDS = TILE_DEFINITIONS.stream()
            .map(TileDefinition::typeId)
            .toList();

    public static void registerAll(LevelRegistry registry) {
        for (TileDefinition definition : TILE_DEFINITIONS) {
            registry.register(definition.typeId(), loc -> new Tile(loc, definition.tileType()));
        }
    }

    public static List<String> getTypeIds() {
        return TYPE_IDS;
    }

    private record TileDefinition(String typeId, TileType tileType) {}
}
