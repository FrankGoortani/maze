package excelian.maze;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static excelian.maze.Direction.*;
import static org.junit.Assert.*;

/**
 * Created by frank on 2017-07-05.
 */
public class ExplorerTest {

    Explorer explorer;
    Maze maze;
    Cell[][] cells;

    @Before
    public void setUp() throws Exception {
        maze = new Maze(8,10);
        cells = maze.getCells();
        explorer = new Explorer(1,0,5,8, cells);
    }

    @Test
    public void getExplorerLocation() throws Exception {
        assertTrue(explorer.getExplorerLocation().equals(cells[1][0]));
    }

    @Test
    public void setExplorerLocation() throws Exception {
        explorer.setExplorerLocation(cells[2][3]);//reset location
        assertTrue(explorer.getExplorerLocation().equals(cells[2][3]));

    }

    @Test
    public void move() throws Exception {
        explorer.setExplorerLocation(cells[2][3]);//reset location
        explorer.move(UP);
        assertTrue(explorer.getExplorerLocation().equals(cells[2][2]));
    }

    @Test
    public void getPossibleMoves() throws Exception {
        explorer.setExplorerLocation(cells[2][3]); //reset location
        ArrayList<Direction> directions = explorer.getPossibleMoves();
        for(Direction direction:directions){
            explorer.setExplorerLocation(cells[2][3]); //reset location
            explorer.move(direction);
            assertFalse(explorer.getExplorerLocation().wall);
        }
    }

    @Test
    public void isMazeSolvable()  throws Exception{
        explorer.draw();
        boolean isSolved = false;
        for(Cell cell:cells[5][8].neighbors){
            System.out.print(explorer.grid[cell.x*4+1][cell.y*2+1]);
            System.out.print(cell);
            isSolved = isSolved || explorer.grid[cell.x*4+1][cell.y*2+1]=='*';
        }
        assertTrue(isSolved);
    }

}