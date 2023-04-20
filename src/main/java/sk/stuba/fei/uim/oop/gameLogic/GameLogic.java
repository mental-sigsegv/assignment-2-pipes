package sk.stuba.fei.uim.oop.gameLogic;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.board.Board;
import sk.stuba.fei.uim.oop.board.Tile;
import sk.stuba.fei.uim.oop.board.Type;
import sk.stuba.fei.uim.oop.universalAdapter.UniversalAdapter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class GameLogic extends UniversalAdapter {
    private Board board;
    private int boardSize;
    @Getter
    private JLabel boardSizeLabel;
    private JFrame mainFrame;
    public GameLogic(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        initBoard(8);
    }

    private void initBoard(int size) {
        board = new Board(size);
        boardSize = size;
        boardSizeLabel = new JLabel();
        updateBoardSizeLabel();
        board.addMouseListener(this);
        board.addMouseMotionListener(this);
        mainFrame.add(board);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component component = board.getComponentAt(e.getX(), e.getY());
        if (!(component instanceof Tile)) {
            return;
        }

        ((Tile) component).setHighlight(true);
        board.repaint();

    }
    @Override
    public void mouseExited(MouseEvent e) {
        board.clearHighlight();
        board.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Component component = board.getComponentAt(e.getX(), e.getY());
        if (!(component instanceof Tile)) {
            return;
        }
        if (((Tile) component).getType().equals(Type.STRAIGHT_PIPE) || ((Tile) component).getType().equals(Type.CURVED_PIPE)) {
            ((Tile) component).setRotation((((Tile) component).getRotation() + 90)%360);
        }
        ((Tile) component).setHighlight(true);
        ((Tile) component).repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        Component component = board.getComponentAt(e.getX(), e.getY());
        if (!(component instanceof Tile)) {
            return;
        }

        ((Tile) component).setHighlight(true);
        ((Tile) component).repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonName = ((JButton) e.getSource()).getText();
        switch (buttonName) {
            case "RESTART":
                gameRestart();
                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.setFocusable(true);
                mainFrame.requestFocus();
                break;
            case "CHECK":
                System.out.println("Check");
                break;
            default:
                System.out.println("Button name not found");
        }


    }


    private void updateBoardSizeLabel() {
        boardSizeLabel.setText("CURRENT BOARD SIZE: " + boardSize);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void gameRestart() {
        mainFrame.remove(board);
        initializeNewBoard(boardSize);
        mainFrame.add(board);
    }

    private void initializeNewBoard(int dimension) {
        board = new Board(dimension);
        board.addMouseMotionListener(this);
        board.addMouseListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        boardSize = ((JSlider) e.getSource()).getValue();
        updateBoardSizeLabel();
        gameRestart();
        mainFrame.setFocusable(true);
        mainFrame.requestFocus();
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
