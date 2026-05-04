package nl.han.jefmk.levels.model;

public class TextEntry {
    private int gridX;
    private int gridY;
    private String text;

    public TextEntry() {}

    public TextEntry(int gridX, int gridY, String text) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.text = text;
    }

    public int getGridX() { return gridX; }
    public void setGridX(int gridX) { this.gridX = gridX; }
    public int getGridY() { return gridY; }
    public void setGridY(int gridY) { this.gridY = gridY; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
