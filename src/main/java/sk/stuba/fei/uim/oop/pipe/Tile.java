package sk.stuba.fei.uim.oop.pipe;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import sk.stuba.fei.uim.oop.board.Compound;
import sk.stuba.fei.uim.oop.board.Direction;
import sk.stuba.fei.uim.oop.board.Type;

public abstract class Tile extends JPanel {
    @Setter
    private boolean highlight;
    @Setter @Getter
    private Type type;
    @Setter
    private Compound compound;
    @Setter @Getter
    private double rotation;
    @Getter @Setter
    private Direction entry;
    @Getter @Setter
    private Direction exit;
    @Getter
    private ArrayList<Direction> pipeEntrances;
    private LineBorder defaultBorder;
    private LineBorder highlightBorder;
    public Tile() {
        type = Type.EMPTY;
        compound = Compound.AIR;
        highlight = false;

        initBorders();
        initEntrances();
    }
    private void initBorders() {
        defaultBorder = new LineBorder(Color.BLACK, 2);
        highlightBorder = new LineBorder(Color.RED, 2);
    }
    private void initEntrances() {
        entry = Direction.EMPTY;
        exit = Direction.EMPTY;

        pipeEntrances = new ArrayList<>();
        pipeEntrances.add(entry);
        pipeEntrances.add(exit);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // TODO : divide pipes into subclasses with proper pain method and override entry/end attribute
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(rotation),  getWidth() / 2.0, getHeight() / 2.0);
        Rectangle2D shape;
        Dimension size = getSize();
        if (type.equals(Type.STRAIGHT_PIPE)) {
            shape = new Rectangle2D.Double(0, size.height / 2.0 - 20, size.width, 40);
            Shape s = transform.createTransformedShape(shape);

            if (compound.equals(Compound.WATER)) {
                g2d.setColor(new Color(153, 217, 234));
            } else {
                g2d.setColor(Color.WHITE);
            }

            g2d.fill(s);
            g2d.setColor(Color.BLACK);
            g2d.draw(s);

            entry = Direction.LEFT;
            exit = Direction.RIGHT;
        } else if (type.equals(Type.CURVED_PIPE)) {
            shape = new Rectangle2D.Double( size.width / 2.0 - 20, size.height / 2.0 - 20, size.width, 160);
            Rectangle2D tmp = new Rectangle2D.Double( size.width / 2.0 + 20, size.height / 2.0 + 20, size.width, 80);

            Shape s = transform.createTransformedShape(shape);
            Shape s2 = transform.createTransformedShape(tmp);

            if (compound.equals(Compound.WATER)) {
                g2d.setColor(new Color(153, 217, 234));
            } else {
                g2d.setColor(Color.WHITE);
            }

            g2d.fill(s);
            g2d.setColor(Color.BLACK);
            g2d.draw(s);


            g2d.setColor(Color.WHITE);
            g2d.fill(s2);
            g2d.setColor(Color.BLACK);
            g2d.draw(s2);

            entry = Direction.DOWN;
            exit = Direction.RIGHT;
        } else if (type.equals(Type.START)) {
            shape = new Rectangle2D.Double( size.width / 3.0, size.height / 2.0 - 20, size.width, 40);
            Shape s = transform.createTransformedShape(shape);
            g2d.setColor(new Color(153, 217, 234));
            g2d.fill(s);
            g2d.setColor(Color.black);
            g2d.draw(s);

            exit = Direction.RIGHT;
            entry = Direction.RIGHT;
        } else if (type.equals(Type.END)){
            shape = new Rectangle2D.Double( size.width / 3.0, size.height / 2.0 - 20, size.width, 40);
            Shape s = transform.createTransformedShape(shape);
            g2d.setColor(Color.WHITE);
            g2d.fill(s);
            g2d.setColor(Color.black);
            g2d.draw(s);

            exit = Direction.RIGHT;
            entry = Direction.RIGHT;
        }


        if (!type.equals(Type.EMPTY)) {
            entry = Direction.values()[(entry.ordinal() + (int) rotation/90)%(Direction.values().length-1)];
            exit = Direction.values()[(exit.ordinal() + (int) rotation/90)%(Direction.values().length-1)];
        }


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
