public class Direction {
    private String direction;

    public Direction(Cell one, Cell two) {
        if (one.getCELL_ID() + 1 == two.getCELL_ID()){
            direction = "east";
        }
        else if (one.getCELL_ID() - 1 == two.getCELL_ID()){
            direction = "west";
        }
        else if (one.getCELL_ID() < two.getCELL_ID()){
            direction = "south";
        }
        else if (one.getCELL_ID() > two.getCELL_ID()){
            direction = "north";
        }
    }

    public String getDirection() {
        return direction;
    }
}
