package com.branden;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by badams on 4/16/16.
 */
public class Options extends JFrame{

    private JPanel rootPanel;

    private JButton startGameButton;
    private int errors;
    Options(){
        setContentPane(rootPanel);
        setVisible(true);
        pack();
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (errors == 0) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            // game speed, screen size, mazes and warp options are now hardcoded for
                            // Development
                            SnakeGame.setGameSpeed(50);
                            SnakeGame.setScreenSize(500);
                            SnakeGame.setMaze(false);
                            SnakeGame.setWarp(false);
                            SnakeGame.initializeGame();
                            SnakeGame.createAndShowGUI();
                            SnakeGame.newGame();
                        }
                    });
                }
            }

        });
    }

}
