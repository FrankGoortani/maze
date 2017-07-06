package excelian.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static excelian.maze.Direction.*;

/**
 * Created by frank on 2017-07-03.
 */
public class Explorer {
    public char[][] grid; // output grid
    private Cell[][] cells;
    private int dimensionX, dimensionY; // dimension of maze
    private int gridDimensionX, gridDimensionY; // dimension of output grid
    private int startX, startY, endX, endY;
    private Cell explorerLocation;

    // solve the maze starting from the start state (A-star algorithm)
    public Explorer(int startX, int startY, int endX, int endY, Cell[][] cells) {
        this.cells = cells;
        this.dimensionX = cells.length;
        this.dimensionY = cells[0].length;
        gridDimensionX = dimensionX * 4 + 1;
        gridDimensionY = dimensionY * 2 + 1;
        this.grid = new char[gridDimensionX][gridDimensionY];
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        explorerLocation = cells[startX][startY];

        // re initialize cells for path finding
        for (Cell[] cellrow : cells) {
            for (Cell cell : cellrow) {
                cell.parent = null;
                cell.visited = false;
                cell.inPath = false;
                cell.travelled = 0;
                cell.projectedDist = -1;
            }
        }
        // cells still being considered
        ArrayList<Cell> openCells = new ArrayList<>();
        // cell being considered
        Cell endCell = getCell(endX, endY);
        if (endCell == null) return; // quit if end out of bounds
        { // anonymous block to delete start, because not used later
            Cell start = getCell(startX, startY);
            if (start == null) return; // quit if start out of bounds
            start.projectedDist = getProjectedDistance(start, 0, endCell);
            start.visited = true;
            openCells.add(start);
        }
        boolean solving = true;
        while (solving) {
            if (openCells.isEmpty()) return; // quit, no path
            // sort openCells according to least projected distance
            Collections.sort(openCells, new Comparator<Cell>(){
                @Override
                public int compare(Cell cell1, Cell cell2) {
                    double diff = cell1.projectedDist - cell2.projectedDist;
                    if (diff > 0) return 1;
                    else if (diff < 0) return -1;
                    else return 0;
                }
            });
            Cell current = openCells.remove(0); // pop cell least projectedDist
            if (current == endCell) break; // at end
            for (Cell neighbor : current.neighbors) {
                double projDist = getProjectedDistance(neighbor,
                        current.travelled + 1, endCell);
                if (!neighbor.visited || // not visited yet
                        projDist < neighbor.projectedDist) { // better path
                    neighbor.parent = current;
                    neighbor.visited = true;
                    neighbor.projectedDist = projDist;
                    neighbor.travelled = current.travelled + 1;
                    if (!openCells.contains(neighbor))
                        openCells.add(neighbor);
                }
            }
        }
        // create path from end to beginning
        Cell backtracking = endCell;
        backtracking.inPath = true;
        while (backtracking.parent != null) {
            backtracking = backtracking.parent;
            backtracking.inPath = true;
        }
    }
    // get the projected distance
    // (A star algorithm consistent)
    private double getProjectedDistance(Cell current, double travelled, Cell end) {
        return travelled + Math.abs(current.x - end.x) +
                Math.abs(current.y - current.x);
    }

    // used to get a Cell at x, y; returns null out of bounds
    private Cell getCell(int x, int y) {
        try {
            return this.cells[x][y];
        } catch (ArrayIndexOutOfBoundsException e) { // catch out of bounds
            return null;
        }
    }

