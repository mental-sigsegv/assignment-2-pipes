package sk.stuba.fei.uim.oop.pipe;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import sk.stuba.fei.uim.oop.board.Direction;

public abstract class Tile extends JPanel {
    @Setter
    private boolean highlight;
    @Setter
    protected State state;
    @Setter @Getter
    protected double rotation;
    @Getter @Setter
    protected Direction entry;
    @Getter @Setter
    protected Direction exit;
    @Getter
    private ArrayList<Direction> pipeEntrances;
    private LineBorder defaultBorder;
    private LineBorder highlightBorder;
    protected Rectangle2D rect;
    protected Dimension size;
    protected AffineTransform transform;
    public Tile() {
        state = State.AIR;
        highlight = false;

        initBorders();
        initEntrances();
    }
    private void initBorders() {
        defaultBorder = new LineBorder(Color.BLACK, 2);
        highlightBorder = new LineBorder(Color.RED, 2);
    }
    private void initEntrances() {
        entry = Direction.EMPTY;
        exit = Direction.EMPTY;

        pipeEntrances = new ArrayList<>();
        pipeEntrances.add(entry);
        pipeEntrances.add(exit);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        size = getSize();
        transform = new AffineTransform();
        transform.rotate(Math.toRadians(rotation),  getWidth() / 2.0, getHeight() / 2.0);
    }
    protected void rotatePipe() {
        if (!(this instanceof Empty)) {
            entry = Direction.values()[(entry.ordinal() + (int) rotation/90)%(Direction.values().length-1)];
            exit = Direction.values()[(exit.ordinal() + (int) rotation/90)%(Direction.values().length-1)];
        }
    }
    protected void highlightPipe() {
        if (highlight) {
            setBorder(highlightBorder);
            highlight = false;
        } else {
            setBorder(defaultBorder);
        }
    }
    public void swapEntryExit() {
        Direction tmp;
        tmp = entry;
        entry = exit;
        exit = tmp;
    }
}
