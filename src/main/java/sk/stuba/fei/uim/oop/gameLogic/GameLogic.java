package sk.stuba.fei.uim.oop.gameLogic;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.board.*;
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

        ((Tile) component).setRotation((((Tile) component).getRotation() + 90)%360);
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

        System.out.println(((Tile) component).getExit());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonName = ((JButton) e.getSource()).getText();
        switch (buttonName) {
            // TODO : replace string with static attributes
            case "RESTART":
                gameRestart();
                break;
            case "CHECK":
                check();
                board.repaint();

                Timer timer = new Timer(1000, s -> {
                    clearWater();
                });
                timer.setRepeats(false);
                timer.start();
                break;
            default:
                System.out.println("Button name not found");
        }


    }
    private void clearWater() {
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                board.getBoard()[i][j].setCompound(Compound.AIR);
            }
        }
        board.validate();
        board.repaint();
    }

    // TODO : implement better way of showing water path (just background is not clearly visible)
    private void check() {
        int tileX = board.getStartPos().get(0);
        int tileY = board.getStartPos().get(1);
        Tile tile = board.getBoard()[tileX][tileY];
        Tile nextTile;
        int nextTileX = tileX;
        int nextTileY = tileY;

        // TODO : remove system out print
        while (true) {
            if ((tile.getEntry() == tile.getExit()) && tileX != board.getStartPos().get(0) && tileY != board.getStartPos().get(1)) {
                System.out.println("Path found");
                break;
            }

            switch (tile.getExit()) {
                case LEFT:
                    nextTileY = tileY - 1;
                    break;
                case RIGHT:
                    nextTileY = tileY + 1;
                    break;
                case UP:
                    nextTileX = tileX - 1;
                    break;
                case DOWN:
                    nextTileX = tileX + 1;
                    break;
            }
            if (nextTileX < 0 || nextTileY < 0 || nextTileX >= boardSize || nextTileY >= boardSize) {
                System.out.println("Path out of board. 404 not found");
                break;
            }

            nextTile = board.getBoard()[nextTileX][nextTileY];

            if (nextTile.getType() == Type.EMPTY) {
                System.out.println("Empty tile");
                break;
            }

            Direction nextTileOppositeEntry = Direction.values()[(nextTile.getEntry().ordinal() + 2)%(Direction.values().length-1)];
            Direction nextTileOppositeExit = Direction.values()[(nextTile.getExit().ordinal() + 2)%(Direction.values().length-1)];
            if (tile.getExit() != nextTileOppositeEntry && tile.getExit() != nextTileOppositeExit) {
                System.out.println(tile.getExit());
                System.out.println(nextTile.getEntry() + " " + nextTile.getExit());
                System.out.println("Wrong pipe / wall");
                break;
            } else if (tile.getExit() == nextTileOppositeExit) {
                nextTile.swapEntryExit();
            }

            nextTile.setCompound(Compound.WATER);

            tile = nextTile;
            tileX = nextTileX;
            tileY = nextTileY;
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

        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.setFocusable(true);
        mainFrame.requestFocus();
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
    }
}
