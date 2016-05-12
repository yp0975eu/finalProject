package com.branden;

import java.awt.*;
import java.util.LinkedList;

/**
    Removed fillGameboardWithZeros from Snake to create this class
    because need to reuse empty gameboard to create maze
 */
public class Gameboard {

    protected int maxX;
    protected int maxY;
    protected int squareSize;
    protected int emptyGameboard[][];

    Gameboard(int maxX, int maxY, int squareSize){
        this.maxX = maxX;
        this.maxY = maxY;
        this.squareSize = squareSize;
        emptyGameboard = new int[maxX][maxY];
        fillGameboardWithZeros();
    }
    protected void fillGameboardWithZeros() {
        for (int x = 0; x < maxX; x++){
            for (int y = 0 ; y < maxY ; y++) {
                emptyGameboard[x][y] = 0;
            }
        }
    }
    public LinkedList<Point> segmentsToDraw(int numberOfBlocks, int[][] searchArray){
        //Return a list of the actual x and y coordinates of the top left of each snake segment
        //Useful for the Panel class to draw the snake
        LinkedList<Point> segmentCoordinates = new LinkedList<Point>();
        for (int segment = 1 ; segment <= numberOfBlocks ; segment++ ) {
            //search array for each segment number
            for (int x = 0 ; x < maxX ; x++) {
                for (int y = 0 ; y < maxY ; y++) {
                    if (searchArray[x][y] == segment){
                        //make a Point for this segment's coordinates and add to list
                        Point p = new Point(x , y);
                        segmentCoordinates.add(p);
                    }
                }
            }
        }
        return segmentCoordinates;

    }
}
