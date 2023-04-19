package sk.stuba.fei.uim.oop.gameLogic;

import sk.stuba.fei.uim.oop.board.Board;
import sk.stuba.fei.uim.oop.board.Tile;
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
    private JFrame mainFrame;
    public GameLogic(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        initBoard(8);
    }

    private void initBoard(int size) {
        board = new Board(size);
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
}
