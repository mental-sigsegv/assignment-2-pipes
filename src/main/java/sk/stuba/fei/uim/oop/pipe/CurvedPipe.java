package sk.stuba.fei.uim.oop.pipe;

import sk.stuba.fei.uim.oop.board.Type;

import java.awt.*;

public class CurvedPipe extends Tile {
    public CurvedPipe() {
        super();
        setBackground(Color.WHITE);
        setType(Type.CURVED_PIPE);
        repaint();
    }
}
