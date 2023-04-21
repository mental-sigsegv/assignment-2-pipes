package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.gameLogic.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Game {
    public Game() {
        JFrame frame = new JFrame("Water pipes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024,1024);
        frame.setResizable(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        GameLogic gameLogic = new GameLogic(frame);
        frame.addKeyListener(gameLogic);

        JPanel sideMenu = new JPanel();
        sideMenu.setBackground(Color.LIGHT_GRAY);

        JButton buttonRestart = new JButton("RESTART");
        buttonRestart.addActionListener(gameLogic);
        buttonRestart.setFocusable(false);

        JButton buttonCheck = new JButton("CHECK");
        buttonCheck.addActionListener(gameLogic);
        buttonCheck.setFocusable(false);


        JSlider slider = new JSlider(JSlider.HORIZONTAL, 8, 16, 8);
        slider.setMinorTickSpacing(2);
        slider.setMajorTickSpacing(2);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(gameLogic);

        sideMenu.setLayout(new GridLayout(2, 4, 1, 1));
        sideMenu.add(buttonRestart);
        sideMenu.add(buttonCheck);
        sideMenu.add(gameLogic.getBoardSizeLabel());
        sideMenu.add(slider);
        frame.add(sideMenu, BorderLayout.PAGE_START);

        frame.setVisible(true);
    }
}
