package sk.stuba.fei.uim.oop.gameLogic;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.*;
import sk.stuba.fei.uim.oop.pipe.Empty;
import sk.stuba.fei.uim.oop.pipe.State;
import sk.stuba.fei.uim.oop.pipe.Tile;
import sk.stuba.fei.uim.oop.universalAdapter.UniversalAdapter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static sk.stuba.fei.uim.oop.gui.Game.CHECK;
import static sk.stuba.fei.uim.oop.gui.Game.RESET;

public class GameLogic extends UniversalAdapter {
    private Board board;
    private int boardSize;
    @Getter
    private JLabel boardSizeLabel;
    @Getter
    private JLabel levelLabel;
    private int level;
    private final JFrame mainFrame;
    public GameLogic(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        initBoard();
    }
    private void initBoard() {
        board = new Board(8);
        boardSize = 8;
        boardSizeLabel = new JLabel();
        level = 1;
        levelLabel = new JLabel(Integer.toString(level));
        board.addMouseListener(this);
        board.addMouseMotionListener(this);
        mainFrame.add(board);

        updateBoardSizeLabel();
        updateLevelLabel();
    }
    private void check() {
        if (checkPath()) {
            incLevel();
            gameRestart();
        } else {
            board.repaint();

            Timer timer = new Timer(1000, s -> clearWater());
            timer.setRepeats(false);
            timer.start();
        }
    }
    private void clearWater() {
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                board.getBoard()[i][j].setState(State.AIR);
            }
        }
        board.validate();
        board.repaint();
    }
    private boolean checkPath() {
        int tileX = board.getStartPos().get(0);
        int tileY = board.getStartPos().get(1);
        Tile tile = board.getBoard()[tileX][tileY];

        int nextTileX = tileX;
        int nextTileY = tileY;
        Tile nextTile;

        while (true) {
            if ((tile.getEntry() == tile.getExit()) && (tileX != board.getStartPos().get(0) || tileY != board.getStartPos().get(1))) {
                return true;
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
                return false;
            }

            nextTile = board.getBoard()[nextTileX][nextTileY];

            if (nextTile instanceof Empty) {
                return false;
            }

            Direction nextTileOppositeEntry = Direction.values()[(nextTile.getEntry().ordinal() + 2)%(Direction.values().length-1)];
            Direction nextTileOppositeExit = Direction.values()[(nextTile.getExit().ordinal() + 2)%(Direction.values().length-1)];

            if (tile.getExit() != nextTileOppositeEntry && tile.getExit() != nextTileOppositeExit) {
                return false;
            } else if (tile.getExit() == nextTileOppositeExit) {
                nextTile.swapEntryExit();
            }

            nextTile.setState(State.WATER);

            tile = nextTile;
            tileX = nextTileX;
            tileY = nextTileY;
        }
    }
    private void updateBoardSizeLabel() {
        boardSizeLabel.setText("CURRENT BOARD SIZE: " + boardSize);
        boardSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        boardSizeLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    private void resetLevelCounter() {
        level = 1;
        updateLevelLabel();
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
    private void updateLevelLabel() {
        levelLabel.setText("CURRENT BOARD LEVEL: " + level);
        levelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        levelLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    private void incLevel() {
        level++;
        updateLevelLabel();
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        boardSize = ((JSlider) e.getSource()).getValue();
        updateBoardSizeLabel();
        resetLevelCounter();
        gameRestart();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                gameRestart();
                resetLevelCounter();
                break;
            case KeyEvent.VK_ENTER:
                check();
                break;
            case KeyEvent.VK_ESCAPE:
                mainFrame.dispose();
        }
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
        component.repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        Component component = board.getComponentAt(e.getX(), e.getY());
        if (!(component instanceof Tile)) {
            return;
        }

        ((Tile) component).setHighlight(true);
        component.repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonName = ((JButton) e.getSource()).getText();
        switch (buttonName) {
            case RESET:
                gameRestart();
                resetLevelCounter();
                break;
            case CHECK:
                check();
        }
    }
}
