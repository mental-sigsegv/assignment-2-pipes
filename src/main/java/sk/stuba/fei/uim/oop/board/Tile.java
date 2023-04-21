package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import sk.stuba.fei.uim.oop.board.Type;

public class Tile extends JPanel {
    @Setter
    private boolean highlight;
    @Setter @Getter
    private Type type;
    @Setter
    private Compound compound;
    @Setter @Getter
    private double rotation = 0.0;
    @Getter
    private Direction entry;
    @Getter
    private Direction exit;
    @Getter
    private ArrayList<Direction> directions;
    private final LineBorder defaultBorder;
    private final LineBorder highlightBorder;
    public Tile() {
        defaultBorder = new LineBorder(Color.BLACK, 2);
        highlightBorder = new LineBorder(Color.RED, 2);
        type = Type.STRAIGHT_PIPE;
        highlight = false;
        entry = Direction.EMPTY;
        exit = Direction.EMPTY;

        compound = Compound.AIR;

        directions = new ArrayList<>();
        directions.add(entry);
        directions.add(exit);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        if (type.equals(Type.START)) {
            setBackground(Color.GREEN);
        } else if (type.equals(Type.END)) {
            setBackground(Color.RED);
        } else if (compound.equals(Compound.WATER)) {
            setBackground(Color.blue);
//            setCompound(Compound.AIR);
        }
        else {
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

        if (!type.equals(Type.EMPTY)) {
            entry = Direction.values()[(entry.ordinal() + (int) rotation/90)%(Direction.values().length-1)];
            exit = Direction.values()[(exit.ordinal() + (int) rotation/90)%(Direction.values().length-1)];
        }

        Shape s = transform.createTransformedShape(shape);
        g2d.draw(s);

        if (highlight) {
            setBorder(highlightBorder);
            highlight = false;
        } else {
            setBorder(defaultBorder);
        }
    }

    public void swapEntryExit() {
        Direction tmp;
        tmp = entry;
        entry = exit;
        exit = tmp;
    }
}
