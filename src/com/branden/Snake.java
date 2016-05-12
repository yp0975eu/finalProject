package com.branden;

import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;

public class Snake extends Gameboard implements Serializable {
	//FINDBUGS
	// changed to static because these belong to the class,
	// private to prevent unauthorized access,
	// final to prevent being written to.
	private static final int DIRECTION_UP = 0;
	private static final int DIRECTION_DOWN = 1;
	private static final int DIRECTION_LEFT = 2;
	private static final int DIRECTION_RIGHT = 3;  //These are completely arbitrary numbers.

	private boolean hitWall = false;
	private boolean ateTail = false;

	private int snakeSquares[][];  //represents all of the squares on the screen
	//NOT pixels!
	//A 0 means there is no part of the snake in this square
	//A non-zero number means part of the snake is in the square
	//The head of the snake is 1, rest of segments are numbered in order

	private int currentHeading;  //Direction snake is going in, ot direction user is telling snake to go
	private int lastHeading;    //Last confirmed movement of snake. See moveSnake method
	
	private int snakeSize;   //size of snake - how many segments?

	private int growthIncrement = 2; //how many squares the snake grows after it eats a kibble

	private int justAteMustGrowThisMuch = 0;
	// BA: Moved to superclass
	//private int maxX, maxY, squareSize;
	private int snakeHeadX, snakeHeadY, startHeadX, startHeadY; //store coordinates of head - first segment

	public Snake(int maxX, int maxY, int squareSize){
		super(maxX, maxY,squareSize);
		//assign emptyGameboard reference to the local snakeSquares array
		snakeSquares = emptyGameboard;
		createStartSnake();
	}

	protected void createStartSnake(){

		// TODO randomize start position, and at the same time always center snake in viewport
		//snake starts as 3 horizontal squares in the center of the screen, moving left
		Random random = new Random();

		//TODO if screen center is less than viewport padding then get a new start position.

		int startPositionX = (int) (random.nextInt(maxX)/2);  //Cast just in case we have an odd number
		int startPositionY = (int) (random.nextInt(maxY)/2);  //Cast just in case we have an odd number

		snakeSquares[startPositionX][startPositionY] = 1;
		snakeSquares[startPositionX+1][startPositionY] = 2;
		snakeSquares[startPositionX+2][startPositionY] = 3;

		// BA: initialize the snake head, size, heading justAteMustGrowThisMuch
		startHeadX = snakeHeadX = startPositionX;
		startHeadY = snakeHeadY = startPositionY;

		snakeSize = 3;

		currentHeading = DIRECTION_LEFT;
		lastHeading = DIRECTION_LEFT;
		
		justAteMustGrowThisMuch = 0;
	}

	public LinkedList<Point> segmentsToDraw(){
		//Return a list of the actual x and y coordinates of the top left of each snake segment
		//Useful for the Panel class to draw the snake
		return super.segmentsToDraw(snakeSize,snakeSquares);

	}

	public void snakeUp(){
		if (currentHeading == DIRECTION_UP || currentHeading == DIRECTION_DOWN) { return; }
		currentHeading = DIRECTION_UP;
	}
	public void snakeDown(){
		if (currentHeading == DIRECTION_DOWN || currentHeading == DIRECTION_UP) { return; }
		currentHeading = DIRECTION_DOWN;
	}
	public void snakeLeft(){
		if (currentHeading == DIRECTION_LEFT || currentHeading == DIRECTION_RIGHT) { return; }
		currentHeading = DIRECTION_LEFT;
	}
	public void snakeRight(){
		if (currentHeading == DIRECTION_RIGHT || currentHeading == DIRECTION_LEFT) { return; }
		currentHeading = DIRECTION_RIGHT;
	}

	//	public void	eatKibble(){
	//		//record how much snake needs to grow after eating food
	//		justAteMustGrowThisMuch += growthIncrement;
	//	}

