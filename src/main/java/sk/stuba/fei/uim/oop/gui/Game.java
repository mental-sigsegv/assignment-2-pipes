package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.gameLogic.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Game {
    public static final String RESET = "RESTART";
    public static final String CHECK = "CHECK";
    public Game() {
        JFrame frame = new JFrame("Water pipes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024,1024);
        frame.setResizable(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        GameLogic gameLogic = new GameLogic(frame);
        frame.addKeyListener(gameLogic);

        JPanel menu = new JPanel();
        menu.setBackground(Color.WHITE);

        JButton buttonRestart = new JButton(RESET);
        buttonRestart.addActionListener(gameLogic);
        buttonRestart.setFocusable(false);

        JButton buttonCheck = new JButton(CHECK);
        buttonCheck.addActionListener(gameLogic);
        buttonCheck.setFocusable(false);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 8, 16, 8);
        slider.setMinorTickSpacing(2);
        slider.setMajorTickSpacing(2);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(gameLogic);

        menu.setLayout(new GridLayout(2, 4));
        menu.add(buttonRestart);
        menu.add(gameLogic.getBoardSizeLabel());
        menu.add(slider);
        menu.add(buttonCheck);
        menu.add(gameLogic.getLevelLabel());

        frame.add(menu, BorderLayout.PAGE_START);

        frame.setVisible(true);
    }
}
