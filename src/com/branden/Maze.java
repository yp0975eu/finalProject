package com.branden;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/* Randomly add blocks to playing field. Snake cannot go through maze blocks */
public class Maze extends Gameboard{
    // number of blocks that make up the maze
    private boolean isActive;
    private int numberOfBlocks = 0;
    private int[][] mazeBlocks;
    private int blockX;
    private int blockY;
    private static final int ACTIVE = 1;

    public Maze(int maxX, int maxY, int squareSize, boolean isActive){
        super(maxX, maxY, squareSize);
        // assign local mazeBlocks reference with empty gameboard array
        mazeBlocks = emptyGameboard;
        this.isActive = isActive;
    }

    /**
     * Gets an empty space to add a random maze block to.
    * */
    protected void addBlock(Snake s){

        Random rng = new Random();
        // check if new maze block is in snake
        boolean blockInSnake = true;
        boolean blockInMaze = true;

        // both must be false to continue
        while (blockInSnake == true || blockInMaze == true) {
            //Generate random location
            //FINDBUGS
            blockX = rng.nextInt(SnakeGame.getxSquares());
            blockY = rng.nextInt(SnakeGame.getySquares());
            // test if in snake
            blockInSnake = s.isSnakeSegment(blockX, blockY);
            // test if in maze
            blockInMaze = isBlockInMaze(blockX, blockY);
        }
        // change coordinate (blockX, blockY) inside mazeBlocks to indicate an active maze block
        mazeBlocks[blockX][blockY] = ACTIVE;
        numberOfBlocks++;


    }

    // returns true if coordinates in mazeBlocks are active
    private boolean isBlockInMaze( int blockX, int blockY){
        return mazeBlocks[blockX][blockY] == ACTIVE ? true : false;
    }

    public LinkedList<Point> segmentsToDraw(){
        //Return a list of the actual x and y coordinates of the top left of each maze block
        return super.segmentsToDraw(numberOfBlocks,mazeBlocks);

    }
    public void reset() {
        // reset game board with zeros
        fillGameboardWithZeros();
        // assign gameboard reference to local variable mazeBlocks
        mazeBlocks = emptyGameboard;
    }

    // returns a single block
    public int getMazeBlock(int xCord, int yCord) {
        return mazeBlocks[xCord][yCord];
    }
    public boolean isActive(){
        return isActive;
    }
}
