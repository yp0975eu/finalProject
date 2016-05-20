package com.branden;

/**
 * Created by Clara. Manages game components such as the Snake, Kibble... and their interactions.
 */
public class GameComponentManager {

    private Kibble kibble;
    private Snake snake;
    private Score score;
    private Maze maze;
    /** Called every clock tick. Tell components to interact/update,
     * manage interactions, update score etc.
     * If there were more components - e.g walls, mazes,
     * different types of kibble/prizes, different scoring systems...
     * they could be managed here too
     */
    public void update() {

        snake.moveSnake(maze);
        if (snake.didEatKibble(kibble)) {
			//tell kibble to update
            kibble.addKibble(snake);
            //FINDBUGS
            score.increaseScore();
            // if the maze is not active then all maze blocks will be 0
            // and it shouldn't interfere with the game
            if ( maze.isActive() ){
                maze.addBlock(snake);
            }

		}
    }

    public void newGame() {
        snake.reset();
        maze.reset();


    }


    public void addKibble(Kibble kibble) {
        this.kibble = kibble;
    }

    public void addSnake(Snake snake) {
        this.snake = snake;
    }

    public void addScore(Score score) {
        this.score = score;
    }
    public void addMaze(Maze maze) {
        this.maze = maze;
    }

    public Score getScore() {
        return score;
    }

    public Kibble getKibble() {
        return kibble;
    }

    public Snake getSnake() {
        return snake;
    }

    public Maze getMaze() {
        return maze;
    }



}
