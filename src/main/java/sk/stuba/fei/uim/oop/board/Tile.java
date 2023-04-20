package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import sk.stuba.fei.uim.oop.board.Type;

public class Tile extends JPanel {
    @Setter
    private boolean highlight;
    @Setter @Getter
    private Type type;
    @Setter @Getter
    private double rotation = 0.0;
    @Getter
    private Direction entry;
    @Getter
    private Direction exit;
    private final LineBorder defaultBorder;
    private final LineBorder highlightBorder;
    public Tile() {
        defaultBorder = new LineBorder(Color.BLACK, 2);
        highlightBorder = new LineBorder(Color.RED, 2);
        type = Type.STRAIGHT_PIPE;
        highlight = false;
        entry = Direction.DOWN;
        exit = Direction.RIGHT;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        if (type.equals(Type.START)) {
            setBackground(Color.GREEN);
        } else if (type.equals(Type.END)) {
            setBackground(Color.RED);
        } else {
            setBackground(Color.WHITE);
        }

        AffineTransform transform = new AffineTransform();
        Rectangle2D shape = new Rectangle2D.Double(0, 0, 0, 0);
        Dimension size = getSize();
        if (type.equals(Type.STRAIGHT_PIPE)) {
            shape = new Rectangle2D.Double(0, size.height / 2 - 1, size.width, 3);
            entry = Direction.LEFT;
            exit = Direction.RIGHT;
        } else if (type.equals(Type.CURVED_PIPE)) {
            shape = new Rectangle2D.Double( size.width/2, size.height / 2 - 1, size.width, 3);
            Rectangle2D tmp = new Rectangle2D.Double( size.width/2 - 1, size.width/2 + 1, 3, size.height / 2);
            shape.add(tmp);
            entry = Direction.DOWN;
            exit = Direction.RIGHT;
        } else if (type.equals(Type.END) || type.equals(Type.START)) {
            shape = new Rectangle2D.Double( size.width/2, size.height / 2 - 1, size.width, 3);
            exit = Direction.RIGHT;
            entry = Direction.RIGHT;
        }
        transform.rotate(Math.toRadians(rotation),  (double) getWidth() /2, (double) getHeight() /2);
//        System.out.println(entry);
//        System.out.println((entry.ordinal() + (int) rotation/90)%Direction.values().length);
        entry = Direction.values()[(entry.ordinal() + 1 + (int) rotation/90)%Direction.values().length];
        exit = Direction.values()[(exit.ordinal() + 1 + (int) rotation/90)%Direction.values().length];

        Shape s = transform.createTransformedShape(shape);
        g2d.draw(s);

        if (highlight) {
            setBorder(highlightBorder);
            highlight = false;
        } else {
            setBorder(defaultBorder);
        }
    }
}
