package nl.han.jefmk.levels.model;

public class LevelMeta {
    private final String name;
    private final String filePath;

    public LevelMeta(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getLevelId() {
        String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
        return fileName.replace(".json", "");
    }
}
