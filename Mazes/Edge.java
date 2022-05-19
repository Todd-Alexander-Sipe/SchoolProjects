public class Edge {
    private final Cell CELL_ONE;
    private final Cell CELL_TWO;
    private final boolean VERTICAL;
    private final boolean HORIZONTAL;
    private boolean tornDown;

    public Edge(Cell cellOne, Cell cellTwo,
                boolean vertical, boolean horizontal) {
        this.CELL_ONE = cellOne;
        this.CELL_TWO = cellTwo;
        this.VERTICAL = vertical;
        this.HORIZONTAL = horizontal;
        tornDown = false;
    }

    public void takeDownVerticalWall(){
        CELL_ONE.setRightWall(false);
        CELL_TWO.setLeftWall(false);
        tornDown = true;
    }

    public void takeDownHorizontalWall(){
        CELL_ONE.setDownWall(false);
        CELL_TWO.setUpWall(false);
        tornDown = true;
    }

    public boolean isTornDown() {
        return tornDown;
    }

    public Cell getCELL_ONE() {
        return CELL_ONE;
    }

    public Cell getCELL_TWO() {
        return CELL_TWO;
    }

    public boolean isVERTICAL() {
        return VERTICAL;
    }

    public boolean isHORIZONTAL() {
        return HORIZONTAL;
    }
}
