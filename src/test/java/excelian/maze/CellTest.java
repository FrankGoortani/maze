package excelian.maze;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by frank on 2017-07-05.
 */
public class CellTest {

    private Cell cell1, cell2;

    @Before
    public void setUp() throws Exception {
        cell1 = new Cell(2,3);
        cell2 = new Cell(3,4,true);
    }

    @Test
    public void addNeighbor() throws Exception {
        cell1.addNeighbor(cell2);

        assertTrue(cell1.neighbors.contains(cell2) && cell2.neighbors.contains(cell1));
    }

    @Test
    public void isCellBelowNeighbor() throws Exception {
        assertFalse(cell1.isCellBelowNeighbor());
    }

    @Test
    public void isCellRightNeighbor() throws Exception {
        assertFalse(cell1.isCellRightNeighbor());
    }

    @Test
    public void equals() throws Exception {
        Cell cell3 = new Cell(2, 3);
        assertTrue(cell1.equals(cell3));
    }

}