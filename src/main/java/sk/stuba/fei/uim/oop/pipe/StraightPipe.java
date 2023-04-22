package sk.stuba.fei.uim.oop.pipe;

import sk.stuba.fei.uim.oop.board.Direction;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class StraightPipe extends Tile {
    public StraightPipe() {
        super();
        setBackground(Color.WHITE);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        rect = new Rectangle2D.Double(0, size.height / 2.0 - 20, size.width, 40);
        Shape s = transform.createTransformedShape(rect);

        if (state.equals(State.WATER)) {
            g2d.setColor(new Color(153, 217, 234));
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(s);
        g2d.setColor(Color.BLACK);
        g2d.draw(s);

        entry = Direction.LEFT;
        exit = Direction.RIGHT;

        rotatePipe();
        highlightPipe();
    }
}
