package excelian.maze;

import java.util.Random;

/**
 * Created by frank on 2017-07-03.
 */
public class App {

    // run it
    public static void main(String[] args) {
        Random random = new Random(); // The random object
        int xDim = random.nextInt(20)+1;
        int yDim = random.nextInt(20)+1;
        Maze maze = new Maze(xDim, yDim);
        Explorer explorer = new Explorer(random.nextInt(xDim),random.nextInt(yDim),random.nextInt(xDim),random.nextInt(yDim), maze.getCells());
        explorer.draw();
    }
}
