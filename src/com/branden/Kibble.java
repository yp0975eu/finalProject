package com.branden;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/* In this game, Snakes eat Kibble. Feel free to rename to SnakeFood or Prize or Treats or Cake or whatever. */


public class Kibble {

	/** Identifies a random square to display a kibble
	 * Any square is ok, so long as it doesn't have any snake segments in it. 
	 * There is only one Kibble that knows where it is on the screen.
	 * When the snake eats the kibble, it doesn't disappear and
	 * get recreated, instead it moves, and then will be drawn in the new location. 
	 */

	private int kibbleX; //This is the square number (not pixel)
	private int kibbleY;  //This is the square number (not pixel)
	private int eatingDistanceTolerance = 4;
	LinkedList<Point> allKibble = new LinkedList<>();
	public Kibble(Snake s){
		//Kibble needs to know where the snake is, so it does not create a kibble in the snake
		//Pick a random location for kibble, check if it is in the snake
		//If in snake, try again
		addKibble(s);
		//addSetKibble(s);
	}

	// add kibble to board. playing field should always have 10% of space with kibble
	protected void addKibble(Snake s){
		Random rng = new Random();
		boolean kibbleInSnake = true;
		while (kibbleInSnake == true) {
			//Generate random kibble location
			//FINDBUGS
			kibbleX = rng.nextInt(SnakeGame.getxSquares());
			kibbleY = rng.nextInt(SnakeGame.getySquares());
			kibbleInSnake = s.isSnakeSegment(kibbleX, kibbleY);
		}
		// add to front of list
		allKibble.push(new Point(kibbleX,kibbleY));
	}

	public LinkedList<Point> getAllKibble(){return allKibble;}
	// isKibble takes a point and then returns true if the point is
	// within eatingDistanceTolerance of snakeHead

	public boolean isKibble(Point snakeHead){
		for (Point p : allKibble) {
			if (p.distance(snakeHead) < eatingDistanceTolerance) {
				allKibble.remove(p);
				return true;
			}
		}
		return false;

	}
}
