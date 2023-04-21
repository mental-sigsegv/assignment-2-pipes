package sk.stuba.fei.uim.oop.pipe;

import sk.stuba.fei.uim.oop.board.Type;

import java.awt.*;

public class StartPipe extends Tile {
    public StartPipe() {
        super();
        setBackground(Color.GREEN);
        setType(Type.START);
        repaint();
    }
}
