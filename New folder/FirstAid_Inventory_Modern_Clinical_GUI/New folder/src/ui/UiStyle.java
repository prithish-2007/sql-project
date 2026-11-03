
package src.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

final class UiStyle {

    static final Color NAVY = new Color(20, 55, 68);

    // GREEN BUTTON COLORS
    static final Color TEAL = new Color(20, 150, 95);
    static final Color TEAL_DARK = new Color(15, 120, 75);

    static final Color BG = new Color(243, 248, 247);
    static final Color TEXT = new Color(35, 55, 65);
    static final Color MUTED = new Color(105, 125, 135);
    static final Color BORDER = new Color(215, 229, 226);
    static final Color ORANGE = new Color(205, 125, 35);
    static final Color RED = new Color(185, 65, 65);

    static final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font BOLD = new Font("Segoe UI", Font.BOLD, 14);

    private UiStyle() {}

    static void styleButton(JButton b, boolean primary) {

        b.setUI(new BasicButtonUI());
        b.setFont(BOLD);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(true);
        b.setRolloverEnabled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (primary) {
            b.setBackground(TEAL);
            b.setForeground(Color.WHITE);

            b.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TEAL),
                    new EmptyBorder(11, 20, 11, 20)
                )
            );

        } else {
            b.setBackground(Color.WHITE);
            b.setForeground(NAVY);

            b.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    new EmptyBorder(10, 18, 10, 18)
                )
            );
        }
    }

    static JLabel label(String text) {

        JLabel l = new JLabel(text);
        l.setFont(FONT);
        l.setForeground(TEXT);

        return l;
    }

    static JTextField field(int columns) {

        JTextField f = new JTextField(columns);

        f.setFont(FONT);
        f.setForeground(TEXT);
        f.setBackground(Color.WHITE);
        f.setCaretColor(TEAL);

        f.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(9, 10, 9, 10)
            )
        );

        return f;
    }

    static JPanel page(String title, String subtitle) {

        JPanel p = new JPanel(new BorderLayout(0, 18));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(25, 28, 25, 28));

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(
            new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        JLabel h = new JLabel(title);
        h.setFont(new Font("Segoe UI", Font.BOLD, 27));
        h.setForeground(NAVY);

        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(MUTED);
        sub.setBorder(new EmptyBorder(5, 0, 0, 0));

        heading.add(h);
        heading.add(sub);

        p.add(heading, BorderLayout.NORTH);

        return p;
    }

    static JPanel whiteCard() {

        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(Color.WHITE);

        p.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 16, 16)
            )
        );

        return p;
    }
}
