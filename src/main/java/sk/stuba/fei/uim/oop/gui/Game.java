package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.gameLogic.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Game {
    public Game() {
        JFrame frame = new JFrame("Water pipes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024/2,1024/2);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        GameLogic gameLogic = new GameLogic(frame);


        frame.setVisible(true);
    }
}
