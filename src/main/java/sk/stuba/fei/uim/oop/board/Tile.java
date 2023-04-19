package sk.stuba.fei.uim.oop.board;

import lombok.Setter;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Tile extends JPanel {
    @Setter
    private boolean highlight;
    private LineBorder defaultBorder;
    private LineBorder highlightBorder;
    public Tile() {
        setBackground(Color.WHITE);
        defaultBorder = new LineBorder(Color.BLACK, 2);
        highlightBorder = new LineBorder(Color.RED, 2);

        highlight = false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (highlight) {
            setBorder(highlightBorder);
            highlight = false;
        } else {
            setBorder(defaultBorder);
        }
    }
}
