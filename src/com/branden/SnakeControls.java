package com.branden;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by branden. Handles key presses that affect the snake.
 */
public class SnakeControls implements KeyListener {

    Snake snake;

    SnakeControls(Snake s){
        this.snake = s;
    }

    @Override
    public void keyPressed(KeyEvent ev) {

        if (ev.getKeyCode() == KeyEvent.VK_DOWN) {
            //System.out.println("Key Down");
            snake.snakeDown();
        }
        if (ev.getKeyCode() == KeyEvent.VK_UP) {
            //System.out.println("Key Up");
            snake.snakeUp();
        }
        if (ev.getKeyCode() == KeyEvent.VK_LEFT) {
            //System.out.println("Key Left");
            snake.snakeLeft();
        }
        if (ev.getKeyCode() == KeyEvent.VK_RIGHT) {
            //System.out.println("Key Right");
            snake.snakeRight();
        }

    }

    @Override
    public void keyTyped(KeyEvent ev) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
