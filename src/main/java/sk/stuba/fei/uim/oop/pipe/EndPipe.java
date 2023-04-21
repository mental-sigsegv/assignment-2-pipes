package sk.stuba.fei.uim.oop.pipe;

import sk.stuba.fei.uim.oop.board.Direction;
import sk.stuba.fei.uim.oop.board.Type;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class EndPipe extends Tile {
    public EndPipe() {
        super();
        setBackground(Color.RED);
        setType(Type.END);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        rect = new Rectangle2D.Double( size.width / 3.0, size.height / 2.0 - 20, size.width, 40);
        Shape shape = transform.createTransformedShape(rect);

        g2d.setColor(Color.WHITE);
        g2d.fill(shape);
        g2d.setColor(Color.black);
        g2d.draw(shape);

        exit = Direction.RIGHT;
        entry = Direction.RIGHT;

        rotatePipe();
        highlightPipe();
    }
}
