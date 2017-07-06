package excelian.maze;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by frank on 2017-07-05.
 */
public class MazeTest {

    Maze maze1;
    Maze maze2;

    @Before
    public void setUp() throws Exception {
        maze1 = new Maze(5);
        maze2 = new Maze(7,10);
    }

    @Test
    public void getCells() throws Exception {
        assertEquals(maze1.getCells().length, 5);
        assertEquals(maze1.getCells()[0].length, 5);
    }

    @Test //no need the exception is captured (expected=Exception.class)
    public void getCell() throws Exception {
        assertEquals(maze2.getCell(2, 3).x, 2);
        assertEquals(maze2.getCell(2, 3).y, 3);
        assertEquals(maze2.getCell(10, 10), null);
    }

}