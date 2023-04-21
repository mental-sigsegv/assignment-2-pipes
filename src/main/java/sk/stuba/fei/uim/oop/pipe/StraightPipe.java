package sk.stuba.fei.uim.oop.pipe;

import sk.stuba.fei.uim.oop.board.Type;

import java.awt.*;

public class StraightPipe extends Tile {
    public StraightPipe() {
        super();
        setBackground(Color.WHITE);
        setType(Type.STRAIGHT_PIPE);
        repaint();
    }
}
