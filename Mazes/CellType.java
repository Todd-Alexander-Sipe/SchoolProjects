import javafx.scene.paint.Color;

public enum CellType {
    CELL_WALL(Color.BLACK),
    CELL_WALL_PATH(Color.WHITE),
    CELL_PATH(Color.WHITE),
    CELL_PATH_BACKTRACK(Color.BLUE);

    private Color color;

    CellType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        switch(this) {
            case CELL_WALL:
                return "0";
            case CELL_WALL_PATH:
            case CELL_PATH:
                return "1";
            case CELL_PATH_BACKTRACK:
                return "2";
            default:
                return "-";
        }
    }
}
