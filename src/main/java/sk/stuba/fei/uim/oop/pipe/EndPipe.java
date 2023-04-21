package sk.stuba.fei.uim.oop.pipe;

import sk.stuba.fei.uim.oop.board.Type;

import java.awt.*;

public class EndPipe extends Tile {
    public EndPipe() {
        super();
        setBackground(Color.RED);
        setType(Type.END);
        repaint();
    }
}
