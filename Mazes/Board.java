import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class Board {

    private final int BOARD_SIZE;
    private final Cell[][] BOARD;
    private ArrayList<Integer> actionList = new ArrayList<>();
    private ArrayList<Integer> solverLocationList = new ArrayList<>();
    Stack<Cell> pathStack = new Stack<>();
    private int startCell;
    private int finishCell;

    public Board(int boardSize, int cellSize) {
        BOARD = new Cell[boardSize][boardSize];
        this.BOARD_SIZE = boardSize;
        int count = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                BOARD[i][j] = new Cell(0, i, j, count, cellSize);
                count++;
            }
        }
    }

    public ArrayList<Integer> determineStartAndFinish(){
        ArrayList<Integer> startFinish = new ArrayList<>();
        Random rand1 = new Random();
        startCell = rand1.nextInt(BOARD_SIZE);
        Random rand2 = new Random();
        finishCell = rand2.nextInt(BOARD_SIZE) +
                (BOARD_SIZE * BOARD_SIZE - BOARD_SIZE);
        System.out.println("Start cell: " + startCell);
        System.out.println("Finish cell: " + finishCell);
        startFinish.add(startCell);
        startFinish.add(finishCell);
        getCellFromID(startCell).setStartCell();
        getCellFromID(finishCell).setFinishCell();
        return startFinish;
    }

    public Cell getCell(int row, int col){
        return BOARD[row][col];
    }

    public Cell getCellFromID(int cellID){
        if (cellID == 0){
            return BOARD[0][0];
        }
        else{
            return BOARD[cellID/BOARD_SIZE][cellID%BOARD_SIZE];
        }
    }

    public void depthFirstSearch(String solver){
        Cell c = BOARD[0][0];
        pathStack.push(c);
        c.visit();
        ArrayList<Cell> neighbors;
        neighbors = getNeighborsBreakWalls(c);
        Collections.shuffle(neighbors);
        for (Cell neighbor : neighbors) {
            pathStack.push(neighbor);
            neighbor.visit();
        }
        neighbors.clear();
        while(!pathStack.empty()){
            c = pathStack.pop();
            neighbors = getNeighborsBreakWalls(c);
            Collections.shuffle(neighbors);
            for (Cell neighbor : neighbors) {
                pathStack.push(neighbor);
                neighbor.visit();
            }
            neighbors.clear();
        }
        chooseSolver(solver);
    }

    public void kruskal(String solver){
        ArrayList<Edge> edges = getAllEdges();
        Collections.shuffle(edges);
        kruskalHelper(edges, solver);
    }

    public void kruskalHelper(ArrayList<Edge> edges, String solver){
        UnionFind uf = new UnionFind(BOARD_SIZE * BOARD_SIZE);
        while(!edges.isEmpty()){
            if (!uf.connected(edges.get(0).getCELL_ONE().getCELL_ID(),
                    edges.get(0).getCELL_TWO().getCELL_ID())){
                uf.union(edges.get(0).getCELL_ONE().getCELL_ID(),
                        edges.get(0).getCELL_TWO().getCELL_ID());
                if (edges.get(0).isHORIZONTAL()){
                    edges.get(0).takeDownHorizontalWall();
                    edges.get(0).getCELL_ONE().visit();
                    edges.get(0).getCELL_TWO().visit();
                    addCellIDToActions(edges.get(0).getCELL_ONE());
                    addCellIDToActions(edges.get(0).getCELL_TWO());
                }
                if (edges.get(0).isVERTICAL()){
                    edges.get(0).takeDownVerticalWall();
                    edges.get(0).getCELL_ONE().visit();
                    edges.get(0).getCELL_TWO().visit();
                    addCellIDToActions(edges.get(0).getCELL_ONE());
                    addCellIDToActions(edges.get(0).getCELL_TWO());
                }
                edges.remove(0);
            }
            else{
                edges.remove(0);
            }
        }
        chooseSolver(solver);
    }

    public void prim(String solver){
        ArrayList<String> directionChoices = new ArrayList<>();
        String direction;
        Cell c = BOARD[0][0];
        c.visit();
        ArrayList<Cell> neighbors = getNeighbors(c);
        while(!neighbors.isEmpty()){
            Collections.shuffle(neighbors);
            c = neighbors.get(0);
            c.visit();
            neighbors.remove(0);
            ArrayList<Cell> temp = getNeighbors(c);
            for (Cell cell : temp) {
                if (!neighbors.contains(cell)) {
                    neighbors.add(cell);
                }
            }
            if (c.getROW() > 0){ //check up
                if (BOARD[c.getROW() - 1][c.getCOL()].isVisited()){
                    directionChoices.add("north");
                }
            }
            if (c.getROW() < BOARD_SIZE - 1){ //check down
                if (BOARD[c.getROW() + 1][c.getCOL()].isVisited()){
                    directionChoices.add("south");
                }
            }
            if (c.getCOL() > 0){ //check left
                if (BOARD[c.getROW()][c.getCOL() - 1].isVisited()){
                    directionChoices.add("west");
                }

            }
            if (c.getCOL() < BOARD_SIZE - 1){ //check right
                if (BOARD[c.getROW()][c.getCOL() + 1].isVisited()){
                    directionChoices.add("east");
                }
            }
            Collections.shuffle(directionChoices);
            direction = directionChoices.get(0);
            if (direction.equals("north")){
                c.setUpWall(false);
                BOARD[c.getROW() - 1][c.getCOL()].setDownWall(false);
                addCellIDToActions(c);
                addCellIDToActions(BOARD[c.getROW() - 1][c.getCOL()]);
            }
            if (direction.equals("south")){
                c.setDownWall(false);
                BOARD[c.getROW() + 1][c.getCOL()].setUpWall(false);
                addCellIDToActions(c);
                addCellIDToActions(BOARD[c.getROW() + 1][c.getCOL()]);
            }
            if (direction.equals("west")){
                c.setLeftWall(false);
                BOARD[c.getROW()][c.getCOL() - 1].setRightWall(false);
                addCellIDToActions(c);
                addCellIDToActions(BOARD[c.getROW()][c.getCOL() - 1]);
            }
            if (direction.equals("east")){
                c.setRightWall(false);
                BOARD[c.getROW()][c.getCOL() + 1].setLeftWall(false);
                addCellIDToActions(c);
                addCellIDToActions(BOARD[c.getROW()][c.getCOL() + 1]);
            }
            directionChoices.clear();
        }
        chooseSolver(solver);
    }

    public ArrayList<Edge> getAllEdges(){
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < (BOARD_SIZE); i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (j < BOARD_SIZE - 1){ //add to right
                    edges.add(new Edge(BOARD[i][j], BOARD[i][j + 1],
                            true, false));
                }
                if (i < BOARD_SIZE - 1){
                    edges.add(new Edge(BOARD[i][j], BOARD[i + 1][j],
                            false, true));
                }
            }
        }
        return edges;
    }

    public ArrayList<Cell> getNeighbors(Cell c){
        ArrayList<Cell> neighbors = new ArrayList<>();
        if (c.getROW() > 0){ //check up
            if (!BOARD[c.getROW() - 1][c.getCOL()].isVisited()){
                neighbors.add(BOARD[c.getROW() - 1][c.getCOL()]);
            }
        }
        if (c.getROW() < (BOARD_SIZE - 1)){ //check down
            if (!BOARD[c.getROW() + 1][c.getCOL()].isVisited()){
                neighbors.add(BOARD[c.getROW() + 1][c.getCOL()]);
            }
        }
        if (c.getCOL() > 0){ //check left
            if (!BOARD[c.getROW()][c.getCOL() - 1].isVisited()){
                neighbors.add(BOARD[c.getROW()][c.getCOL() - 1]);
            }
        }
        if (c.getCOL() < (BOARD_SIZE - 1)){ //check right
            if (!BOARD[c.getROW()][c.getCOL() + 1].isVisited()){
                neighbors.add(BOARD[c.getROW()][c.getCOL() + 1]);
            }
        }
        return neighbors;
    }

    public ArrayList<Cell> getNeighborsBreakWalls(Cell c){
        ArrayList<Cell> neighbors = new ArrayList<>();
        if (c.getROW() > 0){ //check up
            if (!BOARD[c.getROW() - 1][c.getCOL()].isVisited()){
                neighbors.add(BOARD[c.getROW() - 1][c.getCOL()]);
                c.setUpWall(false);
                BOARD[c.getROW() - 1][c.getCOL()].setDownWall(false);
                addCellIDToActions(c);
                addCellIDToActions(BOARD[c.getROW() - 1][c.getCOL()]);
            }
        }
        if (c.getROW() < (BOARD_SIZE - 1)){ //check down
            if (!BOARD[c.getROW() + 1][c.getCOL()].isVisited()){
                neighbors.add(BOARD[c.getROW() + 1][c.getCOL()]);
                c.setDownWall(false);
                BOARD[c.getROW() + 1][c.getCOL()].setUpWall(false);
                addCellIDToActions(c);
                addCellIDToActions(BOARD[c.getROW() + 1][c.getCOL()]);
            }
        }
        if (c.getCOL() > 0){ //check left
            if (!BOARD[c.getROW()][c.getCOL() - 1].isVisited()){
                neighbors.add(BOARD[c.getROW()][c.getCOL() - 1]);
                c.setLeftWall(false);
                BOARD[c.getROW()][c.getCOL() - 1].setRightWall(false);
                addCellIDToActions(c);
                addCellIDToActions(BOARD[c.getROW()][c.getCOL() - 1]);
            }
        }
        if (c.getCOL() < (BOARD_SIZE - 1)){ //check right
            if (!BOARD[c.getROW()][c.getCOL() + 1].isVisited()){
                neighbors.add(BOARD[c.getROW()][c.getCOL() + 1]);
                c.setRightWall(false);
                BOARD[c.getROW()][c.getCOL() + 1].setLeftWall(false);
                addCellIDToActions(c);
                addCellIDToActions(BOARD[c.getROW()][c.getCOL() + 1]);
            }
        }
        return neighbors;
    }

    public void printBoard(){
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                BOARD[i][j].printCellValue();
            }
            System.out.println();
        }
    }

    public void printBoardID(){
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                BOARD[i][j].printCellID();
            }
            System.out.println();
        }
    }

    public void chooseSolver(String solver){
        if (solver.equals("mouse")){
            System.out.println("Solver type chosen is: " + solver);
            mouse();
        }
        if (solver.equals("mouse_thread")){
            System.out.println("Solver type chosen is: " + solver);
            mouseThread();
        }
        if (solver.equals("wall")){
            System.out.println("Solver type chosen is: " + solver);
            wall();
        }
        if (solver.equals("wall_thread")){
            System.out.println("Solver type chosen is: " + solver);
        }
        if (solver.equals("deadend")){
            System.out.println("Solver type chosen is: " + solver);
            deadEndFilling();
        }
    }

    public void mouse() {
        String directionTravelled = "down";
        String directionTravelling;
        ArrayList<String> openTravelOptions = new ArrayList<>();
        Cell c = getCellFromID(startCell);
        addSolverLocation(c);

        while (c.getCELL_ID() != finishCell) {
            openTravelOptions = mouseHelper(c);
            Collections.shuffle(openTravelOptions);
            if (openTravelOptions.size() == 1) {
                directionTravelling = openTravelOptions.get(0);
                if (directionTravelling.equals("up")) {
                    c = BOARD[c.getROW() - 1][c.getCOL()];
                    directionTravelled = "up";
                    addSolverLocation(c);
                }
                if (directionTravelling.equals("right")) {
                    c = BOARD[c.getROW()][c.getCOL() + 1];
                    directionTravelled = "right";
                    addSolverLocation(c);
                }
                if (directionTravelling.equals("down")) {
                    c = BOARD[c.getROW() + 1][c.getCOL()];
                    directionTravelled = "down";
                    addSolverLocation(c);
                }
                if (directionTravelling.equals("left")) {
                    c = BOARD[c.getROW()][c.getCOL() - 1];
                    directionTravelled = "left";
                    addSolverLocation(c);
                }
            }
            if (openTravelOptions.size() > 1) {
                if (directionTravelled.equals("down") && openTravelOptions.get(0).equals("up")) {
                    openTravelOptions.remove(0);
                    directionTravelling = openTravelOptions.get(0);
                }
                if (directionTravelled.equals("up") && openTravelOptions.get(0).equals("down")){
                    openTravelOptions.remove(0);
                    directionTravelling = openTravelOptions.get(0);
                }
                if (directionTravelled.equals("right") && openTravelOptions.get(0).equals("left")){
                    openTravelOptions.remove(0);
                    directionTravelling = openTravelOptions.get(0);
                }
                if (directionTravelled.equals("left") && openTravelOptions.get(0).equals("right")){
                    openTravelOptions.remove(0);
                    directionTravelling = openTravelOptions.get(0);
                }
                directionTravelling = openTravelOptions.get(0);
                if (directionTravelling.equals("up")) {
                    c = BOARD[c.getROW() - 1][c.getCOL()];
                    directionTravelled = "up";
                    addSolverLocation(c);
                }
                if (directionTravelling.equals("right")) {
                    c = BOARD[c.getROW()][c.getCOL() + 1];
                    directionTravelled = "right";
                    addSolverLocation(c);
                }
                if (directionTravelling.equals("down")) {
                    c = BOARD[c.getROW() + 1][c.getCOL()];
                    directionTravelled = "down";
                    addSolverLocation(c);
                }
                if (directionTravelling.equals("left")) {
                    c = BOARD[c.getROW()][c.getCOL() - 1];
                    directionTravelled = "left";
                    addSolverLocation(c);
                }
            }
            openTravelOptions.clear();
        }
    }

    public ArrayList<String> mouseHelper(Cell c){
        ArrayList<String> openTravelOptions = new ArrayList<>();
        if (!c.isUpWall()){
            openTravelOptions.add("up");
        }
        if (!c.isRightWall()){
            openTravelOptions.add("right");
        }
        if (!c.isDownWall()){
            openTravelOptions.add("down");
        }
        if (!c.isLeftWall()){
            openTravelOptions.add("left");
        }
        return openTravelOptions;
    }

    public void mouseThread(){
        Cell c = getCellFromID(startCell);
        c.setSolverVisited();
        ArrayList<Cell> neighbors = getNeighbors(c);

    }

    public void wall(){
        //mouse enters maze from above the top left cell
        Cell c = getCellFromID(startCell);
        addSolverLocation(c);
        String currentDirection = "south";
        while (c.getCELL_ID() != finishCell){
            switch (currentDirection) {
                case "south":
                    if (!c.isLeftWall()) {
                        c = BOARD[c.getROW()][c.getCOL() - 1];
                        currentDirection = "west";
                        break;
                    } else if (!c.isDownWall()) {
                        c = BOARD[c.getROW() + 1][c.getCOL()];
                        currentDirection = "south";
                        break;
                    } else if (!c.isRightWall()) {
                        c = BOARD[c.getROW()][c.getCOL() + 1];
                        currentDirection = "east";
                        break;
                    } else if (!c.isUpWall()) {
                        c = BOARD[c.getROW() - 1][c.getCOL()];
                        currentDirection = "north";
                        break;
                    }
                    break;
                case "east":
                    if (!c.isDownWall()) {
                        c = BOARD[c.getROW() + 1][c.getCOL()];
                        currentDirection = "south";
                        break;
                    } else if (!c.isRightWall()) {
                        c = BOARD[c.getROW()][c.getCOL() + 1];
                        currentDirection = "east";
                        break;
                    } else if (!c.isUpWall()) {
                        c = BOARD[c.getROW() - 1][c.getCOL()];
                        currentDirection = "north";
                        break;
                    } else if (!c.isLeftWall()) {
                        c = BOARD[c.getROW()][c.getCOL() - 1];
                        currentDirection = "west";
                        break;
                    }
                    break;
                case "north":
                    if (!c.isRightWall()) {
                        c = BOARD[c.getROW()][c.getCOL() + 1];
                        currentDirection = "east";
                        break;
                    } else if (!c.isUpWall()) {
                        c = BOARD[c.getROW() - 1][c.getCOL()];
                        currentDirection = "north";
                        break;
                    } else if (!c.isLeftWall()) {
                        c = BOARD[c.getROW()][c.getCOL() - 1];
                        currentDirection = "west";
                        break;
                    } else if (!c.isDownWall()) {
                        c = BOARD[c.getROW() + 1][c.getCOL()];
                        currentDirection = "south";
                        break;
                    }
                    break;
                case "west":
                    if (!c.isUpWall()) {
                        c = BOARD[c.getROW() - 1][c.getCOL()];
                        currentDirection = "north";
                        break;
                    } else if (!c.isLeftWall()) {
                        c = BOARD[c.getROW()][c.getCOL() - 1];
                        currentDirection = "west";
                        break;
                    } else if (!c.isDownWall()) {
                        c = BOARD[c.getROW() + 1][c.getCOL()];
                        currentDirection = "south";
                        break;
                    } else if (!c.isRightWall()) {
                        c = BOARD[c.getROW()][c.getCOL() + 1];
                        currentDirection = "east";
                        break;
                    }
                    break;
            }
            addSolverLocation(c);
        }
        //mouse finished, leaves the maze below the bottom right cell
        currentDirection = "north";
        while (c.getCELL_ID() != startCell){
            switch (currentDirection) {
                case "south":
                    if (!c.isLeftWall()) {
                        c = BOARD[c.getROW()][c.getCOL() - 1];
                        currentDirection = "west";
                        break;
                    } else if (!c.isDownWall()) {
                        c = BOARD[c.getROW() + 1][c.getCOL()];
                        currentDirection = "south";
                        break;
                    } else if (!c.isRightWall()) {
                        c = BOARD[c.getROW()][c.getCOL() + 1];
                        currentDirection = "east";
                        break;
                    } else if (!c.isUpWall()) {
                        c = BOARD[c.getROW() - 1][c.getCOL()];
                        currentDirection = "north";
                        break;
                    }
                    break;
                case "east":
                    if (!c.isDownWall()) {
                        c = BOARD[c.getROW() + 1][c.getCOL()];
                        currentDirection = "south";
                        break;
                    } else if (!c.isRightWall()) {
                        c = BOARD[c.getROW()][c.getCOL() + 1];
                        currentDirection = "east";
                        break;
                    } else if (!c.isUpWall()) {
                        c = BOARD[c.getROW() - 1][c.getCOL()];
                        currentDirection = "north";
                        break;
                    } else if (!c.isLeftWall()) {
                        c = BOARD[c.getROW()][c.getCOL() - 1];
                        currentDirection = "west";
                        break;
                    }
                    break;
                case "north":
                    if (!c.isRightWall()) {
                        c = BOARD[c.getROW()][c.getCOL() + 1];
                        currentDirection = "east";
                        break;
                    } else if (!c.isUpWall()) {
                        c = BOARD[c.getROW() - 1][c.getCOL()];
                        currentDirection = "north";
                        break;
                    } else if (!c.isLeftWall()) {
                        c = BOARD[c.getROW()][c.getCOL() - 1];
                        currentDirection = "west";
                        break;
                    } else if (!c.isDownWall()) {
                        c = BOARD[c.getROW() + 1][c.getCOL()];
                        currentDirection = "south";
                        break;
                    }
                    break;
                case "west":
                    if (!c.isUpWall()) {
                        c = BOARD[c.getROW() - 1][c.getCOL()];
                        currentDirection = "north";
                        break;
                    } else if (!c.isLeftWall()) {
                        c = BOARD[c.getROW()][c.getCOL() - 1];
                        currentDirection = "west";
                        break;
                    } else if (!c.isDownWall()) {
                        c = BOARD[c.getROW() + 1][c.getCOL()];
                        currentDirection = "south";
                        break;
                    } else if (!c.isRightWall()) {
                        c = BOARD[c.getROW()][c.getCOL() + 1];
                        currentDirection = "east";
                        break;
                    }
                    break;
            }
            addSolverLocation(c);
        }
    }

    public void deadEndFilling(){
        //find start locations for threads:
        for (int i = 0; i < (BOARD_SIZE * BOARD_SIZE); i++) {
            Cell c = getCellFromID(i);
            ArrayList<Cell> neighbors = getNeighbors(c);
            if (neighbors.size() == 1){
                neighbors.get(0).setDoNotTravelHere();
            }
            neighbors.clear();
        }
    }

    public void addCellIDToActions(Cell c){
        actionList.add(c.getCELL_ID());
    }

    public ArrayList<Integer> getActionList(){
        return actionList;
    }

    public void printActions(){
        System.out.println(actionList);
    }

    public void addSolverLocation(Cell c){
        solverLocationList.add(c.getCELL_ID());
    }

    public ArrayList<Integer> getSolverLocationList(){
        return solverLocationList;
    }

    public void printSolverLocationList(){
        System.out.println(solverLocationList);
    }

}

