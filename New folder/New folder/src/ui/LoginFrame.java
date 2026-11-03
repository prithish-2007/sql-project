package src.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import src.dao.StaffDAO;

public class LoginFrame extends JFrame {
    private final JTextField username = UiStyle.field(20);
    private final JPasswordField password = new JPasswordField(20);

    public LoginFrame() {
        setTitle("FirstAid Inventory | Staff Sign In");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 570);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(UiStyle.BG);
        JPanel brand = new JPanel();
        brand.setBackground(UiStyle.NAVY);
        brand.setBorder(new EmptyBorder(55, 48, 45, 45));
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        JLabel mark = new JLabel("✚"); mark.setFont(new Font("Segoe UI", Font.BOLD, 48)); mark.setForeground(new Color(134, 207, 190));
        JLabel name = new JLabel("FirstAid"); name.setFont(new Font("Segoe UI", Font.BOLD, 34)); name.setForeground(Color.WHITE);
        JLabel sub = new JLabel("INVENTORY SYSTEM"); sub.setFont(new Font("Segoe UI", Font.BOLD, 12)); sub.setForeground(new Color(171, 198, 198));
        JLabel desc = new JLabel("<html><div style='width:280px'>A smarter way to manage campus medical supplies, monitor expiry dates and keep essential first-aid items ready.</div></html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 16)); desc.setForeground(new Color(218, 231, 229));
        desc.setBorder(new EmptyBorder(28, 0, 0, 0));
        brand.add(mark); brand.add(Box.createVerticalStrut(10)); brand.add(name); brand.add(sub); brand.add(desc); brand.add(Box.createVerticalGlue());
        JLabel foot = new JLabel("COLLEGE HEALTH & SAFETY"); foot.setFont(new Font("Segoe UI", Font.BOLD, 11)); foot.setForeground(new Color(146, 174, 175)); brand.add(foot);

        JPanel formWrap = new JPanel(new GridBagLayout()); formWrap.setBackground(UiStyle.BG);
        JPanel form = new JPanel(); form.setOpaque(false); form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS)); form.setBorder(new EmptyBorder(25, 42, 25, 42));
        JLabel title = new JLabel("Welcome back"); title.setFont(new Font("Segoe UI", Font.BOLD, 28)); title.setForeground(UiStyle.NAVY);
        JLabel hint = new JLabel("Sign in with your staff account to continue."); hint.setFont(UiStyle.FONT); hint.setForeground(UiStyle.MUTED);
        form.add(title); form.add(Box.createVerticalStrut(8)); form.add(hint); form.add(Box.createVerticalStrut(35));
        form.add(UiStyle.label("Username")); form.add(Box.createVerticalStrut(8)); form.add(username); form.add(Box.createVerticalStrut(20));
        form.add(UiStyle.label("Password")); form.add(Box.createVerticalStrut(8));
        password.setFont(UiStyle.FONT); password.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiStyle.BORDER), new EmptyBorder(9,10,9,10))); form.add(password);
        form.add(Box.createVerticalStrut(26));
        JButton login = new JButton("Sign in  →"); UiStyle.styleButton(login, true); login.setAlignmentX(Component.LEFT_ALIGNMENT); login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        login.addActionListener(e -> signIn()); form.add(login);
        form.add(Box.createVerticalStrut(16));
        JLabel note = new JLabel("Authorized staff access only"); note.setForeground(UiStyle.MUTED); note.setFont(new Font("Segoe UI", Font.PLAIN, 12)); form.add(note);
        formWrap.add(form);
        root.add(brand); root.add(formWrap); setContentPane(root);
        getRootPane().setDefaultButton(login);
    }

    private void signIn() {
        String user = username.getText().trim();
        String pass = new String(password.getPassword());
        if (user.isEmpty() || pass.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter both username and password.", "Missing details", JOptionPane.WARNING_MESSAGE); return; }
        if (StaffDAO.login(user, pass)) {
            dispose(); new DashboardFrame(user).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Check your staff username and password, and confirm the database is connected.", "Unable to sign in", JOptionPane.ERROR_MESSAGE);
        }
    }
}
