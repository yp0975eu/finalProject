package com.branden;

import java.util.Timer;

import javax.swing.*;


public class SnakeGame {
	//FINDBUGS
	private static int xPixelMaxDimension;
	private static int yPixelMaxDimension;
	private static int xSquares ;
	private static int ySquares ;
	private static int squareSize;
	private static final int borderSize = 1;
	// the snake will always be 1 tenth the boardGameWidth
	private static final double snakeToWindowRatio = .01;

	//FINDBUGS
	private static Snake snake;
	private static Maze maze;
	private static GameComponentManager componentManager;
	// option variables
	private static boolean useMazes;
	private static boolean useWarp;
	private static boolean twoPlayerBattleMode;

	//FINDBUGS
	private static Score score;

	//The numerical values of these variables are not important.
	//The important thing is to use the constant instead of the values so you are clear what you are setting.
	//Easy to forget what number is Game over vs. game won
	//Using constant names instead makes it easier to keep it straight. Refer to these variables
	//using statements such as SnakeGame.GAME_OVER
	static final int BEFORE_GAME = 1;
	static final int DURING_GAME = 2;
	static final int GAME_OVER = 3;
	static final int GAME_WON = 4;

	//use this to figure out what should be happening.
	//Other classes like Snake and DrawSnakeGamePanel will query this, and change its value
	private static int gameStage = BEFORE_GAME;

	//FINDBUGS
	private static long clockInterval; //controls game speed
	//Every time the clock ticks, the snake moves
	//This is the time between clock ticks, in milliseconds
	//1000 milliseconds = 1 second.

	static JFrame snakeFrame;
	static DrawSnakeGamePanel snakePanel;
	//Framework for this class adapted from the Java Swing Tutorial, FrameDemo and Custom Painting Demo. You should find them useful too.
	//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/FrameDemoProject/src/components/FrameDemo.java
	//http://docs.oracle.com/javase/tutorial/uiswing/painting/step2.html



	protected static void createAndShowGUI() {
		//Create and set up the window.
		snakeFrame = new JFrame();
		snakeFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		snakeFrame.setSize(xPixelMaxDimension, yPixelMaxDimension);
		snakeFrame.setUndecorated(true); //hide title bar
		// BA: show the frame
		snakeFrame.setVisible(true);
		// BA: disable resizable window
		snakeFrame.setResizable(false);

		snakePanel = new DrawSnakeGamePanel(componentManager);

		snakePanel.setFocusable(true);
		snakePanel.requestFocusInWindow(); //required to give this component the focus so it can generate KeyEvents

		snakeFrame.add(snakePanel);
		//Add listeners to listen for key presses
		snakePanel.addKeyListener(new GameControls());
		//FINDBUGS
		snakePanel.addKeyListener(new SnakeControls(getSnake()));

		// BA: game stage already set in initializeGame()
		// setGameStage(BEFORE_GAME);

		// BA: duplicate code, same as line 69,  not needed
		// snakeFrame.setVisible(true);
	}

	protected static void initializeGame() {

		//set up score, snake and first kibble
		// BA: int division truncates decimal
	/*	xSquares = xPixelMaxDimension / squareSize;
		ySquares = yPixelMaxDimension / squareSize;*/

		// sets the game board grid to be x times as large as the viewport screen
		xSquares = (int) (xPixelMaxDimension * 2);
		ySquares = (int) (yPixelMaxDimension * 2);
		componentManager = new GameComponentManager();
		//FINDBUGS
		setSnake(new Snake(xSquares, ySquares, squareSize));

		Kibble kibble = new Kibble(getSnake());
		componentManager.addSnake(getSnake());
		componentManager.addKibble(kibble);
		maze = new Maze(xSquares, ySquares, squareSize, useMazes);
		componentManager.addMaze(maze);

		score = new Score();
		componentManager.addScore(score);

		gameStage = BEFORE_GAME;
	}
	//FINDBUGS
	public static void setSnake(Snake snake) {
		SnakeGame.snake = snake;
	}
	//FINDBUGS
	public static Snake getSnake() {
		return snake;
	}

	protected static void newGame() {
		Timer timer = new Timer();
		GameClock clockTick = new GameClock(componentManager, snakePanel);
		componentManager.newGame();
		timer.scheduleAtFixedRate(clockTick, 0, clockInterval);
	}


	public static int getGameStage() {
		return gameStage;
	}

	public static void setGameStage(int gameStage) {
		SnakeGame.gameStage = gameStage;
	}
	public static void setTwoPlayerBattleMode( Boolean battleMode){
		twoPlayerBattleMode = battleMode;
	}
	public static Boolean getTwoPlayerBattleMode(){
		return twoPlayerBattleMode;
	}

	public static void setMaze( Boolean mazes){
		useMazes = mazes;
	}
	public static void setWarp( Boolean warp){
		useWarp = warp;
	}
	public static Boolean useWarp(){
		return useWarp;
	}
	public static void setGameSpeed(int gs){
		// auto type conversion from int to long
		clockInterval = gs;
	}

	public static void setScreenSize(int windowSize){
		//Pixels in window. 501 to have 50-pixel squares plus 1 to draw a border on last square
		windowSize = 500;
		xPixelMaxDimension = windowSize + borderSize;
		yPixelMaxDimension = windowSize + borderSize;

		//squareSize = (int)( windowSize * snakeToWindowRatio ) ;
		squareSize = (int)( windowSize * .01 ) ;

	}
	public static Score getScore(){
		return score;
	}
	//FINDBUGS
	public static int getxPixelMaxDimension() {
		return xPixelMaxDimension;
	}
	//FINDBUGS
	public static int getyPixelMaxDimension() {
		return yPixelMaxDimension;
	}
	//FINDBUGS
	public static int getxSquares() {
		return xSquares;
	}
	//FINDBUGS
	public static int getySquares() {
		return ySquares;
	}
	//FINDBUGS
	public static int getSquareSize() {
		return squareSize;
	}
}
