package nl.han.jefmk.levels.model;

import java.util.HashMap;
import java.util.Map;

public class ObstacleEntry extends GridEntry {

    private Map<String, Object> config = new HashMap<>();

    public ObstacleEntry() {}

    public ObstacleEntry(int gridX, int gridY, String type) {
        super(gridX, gridY, type);
    }

    public Map<String, Object> getConfig() { return config; }
    public void setConfig(Map<String, Object> config) { this.config = config; }
}
