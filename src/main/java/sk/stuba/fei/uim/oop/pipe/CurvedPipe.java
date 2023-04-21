package sk.stuba.fei.uim.oop.pipe;

import sk.stuba.fei.uim.oop.board.Compound;
import sk.stuba.fei.uim.oop.board.Direction;
import sk.stuba.fei.uim.oop.board.Type;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CurvedPipe extends Tile {
    public CurvedPipe() {
        super();
        setBackground(Color.WHITE);
        setType(Type.CURVED_PIPE);
        repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        rect = new Rectangle2D.Double( size.width / 2.0 - 20, size.height / 2.0 - 20, size.width, 160);
        Rectangle2D rectTmp = new Rectangle2D.Double( size.width / 2.0 + 20, size.height / 2.0 + 20, size.width, 80);

        Shape shape = transform.createTransformedShape(rect);
        Shape shape1 = transform.createTransformedShape(rectTmp);

        if (compound.equals(Compound.WATER)) {
            g2d.setColor(new Color(153, 217, 234));
        } else {
            g2d.setColor(Color.WHITE);
        }

        g2d.fill(shape);
        g2d.setColor(Color.BLACK);
        g2d.draw(shape);

        g2d.setColor(Color.WHITE);
        g2d.fill(shape1);
        g2d.setColor(Color.BLACK);
        g2d.draw(shape1);

        entry = Direction.DOWN;
        exit = Direction.RIGHT;

        rotatePipe();
        highlightPipe();
    }
}
