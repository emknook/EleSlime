package nl.han.jefmk.levels.model;

import java.util.ArrayList;
import java.util.List;

public class LevelData {
    private String name;
    private double tileSize;
    private SpawnPoint spawn;
    private List<TileEntry> tiles = new ArrayList<>();
    private List<PickupEntry> pickups = new ArrayList<>();
    private List<TextEntry> texts = new ArrayList<>();
    private List<MobEntry> mobs = new ArrayList<>();

    public LevelData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTileSize() {
        return tileSize;
    }

    public void setTileSize(double tileSize) {
        this.tileSize = tileSize;
    }

    public SpawnPoint getSpawn() {
        return spawn;
    }

    public void setSpawn(SpawnPoint spawn) {
        this.spawn = spawn;
    }

    public List<TileEntry> getTiles() {
        return tiles;
    }

    public void setTiles(List<TileEntry> tiles) {
        this.tiles = tiles;
    }

    public List<PickupEntry> getPickups() {
        return pickups;
    }

    public void setPickups(List<PickupEntry> pickups) {
        this.pickups = pickups;
    }

    public List<TextEntry> getTexts() {
        return texts;
    }

    public void setTexts(List<TextEntry> texts) {
        this.texts = texts;
    }

    public List<MobEntry> getMobs() { return mobs; }

    public void setMobs(List<MobEntry> mobs) { this.mobs = mobs; }
}
