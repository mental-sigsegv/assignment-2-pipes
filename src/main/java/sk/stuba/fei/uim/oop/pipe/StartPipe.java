package sk.stuba.fei.uim.oop.pipe;

import sk.stuba.fei.uim.oop.board.Direction;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class StartPipe extends Tile {
    public StartPipe() {
        super();
        setBackground(Color.GREEN);
        setType(Type.START);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        rect = new Rectangle2D.Double( size.width / 3.0, size.height / 2.0 - 20, size.width, 40);
        Shape s = transform.createTransformedShape(rect);
        g2d.setColor(new Color(153, 217, 234));
        g2d.fill(s);
        g2d.setColor(Color.black);
        g2d.draw(s);

        exit = Direction.RIGHT;
        entry = Direction.RIGHT;

        rotatePipe();
        highlightPipe();
    }
}
