package sk.stuba.fei.uim.oop.gameLogic;

import lombok.Getter;
import sk.stuba.fei.uim.oop.board.*;
import sk.stuba.fei.uim.oop.pipe.Pipe;
import sk.stuba.fei.uim.oop.universalAdapter.UniversalAdapter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameLogic extends UniversalAdapter {
    private Board board;
    private int boardSize;
    @Getter
    private JLabel boardSizeLabel;
    @Getter
    private JLabel levelLabel;
    private int level;
    private JFrame mainFrame;
    public GameLogic(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        initBoard(8);
    }

    private void updateLevelLabel() {
        levelLabel.setText("CURRENT BOARD LEVEL: " + level);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    private void incLevel() {
        level++;
        updateLevelLabel();
    }

    private void initBoard(int size) {
        board = new Board(size);
        boardSize = size;
        boardSizeLabel = new JLabel();
        level = 1;
        levelLabel = new JLabel(Integer.toString(level));
        updateBoardSizeLabel();
        updateLevelLabel();
        board.addMouseListener(this);
        board.addMouseMotionListener(this);
        mainFrame.add(board);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component component = board.getComponentAt(e.getX(), e.getY());
        if (!(component instanceof Pipe)) {
            return;
        }

        ((Pipe) component).setHighlight(true);
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
        if (!(component instanceof Pipe)) {
            return;
        }

        ((Pipe) component).setRotation((((Pipe) component).getRotation() + 90)%360);
        ((Pipe) component).setHighlight(true);
        ((Pipe) component).repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        Component component = board.getComponentAt(e.getX(), e.getY());
        if (!(component instanceof Pipe)) {
            return;
        }

        ((Pipe) component).setHighlight(true);
        ((Pipe) component).repaint();
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
                checkFun();
                break;
            default:
                System.out.println("Button name not found");
        }


    }
    private void checkFun() {
        if (check()) {
            incLevel();
            gameRestart();
        } else {
            board.repaint();

            Timer timer = new Timer(1000, s -> {
                clearWater();
            });
            timer.setRepeats(false);
            timer.start();
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
    private boolean check() {
        int tileX = board.getStartPos().get(0);
        int tileY = board.getStartPos().get(1);
        Pipe pipe = board.getBoard()[tileX][tileY];
        Pipe nextPipe;
        int nextTileX = tileX;
        int nextTileY = tileY;

        while (true) {
            if ((pipe.getEntry() == pipe.getExit()) && (tileX != board.getStartPos().get(0) || tileY != board.getStartPos().get(1))) {
                return true;
            }

            switch (pipe.getExit()) {
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

            nextPipe = board.getBoard()[nextTileX][nextTileY];

            if (nextPipe.getType() == Type.EMPTY) {
                return false;
            }

            Direction nextTileOppositeEntry = Direction.values()[(nextPipe.getEntry().ordinal() + 2)%(Direction.values().length-1)];
            Direction nextTileOppositeExit = Direction.values()[(nextPipe.getExit().ordinal() + 2)%(Direction.values().length-1)];
            if (pipe.getExit() != nextTileOppositeEntry && pipe.getExit() != nextTileOppositeExit) {
                return false;
            } else if (pipe.getExit() == nextTileOppositeExit) {
                nextPipe.swapEntryExit();
            }

            nextPipe.setCompound(Compound.WATER);

            pipe = nextPipe;
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

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                gameRestart();
                break;
            case KeyEvent.VK_ENTER:
                checkFun();
                break;
            case KeyEvent.VK_ESCAPE:
                mainFrame.dispose();
        }
    }
}
