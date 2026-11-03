package src.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

final class UiStyle {
    static final Color NAVY = new Color(24, 54, 66);
    static final Color TEAL = new Color(28, 116, 108);
    static final Color TEAL_DARK = new Color(20, 91, 85);
    static final Color BG = new Color(245, 248, 247);
    static final Color TEXT = new Color(38, 55, 61);
    static final Color MUTED = new Color(112, 130, 133);
    static final Color BORDER = new Color(226, 234, 232);
    static final Color ORANGE = new Color(190, 119, 39);
    static final Color RED = new Color(177, 70, 65);
    static final Font FONT = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font BOLD = new Font("Segoe UI", Font.BOLD, 14);

    private UiStyle() { }

    static void styleButton(JButton b, boolean primary) {
        b.setFont(BOLD);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(11, 17, 11, 17));
        b.setBackground(primary ? TEAL : Color.WHITE);
        b.setForeground(primary ? Color.WHITE : TEXT);
        if (!primary) b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER), new EmptyBorder(10, 16, 10, 16)));
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
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(9, 10, 9, 10)));
        return f;
    }

    static JPanel page(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout(0, 18));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(25, 28, 25, 28));
        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        JLabel h = new JLabel(title);
        h.setFont(new Font("Segoe UI", Font.BOLD, 25));
        h.setForeground(NAVY);
        JLabel sub = new JLabel(subtitle);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(MUTED);
        sub.setBorder(new EmptyBorder(5, 0, 0, 0));
        heading.add(h); heading.add(sub);
        p.add(heading, BorderLayout.NORTH);
        return p;
    }

    static JPanel whiteCard() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(16, 16, 16, 16)));
        return p;
    }
}