    // draw the maze
    private void updateGrid() {
        char backChar = ' ', wallChar = 'X', cellChar = ' ', pathChar = '*', pathStart = 'S', pathFinish = 'F';
        // fill background
        for (int x = 0; x < gridDimensionX; x ++) {
            for (int y = 0; y < gridDimensionY; y ++) {
                grid[x][y] = backChar;
            }
        }
        // build walls
        for (int x = 0; x < gridDimensionX; x ++) {
            for (int y = 0; y < gridDimensionY; y ++) {
                if (x % 4 == 0 || y % 2 == 0)
                    grid[x][y] = wallChar;
            }
        }
        // make meaningful representation
        for (int x = 0; x < dimensionX; x++) {
            for (int y = 0; y < dimensionY; y++) {
                Cell current = getCell(x, y);
                int gridX = x * 4 + 2, gridY = y * 2 + 1;
                if (current.inPath) {
                    grid[gridX][gridY] = pathChar;
                    if (current.isCellBelowNeighbor())
                        if (getCell(x, y + 1).inPath) {
                            grid[gridX][gridY + 1] = pathChar;
                            grid[gridX + 1][gridY + 1] = backChar;
                            grid[gridX - 1][gridY + 1] = backChar;
                        } else {
                            grid[gridX][gridY + 1] = cellChar;
                            grid[gridX + 1][gridY + 1] = backChar;
                            grid[gridX - 1][gridY + 1] = backChar;
                        }
                    if (current.isCellRightNeighbor())
                        if (getCell(x + 1, y).inPath) {
                            grid[gridX + 2][gridY] = pathChar;
                            grid[gridX + 1][gridY] = pathChar;
                            grid[gridX + 3][gridY] = pathChar;
                        } else {
                            grid[gridX + 2][gridY] = cellChar;
                            grid[gridX + 1][gridY] = cellChar;
                            grid[gridX + 3][gridY] = cellChar;
                        }
                } else {
                    grid[gridX][gridY] = cellChar;
                    if (current.isCellBelowNeighbor()) {
                        grid[gridX][gridY + 1] = cellChar;
                        grid[gridX + 1][gridY + 1] = backChar;
                        grid[gridX - 1][gridY + 1] = backChar;
                    }
                    if (current.isCellRightNeighbor()) {
                        grid[gridX + 2][gridY] = cellChar;
                        grid[gridX + 1][gridY] = cellChar;
                        grid[gridX + 3][gridY] = cellChar;
                    }
                }
            }
        }

        //set start and end
        grid[startX*4+1][startY*2+1] = pathStart;
        grid[endX*4+1][endY*2+1] = pathFinish;

    }

    // simply prints the map
    public void draw() {
        System.out.print(this);
    }
    // forms a meaningful representation
    @Override
    public String toString() {
        updateGrid();
        String output = "";
        for (int y = 0; y < gridDimensionY; y++) {
            for (int x = 0; x < gridDimensionX; x++) {
                output += grid[x][y];
            }
            output += "\n";
        }
        return output;
    }

    public Cell getExplorerLocation() {
        return explorerLocation;
    }

    public void setExplorerLocation(Cell explorerLocation) {
        this.explorerLocation = explorerLocation;
    }

    public void move(Direction direction){
        switch (direction){
            case UP:
                explorerLocation =  cells[explorerLocation.x][explorerLocation.y - 1];
                break;
            case DOWN:
                explorerLocation =  cells[explorerLocation.x][explorerLocation.y + 1];
                break;
            case LEFT:
                explorerLocation =  cells[explorerLocation.x - 1][explorerLocation.y];
                break;
            case RIGHT:
                explorerLocation =  cells[explorerLocation.x + 1][explorerLocation.y];
        }
    }

    public ArrayList<Direction> getPossibleMoves(){
        ArrayList<Direction> directions=new ArrayList<>();
        ArrayList<Cell> neighbors = explorerLocation.neighbors;
        for(Cell cell:neighbors){
            if (!cell.wall) {
                if (cell.equals(cells[explorerLocation.x][explorerLocation.y - 1]))
                    directions.add(UP);
                if (cell.equals(cells[explorerLocation.x][explorerLocation.y + 1]))
                    directions.add(DOWN);
                if (cell.equals(cells[explorerLocation.x - 1][explorerLocation.y]))
                    directions.add(LEFT);
                if (cell.equals(cells[explorerLocation.x + 1][explorerLocation.y]))
                    directions.add(RIGHT);
            }
        }
        return directions;
    }
}
