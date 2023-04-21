package sk.stuba.fei.uim.oop.pipe;

import java.awt.*;

public class Empty extends Tile {
    public Empty() {
        super();
        setBackground(Color.WHITE);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        highlightPipe();
    }
}