	// BA: needs maze parameter to test if it has hit the maze
	protected void moveSnake(Maze maze){
		//Called every clock tick
		//System.out.printf("(%d,%d)\n",snakeHeadX,snakeHeadY);
		//Must check that the direction snake is being sent in is not contrary to current heading
		//So if current heading is down, and snake is being sent up, then should ignore.
		//Without this code, if the snake is heading up, and the user presses left then down quickly, the snake will back into itself.
		if (currentHeading == DIRECTION_DOWN && lastHeading == DIRECTION_UP) {
			currentHeading = DIRECTION_UP; //keep going the same way
		}
		if (currentHeading == DIRECTION_UP && lastHeading == DIRECTION_DOWN) {
			currentHeading = DIRECTION_DOWN; //keep going the same way
		}
		if (currentHeading == DIRECTION_LEFT && lastHeading == DIRECTION_RIGHT) {
			currentHeading = DIRECTION_RIGHT; //keep going the same way
		}
		if (currentHeading == DIRECTION_RIGHT && lastHeading == DIRECTION_LEFT) {
			currentHeading = DIRECTION_LEFT; //keep going the same way
		}

		//Did you hit the wall, snake? 
		//Or eat your tail? Don't move.
		if (isGameOver()) {
			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
			return;
		}

		if (wonGame()) {
			SnakeGame.setGameStage(SnakeGame.GAME_WON);
			return;
		}

		//Use snakeSquares array, and current heading, to move snake

		//Put a 1 in new snake head square
		//increase all other snake segments by 1
		//set tail to 0 if snake did not just eat
		//Otherwise leave tail as is until snake has grown the correct amount 

		//Find the head of the snake - snakeHeadX and snakeHeadY

		//Increase all snake segments by 1
		//All non-zero elements of array represent a snake segment

		for (int x = 0 ; x < maxX ; x++) {
			for (int y = 0 ; y < maxY ; y++){
				if (snakeSquares[x][y] != 0) {
					snakeSquares[x][y]++;
				}
			}
		}

		//now identify where to add new snake head
		if (currentHeading == DIRECTION_UP) {		
			//Subtract 1 from Y coordinate so head is one square up
			snakeHeadY-- ;
		}
		if (currentHeading == DIRECTION_DOWN) {		
			//Add 1 to Y coordinate so head is 1 square down
			snakeHeadY++ ;
		}
		if (currentHeading == DIRECTION_LEFT) {		
			//Subtract 1 from X coordinate so head is 1 square to the left
			snakeHeadX -- ;
		}
		if (currentHeading == DIRECTION_RIGHT) {		
			//Add 1 to X coordinate so head is 1 square to the right
			snakeHeadX ++ ;
		}


		if ( SnakeGame.useWarp() ) {
			// BA: IF SNAKE HITS WALL THEN WARP TO NEXT SIDE OF SCREEN
			// if it's reached the max or min, then set it to the opposite.
			if (snakeHeadX >= maxX) {
				snakeHeadX = 0;
			} else if (snakeHeadX < 0) {
				// BA: maxX - 1, because we don't want the snake to be offscreen
				snakeHeadX = maxX - 1;
			} else if (snakeHeadY >= maxY) {
				snakeHeadY = 0;
			} else if (snakeHeadY < 0) {
				// BA: again, we don't wan the snake to be offscreen
				snakeHeadY = maxY - 1;
			}
		} else{
			// BA: IF THE SNAKE HITS WALL, GAME OVER
			//Does this make snake hit the wall?
			if (snakeHeadX >= maxX || snakeHeadX < 0 || snakeHeadY >= maxY || snakeHeadY < 0 ) {
				hitWall = true;
				SnakeGame.setGameStage(SnakeGame.GAME_OVER);
				return;
			}
		}

		//Does this make the snake eat its tail?
		// BA: did ths snake hit the maze?
		// if the maze is inactive then all the blocks will equal 0

		if (snakeSquares[snakeHeadX][snakeHeadY] != 0 || maze.getMazeBlock(snakeHeadX, snakeHeadY) != 0 ) {
			ateTail = true;
			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
			return;
		}

		//Otherwise, game is still on. Add new head
		snakeSquares[snakeHeadX][snakeHeadY] = 1; 

		//If snake did not just eat, then remove tail segment
		//to keep snake the same length.
		//find highest number, which should now be the same as snakeSize+1, and set to 0
		
		if (justAteMustGrowThisMuch == 0) {
			for (int x = 0 ; x < maxX ; x++) {
				for (int y = 0 ; y < maxY ; y++){
					if (snakeSquares[x][y] == snakeSize+1) {
						snakeSquares[x][y] = 0;
					}
				}
			}
		}
		else {
			//Snake has just eaten. leave tail as is.  Decrease justAteMustGrowThisMuch variable by 1.
			justAteMustGrowThisMuch -- ;
			snakeSize ++;
		}
		
		lastHeading = currentHeading; //Update last confirmed heading

	}


	public boolean isSnakeSegment(int kibbleX, int kibbleY) {
		if (snakeSquares[kibbleX][kibbleY] == 0) {
			return false;
		}
		return true;
	}

	public boolean didEatKibble(Kibble kibble) {
		// Is this kibble in the snake?
		// It should be in the same square as the snake's head
		if ( kibble.isKibble(new Point(snakeHeadX, snakeHeadY))){
			justAteMustGrowThisMuch += growthIncrement;
			return true;
		}
		return false;
	}

	public String toString(){
		//FINDBUGS
		// https://docs.oracle.com/javase/7/docs/api/java/lang/StringBuilder.html
		// String builder needs to be initialized with a capacity.
		// The capacity is the size of the snakeSquares array plus the number of rows to hold newline characters
		StringBuilder stringBuilder = new StringBuilder( (SnakeGame.getxSquares() * SnakeGame.getSquareSize() ) + SnakeGame.getxSquares());
		String textsnake = "";
		//This looks the wrong way around. Actually need to do it this way or snake is drawn flipped 90 degrees.
		for (int y = 0 ; y < maxY ; y++) {
			for (int x = 0 ; x < maxX ; x++){
				stringBuilder.append(snakeSquares[x][y]);
				//textsnake = textsnake + snakeSquares[x][y];
			}
			stringBuilder.append("\n");
			//textsnake += "\n";
		}
		return stringBuilder.toString();
	}

	public boolean wonGame() {

		//If all of the squares have snake segments in, the snake has eaten so much kibble 
		//that it has filled the screen. Win!
		for (int x = 0 ; x < maxX ; x++) {
			for (int y = 0 ; y < maxY ; y++){
				if (snakeSquares[x][y] == 0) {
					//there is still empty space on the screen, so haven't won
					return false;
				}
			}
		}
		//But if we get here, the snake has filled the screen. win!
		return true;
	}

	public void reset() {
		hitWall = false;
		ateTail = false;
		// reset game board with zeros
		fillGameboardWithZeros();
		// assign gameboard reference to local variable snakeSquares
		snakeSquares = emptyGameboard;
		createStartSnake();

	}

	public boolean isGameOver() {
		if (hitWall || ateTail){
			return true;
		}
		return false;
	}
	public int getSnakeHeadX(){
		return snakeHeadX;
	}
	public int getSnakeHeadY(){
		return snakeHeadY;

	}
	public String getSnakeHead(){
		return String.format("(%d,%d)\n",snakeHeadX,snakeHeadY);
	}

	public int getRelativeX(){
		return (int) (startHeadX - snakeHeadX );
	}
	public int getRelativeY(){
		return (int) (startHeadY - snakeHeadY);
	}

}


