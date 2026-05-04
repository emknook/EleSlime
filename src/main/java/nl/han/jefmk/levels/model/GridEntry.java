package nl.han.jefmk.levels.model;

public abstract class GridEntry {
    private int gridX;
    private int gridY;
    private String type;

    public GridEntry() {}

    public GridEntry(int gridX, int gridY, String type) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.type = type;
    }

    public int getGridX() { return gridX; }
    public void setGridX(int gridX) { this.gridX = gridX; }
    public int getGridY() { return gridY; }
    public void setGridY(int gridY) { this.gridY = gridY; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
