package src.ui;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import src.dao.MedicineDAO;
import src.dao.StockDAO;
import src.database.DBConnection;

public class DashboardFrame extends JFrame {

    private final String username;
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);

    private final Color SIDEBAR_TEXT = new Color(220, 235, 233);
    private final Color SIDEBAR_MUTED = new Color(155, 187, 187);
    private final Color TEAL_LIGHT = new Color(222, 243, 238);

    public DashboardFrame(String username) {

        this.username = username;

        setTitle("FirstAid Inventory | Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1120, 720));
        setSize(1320, 820);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UiStyle.BG);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMainArea(), BorderLayout.CENTER);

        setContentPane(root);

        showPage("Dashboard");
    }

    // ================= SIDEBAR =================

    private JPanel buildSidebar() {

        JPanel side = new JPanel();

        side.setBackground(UiStyle.NAVY);
        side.setPreferredSize(new Dimension(245, 0));
        side.setBorder(new EmptyBorder(25, 14, 18, 14));

        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

        // LOGO

        JPanel logo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logo.setOpaque(false);

        JLabel plus = new JLabel("+");
        plus.setFont(new Font("Segoe UI", Font.BOLD, 32));
        plus.setForeground(new Color(112, 216, 191));

        JPanel words = new JPanel();
        words.setOpaque(false);
        words.setLayout(new BoxLayout(words, BoxLayout.Y_AXIS));

        JLabel brand = new JLabel("FirstAid");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brand.setForeground(Color.WHITE);

        JLabel small = new JLabel("INVENTORY SYSTEM");
        small.setFont(new Font("Segoe UI", Font.BOLD, 10));
        small.setForeground(SIDEBAR_MUTED);

        words.add(brand);
        words.add(small);

        logo.add(plus);
        logo.add(words);

        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(logo);
        side.add(Box.createVerticalStrut(38));

        // NAVIGATION HEADING

        JLabel nav = new JLabel("WORKSPACE");
        nav.setFont(new Font("Segoe UI", Font.BOLD, 11));
        nav.setForeground(SIDEBAR_MUTED);
        nav.setBorder(new EmptyBorder(0, 11, 12, 0));
        nav.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(nav);

        // NAVIGATION ITEMS

        String[][] items = {
            {"Dashboard", "01"},
            {"Medicine Stock", "02"},
            {"Add Medicine", "03"},
            {"Add Stock", "04"},
            {"Update Stock", "05"},
            {"Expiry Tracking", "06"},
            {"Low Stock Alerts", "07"},
            {"Search Medicine", "08"},
            {"Expired Stock Report", "09"},
            {"Stock History", "10"}
        };

        java.util.List<JButton> navButtons =
            new java.util.ArrayList<>();

        for (String[] item : items) {

            JButton b = new JButton(
                item[0]
            );

            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setFont(new Font("Segoe UI", Font.BOLD, 13));

            b.setForeground(SIDEBAR_TEXT);
            b.setBackground(UiStyle.NAVY);

            b.setOpaque(true);
            b.setContentAreaFilled(true);
            b.setBorderPainted(false);
            b.setFocusPainted(false);

            b.setBorder(
                new EmptyBorder(12, 15, 12, 8)
            );

            b.setCursor(
                Cursor.getPredefinedCursor(
                    Cursor.HAND_CURSOR
                )
            );

            b.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 43)
            );

            b.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Active dashboard button

            if (item[0].equals("Dashboard")) {
                b.setBackground(UiStyle.TEAL);
                b.setForeground(Color.WHITE);
            }

            navButtons.add(b);

            b.addActionListener(e -> {

                for (JButton button : navButtons) {

                    button.setBackground(UiStyle.NAVY);
                    button.setForeground(SIDEBAR_TEXT);
                }

                b.setBackground(UiStyle.TEAL);
                b.setForeground(Color.WHITE);

                showPage(item[0]);
            });

            side.add(b);
            side.add(Box.createVerticalStrut(5));
        }

        side.add(Box.createVerticalGlue());

        // STAFF INFORMATION

        JPanel user = new JPanel(new BorderLayout(10, 0));
        user.setOpaque(false);
        user.setBorder(new EmptyBorder(12, 8, 15, 5));

        JLabel avatar = new JLabel("●");
        avatar.setForeground(new Color(112, 216, 191));
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 25));

        JPanel userText = new JPanel();
        userText.setOpaque(false);
        userText.setLayout(
            new BoxLayout(userText, BoxLayout.Y_AXIS)
        );

        JLabel u = new JLabel(username);
        u.setForeground(Color.WHITE);
        u.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel role = new JLabel("Staff account");
        role.setForeground(SIDEBAR_MUTED);
        role.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        userText.add(u);
        userText.add(role);

        user.add(avatar, BorderLayout.WEST);
        user.add(userText, BorderLayout.CENTER);

        side.add(user);

        // LOGOUT

        JButton logout = new JButton("Sign out");

        logout.setForeground(new Color(255, 218, 214));
        logout.setBackground(new Color(35, 73, 82));

        logout.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logout.setBorder(new EmptyBorder(12, 15, 12, 15));

        logout.setHorizontalAlignment(SwingConstants.LEFT);
        logout.setFocusPainted(false);
        logout.setOpaque(true);
        logout.setBorderPainted(false);

        logout.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 43)
        );

        logout.addActionListener(e -> {

            dispose();
            new LoginFrame().setVisible(true);

        });

        side.add(logout);

        return side;
    }

    // ================= MAIN AREA =================

    private JPanel buildMainArea() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UiStyle.BG);

        JPanel top = new JPanel(new BorderLayout());

        top.setBackground(Color.WHITE);
        top.setBorder(
            new EmptyBorder(19, 30, 19, 30)
        );

        JLabel path = new JLabel(
            "COLLEGE HEALTH & SAFETY   /   INVENTORY"
        );

        path.setFont(
            new Font("Segoe UI", Font.BOLD, 11)
        );

        path.setForeground(UiStyle.MUTED);

        JLabel user = new JLabel(
            "Signed in as  " + username
        );

        user.setFont(UiStyle.FONT);
        user.setForeground(UiStyle.TEXT);

        top.add(path, BorderLayout.WEST);
        top.add(user, BorderLayout.EAST);

        main.add(top, BorderLayout.NORTH);

        content.setBackground(UiStyle.BG);

        main.add(content, BorderLayout.CENTER);

        return main;
    }

    // ================= PAGE NAVIGATION =================

    private void showPage(String name) {

        JPanel page;

        switch (name) {

            case "Medicine Stock":
                page = buildInventoryPage();
                break;

            case "Add Medicine":
                page = buildAddMedicinePage();
                break;

            case "Add Stock":
                page = buildAddStockPage();
                break;

            case "Update Stock":
                page = buildUpdateStockPage();
                break;

            case "Expiry Tracking":
                page = buildExpiryPage();
                break;

            case "Low Stock Alerts":
                page = buildLowStockPage();
                break;

            case "Search Medicine":
                page = buildSearchPage();
                break;

            case "Expired Stock Report":
                page = buildExpiredPage();
                break;

            case "Stock History":
                page = buildHistoryPage();
                break;

            default:
                page = buildDashboard();
                name = "Dashboard";
        }

        Component old = null;

        for (Component component : content.getComponents()) {

            if (name.equals(component.getName())) {
                old = component;
                break;
            }
        }

        if (old != null) {
            content.remove(old);
        }

        page.setName(name);

        content.add(page, name);

        cards.show(content, name);

        content.revalidate();
        content.repaint();
    }

    // ================= DASHBOARD =================

    private JPanel buildDashboard() {

        JPanel p = UiStyle.page(
            "Good day, " + username,
            "Here is the current status of your campus first-aid inventory."
        );

        JPanel body = new JPanel(new BorderLayout(0, 20));
        body.setOpaque(false);

        // SUMMARY CARDS

        JPanel metrics = new JPanel(
            new GridLayout(1, 5, 14, 0)
        );

        metrics.setOpaque(false);

        metrics.add(metric(
            "TOTAL MEDICINES",
            "—",
            "Registered items",
            new Color(224, 244, 238),
            UiStyle.TEAL,
            "SELECT COUNT(*) FROM MEDICINE"
        ));

        metrics.add(metric(
            "AVAILABLE UNITS",
            "—",
            "Across all batches",
            new Color(229, 239, 249),
            new Color(48, 105, 155),
            "SELECT COALESCE(SUM(quantity),0) FROM STOCK"
        ));

        metrics.add(metric(
            "LOW STOCK",
            "—",
            "Needs replenishment",
            new Color(255, 239, 216),
            UiStyle.ORANGE,
            "SELECT COUNT(*) FROM STOCK WHERE quantity <= minimum_quantity"
        ));

        metrics.add(metric(
            "EXPIRING SOON",
            "—",
            "Within 30 days",
            new Color(255, 239, 216),
            UiStyle.ORANGE,
            "SELECT COUNT(*) FROM STOCK WHERE expiry_date >= CURDATE() AND expiry_date <= DATE_ADD(CURDATE(), INTERVAL 30 DAY)"
        ));

        metrics.add(metric(
            "EXPIRED BATCHES",
            "—",
            "Requires attention",
            new Color(252, 229, 228),
            UiStyle.RED,
            "SELECT COUNT(*) FROM STOCK WHERE expiry_date < CURDATE()"
        ));

        body.add(metrics, BorderLayout.NORTH);

        // LOWER PANELS

        JPanel lower = new JPanel(
            new GridLayout(1, 2, 18, 0)
        );

        lower.setOpaque(false);

        // INVENTORY SNAPSHOT

        JPanel stock = UiStyle.whiteCard();

        stock.add(
            sectionTitle(
                "Inventory snapshot",
                "Current stock by medicine and batch"
            ),
            BorderLayout.NORTH
        );

        stock.add(
            tablePanel(
                "SELECT m.medicine_name, s.batch_number, s.quantity, s.expiry_date, CASE WHEN s.expiry_date < CURDATE() THEN 'Expired' WHEN s.quantity <= s.minimum_quantity THEN 'Low stock' WHEN s.expiry_date <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) THEN 'Expiring soon' ELSE 'In stock' END AS status FROM MEDICINE m JOIN STOCK s ON m.medicine_id=s.medicine_id ORDER BY s.expiry_date LIMIT 8",
                new String[]{
                    "Medicine",
                    "Batch",
                    "Qty",
                    "Expiry",
                    "Status"
                }
            ),
            BorderLayout.CENTER
        );

        // RECENT ACTIVITY

        JPanel activity = UiStyle.whiteCard();

        activity.add(
            sectionTitle(
                "Recent stock activity",
                "Latest inventory changes"
            ),
            BorderLayout.NORTH
        );

        activity.add(
            tablePanel(
                "SELECT m.medicine_name, h.action_type, h.quantity_changed, h.transaction_date FROM STOCK_HISTORY h JOIN STOCK s ON h.stock_id=s.stock_id JOIN MEDICINE m ON s.medicine_id=m.medicine_id ORDER BY h.transaction_date DESC LIMIT 8",
                new String[]{
                    "Medicine",
                    "Action",
                    "Change",
                    "Date"
                }
            ),
            BorderLayout.CENTER
        );

        lower.add(stock);
        lower.add(activity);

        body.add(lower, BorderLayout.CENTER);

        // REFRESH

        JPanel footer = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 0, 0)
        );

        footer.setOpaque(false);

        JButton refresh = new JButton("Refresh dashboard");

        UiStyle.styleButton(refresh, true);

        refresh.addActionListener(e -> showPage("Dashboard"));

        footer.add(refresh);

        body.add(footer, BorderLayout.SOUTH);

        p.add(body, BorderLayout.CENTER);

        return p;
    }

    // ================= SUMMARY CARDS =================

    private JPanel metric(
        String title,
        String fallback,
        String detail,
        Color tint,
        Color accent,
        String sql
    ) {

        JPanel card = UiStyle.whiteCard();

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    UiStyle.BORDER
                ),
                new EmptyBorder(17, 15, 17, 15)
            )
        );

        // TOP ACCENT

        JPanel accentBar = new JPanel();

        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(0, 5));

        card.add(accentBar, BorderLayout.NORTH);

        JPanel inner = new JPanel();

        inner.setOpaque(false);

        inner.setLayout(
            new BoxLayout(inner, BoxLayout.Y_AXIS)
        );

        JLabel t = new JLabel(title);

        t.setFont(
            new Font("Segoe UI", Font.BOLD, 10)
        );

        t.setForeground(UiStyle.TEXT);

        JLabel value = new JLabel(fallback);

        value.setFont(
            new Font("Segoe UI", Font.BOLD, 31)
        );

        value.setForeground(accent);

        JLabel d = new JLabel(detail);

        d.setFont(
            new Font("Segoe UI", Font.PLAIN, 11)
        );

        d.setForeground(UiStyle.TEXT);

        JPanel iconArea = new JPanel(new BorderLayout());

        iconArea.setBackground(tint);
        iconArea.setPreferredSize(new Dimension(38, 6));

        inner.add(t);
        inner.add(Box.createVerticalStrut(15));

        inner.add(value);
        inner.add(Box.createVerticalStrut(7));

        inner.add(d);
        inner.add(Box.createVerticalStrut(13));

        inner.add(iconArea);

        card.add(inner, BorderLayout.CENTER);

        // LOAD DATABASE VALUE

        try (
            Connection c = DBConnection.getConnection();
            Statement st = c == null ? null : c.createStatement();
            ResultSet rs = st == null ? null : st.executeQuery(sql)
        ) {

            if (rs != null && rs.next()) {

                value.setText(
                    String.valueOf(rs.getLong(1))
                );
            }

        } catch (Exception ex) {

            value.setText("—");

            value.setToolTipText(
                "Could not load data: " + ex.getMessage()
            );
        }

        return card;
    }

    // ================= INVENTORY PAGES =================

    private JPanel buildInventoryPage() {

        return dataPage(
            "Medicine Stock",
            "All registered medicines, batches, quantities and expiry dates.",
            "SELECT m.medicine_id, m.medicine_name, m.category, s.stock_id, s.batch_number, s.quantity, s.expiry_date, s.minimum_quantity FROM MEDICINE m JOIN STOCK s ON m.medicine_id=s.medicine_id ORDER BY m.medicine_name",
            new String[]{
                "Medicine ID", "Medicine", "Category",
                "Stock ID", "Batch", "Quantity",
                "Expiry", "Minimum"
            }
        );
    }

    private JPanel buildExpiryPage() {

        return dataPage(
            "Expiry Tracking",
            "Monitor batches approaching their expiry date.",
            "SELECT m.medicine_name, s.batch_number, s.quantity, s.expiry_date, DATEDIFF(s.expiry_date,CURDATE()) AS days_left FROM MEDICINE m JOIN STOCK s ON m.medicine_id=s.medicine_id ORDER BY s.expiry_date",
            new String[]{
                "Medicine", "Batch", "Quantity",
                "Expiry date", "Days left"
            }
        );
    }

    private JPanel buildLowStockPage() {

        return dataPage(
            "Low Stock Alerts",
            "Batches at or below their minimum quantity threshold.",
            "SELECT m.medicine_name, s.batch_number, s.quantity, s.minimum_quantity FROM MEDICINE m JOIN STOCK s ON m.medicine_id=s.medicine_id WHERE s.quantity <= s.minimum_quantity ORDER BY s.quantity",
            new String[]{
                "Medicine", "Batch", "Available", "Minimum"
            }
        );
    }

    private JPanel buildExpiredPage() {

        return dataPage(
            "Expired Stock Report",
            "Expired batches that should be removed from usable inventory.",
            "SELECT m.medicine_name, s.batch_number, s.quantity, s.expiry_date FROM MEDICINE m JOIN STOCK s ON m.medicine_id=s.medicine_id WHERE s.expiry_date < CURDATE() ORDER BY s.expiry_date",
            new String[]{
                "Medicine", "Batch", "Quantity", "Expiry date"
            }
        );
    }

    private JPanel buildHistoryPage() {

        return dataPage(
            "Stock History",
            "Audit trail of stock quantity changes.",
            "SELECT h.stock_id, m.medicine_name, h.staff_id, h.action_type, h.quantity_changed, h.transaction_date, h.remarks FROM STOCK_HISTORY h JOIN STOCK s ON h.stock_id=s.stock_id JOIN MEDICINE m ON s.medicine_id=m.medicine_id ORDER BY h.transaction_date DESC",
            new String[]{
                "Stock ID", "Medicine", "Staff ID",
                "Action", "Change", "Date", "Remarks"
            }
        );
    }

    // ================= DATA TABLE PAGE =================

    private JPanel dataPage(
        String title,
        String subtitle,
        String sql,
        String[] columns
    ) {

        JPanel p = UiStyle.page(title, subtitle);

        JPanel card = UiStyle.whiteCard();

        card.add(
            tablePanel(sql, columns),
            BorderLayout.CENTER
        );

        JButton refresh = new JButton("Refresh");

        UiStyle.styleButton(refresh, false);

        refresh.addActionListener(e -> showPage(title));

        JPanel tools = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 0, 0)
        );

        tools.setOpaque(false);
        tools.add(refresh);

        card.add(tools, BorderLayout.SOUTH);

        p.add(card, BorderLayout.CENTER);

        return p;
    }

    // ================= TABLE DESIGN =================

    private JPanel tablePanel(
        String sql,
        String[] columns,
        Object... parameters
    ) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(Color.WHITE);

        DefaultTableModel model = new DefaultTableModel(
            columns,
            0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);

        table.setFont(
            new Font("Segoe UI", Font.PLAIN, 12)
        );

        table.setRowHeight(34);

        table.setForeground(UiStyle.TEXT);
        table.setBackground(Color.WHITE);

        table.setGridColor(
            new Color(232, 239, 237)
        );

        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        table.setSelectionBackground(
            TEAL_LIGHT
        );

        table.setSelectionForeground(
            UiStyle.NAVY
        );

        table.setFillsViewportHeight(false);

        table.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 12)
        );

        table.getTableHeader().setForeground(
            UiStyle.NAVY
        );

        table.getTableHeader().setBackground(
            new Color(237, 245, 243)
        );

        table.getTableHeader().setPreferredSize(
            new Dimension(0, 39)
        );

        table.getTableHeader().setReorderingAllowed(false);

        try (
            Connection c = DBConnection.getConnection();
            PreparedStatement ps =
                c == null ? null : c.prepareStatement(sql)
        ) {

            if (ps != null) {

                for (int i = 0; i < parameters.length; i++) {

                    ps.setObject(i + 1, parameters[i]);
                }

                try (ResultSet rs = ps.executeQuery()) {

                    ResultSetMetaData md = rs.getMetaData();

                    int n = Math.min(
                        md.getColumnCount(),
                        columns.length
                    );

                    while (rs.next()) {

                        Vector<Object> row = new Vector<>();

                        for (int i = 1; i <= n; i++) {

                            row.add(rs.getObject(i));
                        }

                        model.addRow(row);
                    }
                }
            }

        } catch (Exception ex) {

            JLabel error = new JLabel(
                "Unable to load records: " + ex.getMessage()
            );

            error.setForeground(UiStyle.RED);

            panel.add(error, BorderLayout.NORTH);
        }

        if (model.getRowCount() == 0) {

            JLabel empty = new JLabel(
                "No records to display."
            );

            empty.setFont(UiStyle.FONT);
            empty.setForeground(UiStyle.MUTED);

            empty.setBorder(
                new EmptyBorder(12, 4, 12, 4)
            );

            panel.add(empty, BorderLayout.NORTH);
        }

        JScrollPane scroll = new JScrollPane(table);

        scroll.setBorder(
            BorderFactory.createLineBorder(
                UiStyle.BORDER
            )
        );

        scroll.getViewport().setBackground(Color.WHITE);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // ================= ADD MEDICINE =================

    private JPanel buildAddMedicinePage() {

        JPanel p = UiStyle.page(
            "Add Medicine",
            "Register a new medicine in the campus first-aid catalogue."
        );

        JTextField name = UiStyle.field(28);
        JTextField category = UiStyle.field(28);
        JTextField description = UiStyle.field(28);

        JPanel form = formCard();

        addField(form, "Medicine name", name);
        addField(form, "Category", category);
        addField(form, "Description", description);

        JButton save = new JButton("Save medicine");

        UiStyle.styleButton(save, true);

        save.addActionListener(e -> {

            if (
                name.getText().isBlank()
                || category.getText().isBlank()
            ) {

                warn("Medicine name and category are required.");
                return;
            }

            if (
                MedicineDAO.addMedicine(
                    name.getText().trim(),
                    category.getText().trim(),
                    description.getText().trim()
                )
            ) {

                JOptionPane.showMessageDialog(
                    this,
                    "Medicine added successfully."
                );

                name.setText("");
                category.setText("");
                description.setText("");

            } else {

                warn(
                    "Could not add medicine. Check the database and ensure the medicine details are valid."
                );
            }
        });

        form.add(save);

        p.add(form, BorderLayout.CENTER);

        return p;
    }

    // ================= ADD STOCK =================

    private JPanel buildAddStockPage() {

        JPanel p = UiStyle.page(
            "Add Medicine Stock",
            "Add a new batch for an existing medicine."
        );

        JTextField med = UiStyle.field(24);
        JTextField batch = UiStyle.field(24);
        JTextField qty = UiStyle.field(24);
        JTextField expiry = UiStyle.field(24);
        JTextField min = UiStyle.field(24);

        expiry.setText(
            LocalDate.now().plusYears(1).toString()
        );

        JPanel form = formCard();

        addField(form, "Medicine ID", med);
        addField(form, "Batch number", batch);
        addField(form, "Quantity", qty);
        addField(form, "Expiry date (YYYY-MM-DD)", expiry);
        addField(form, "Minimum quantity", min);

        JButton save = new JButton("Add stock batch");

        UiStyle.styleButton(save, true);

        save.addActionListener(e -> {

            try {

                int id = Integer.parseInt(med.getText().trim());
                int q = Integer.parseInt(qty.getText().trim());
                int m = Integer.parseInt(min.getText().trim());

                LocalDate d = LocalDate.parse(
                    expiry.getText().trim()
                );

                if (
                    batch.getText().isBlank()
                    || q <= 0
                    || m < 0
                    || d.isBefore(LocalDate.now())
                ) {

                    warn(
                        "Enter a batch number, positive quantity, non-negative minimum and a valid future expiry date."
                    );

                    return;
                }

                if (!MedicineDAO.medicineExists(id)) {

                    warn("Medicine ID was not found.");
                    return;
                }

                if (
                    StockDAO.addStock(
                        id,
                        batch.getText().trim(),
                        q,
                        d,
                        m
                    )
                ) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Stock batch added successfully."
                    );

                    med.setText("");
                    batch.setText("");
                    qty.setText("");
                    min.setText("");

                } else {

                    warn("Could not add stock batch.");
                }

            } catch (Exception ex) {

                warn(
                    "Check the medicine ID, quantities and date format (YYYY-MM-DD)."
                );
            }
        });

        form.add(save);

        p.add(form, BorderLayout.CENTER);

        return p;
    }

    // ================= UPDATE STOCK =================

    private JPanel buildUpdateStockPage() {

        JPanel p = UiStyle.page(
            "Update Stock Quantity",
            "Adjust a batch quantity and record the change in stock history."
        );

        JTextField stock = UiStyle.field(24);
        JTextField qty = UiStyle.field(24);
        JTextField staff = UiStyle.field(24);
        JTextField remarks = UiStyle.field(24);

        JPanel form = formCard();

        addField(form, "Stock ID", stock);
        addField(form, "New quantity", qty);
        addField(form, "Staff ID", staff);
        addField(form, "Remarks", remarks);

        JButton update = new JButton("Update quantity");

        UiStyle.styleButton(update, true);

        update.addActionListener(e -> {

            try {

                int id = Integer.parseInt(stock.getText().trim());
                int q = Integer.parseInt(qty.getText().trim());
                int sid = Integer.parseInt(staff.getText().trim());

                if (q < 0) {

                    warn("Quantity cannot be negative.");
                    return;
                }

                if (
                    StockDAO.updateStockQuantity(
                        id,
                        q,
                        sid,
                        remarks.getText().trim()
                    )
                ) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Stock quantity updated and history recorded."
                    );

                    stock.setText("");
                    qty.setText("");
                    remarks.setText("");

                } else {

                    warn(
                        "Stock update failed. Verify the stock ID and staff ID."
                    );
                }

            } catch (Exception ex) {

                warn(
                    "Enter valid whole numbers for stock ID, quantity and staff ID."
                );
            }
        });

        form.add(update);

        p.add(form, BorderLayout.CENTER);

        return p;
    }

    // ================= SEARCH MEDICINE =================

    private JPanel buildSearchPage() {

        JPanel p = UiStyle.page(
            "Search Medicine",
            "Find medicines by name and view their catalogue details."
        );

        JPanel card = UiStyle.whiteCard();

        JTextField search = UiStyle.field(28);

        JButton go = new JButton("Search");

        UiStyle.styleButton(go, true);

        JPanel top = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 8, 0)
        );

        top.setOpaque(false);

        top.add(search);
        top.add(go);

        card.add(top, BorderLayout.NORTH);

        JPanel results = new JPanel(new BorderLayout());

        results.setBackground(Color.WHITE);

        card.add(results, BorderLayout.CENTER);

        Runnable run = () -> {

            results.removeAll();

            String q = search.getText().trim();

            if (q.isEmpty()) {

                warn("Enter a medicine name to search.");
                return;
            }

            results.add(
                tablePanel(
                    "SELECT medicine_id, medicine_name, category, description FROM MEDICINE WHERE medicine_name LIKE ? ORDER BY medicine_name",
                    new String[]{
                        "Medicine ID",
                        "Medicine",
                        "Category",
                        "Description"
                    },
                    "%" + q + "%"
                ),
                BorderLayout.CENTER
            );

            results.revalidate();
            results.repaint();
        };

        go.addActionListener(e -> run.run());

        search.addActionListener(e -> run.run());

        p.add(card, BorderLayout.CENTER);

        return p;
    }

    // ================= FORM DESIGN =================

    private JPanel formCard() {

        JPanel outer = new JPanel(new GridBagLayout());

        outer.setOpaque(false);

        JPanel card = UiStyle.whiteCard();

        card.setLayout(new GridBagLayout());

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiStyle.BORDER),
                new EmptyBorder(25, 28, 25, 28)
            )
        );

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;

        c.anchor = GridBagConstraints.WEST;

        c.insets = new Insets(0, 0, 17, 0);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        outer.add(card, c);

        return card;
    }

    private void addField(
        JPanel form,
        String label,
        JComponent field
    ) {

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = GridBagLayoutInfo.next(form);

        c.anchor = GridBagConstraints.WEST;

        c.insets = new Insets(0, 0, 7, 0);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        JLabel l = UiStyle.label(label);

        l.setFont(UiStyle.BOLD);

        form.add(l, c);

        c.gridy = GridBagLayoutInfo.next(form);

        c.insets = new Insets(0, 0, 15, 0);

        form.add(field, c);
    }

    // ================= SECTION HEADING =================

    private JPanel sectionTitle(
        String title,
        String subtitle
    ) {

        JPanel p = new JPanel();

        p.setOpaque(false);

        p.setLayout(
            new BoxLayout(p, BoxLayout.Y_AXIS)
        );

        JLabel t = new JLabel(title);

        t.setFont(
            new Font("Segoe UI", Font.BOLD, 17)
        );

        t.setForeground(UiStyle.NAVY);

        JLabel s = new JLabel(subtitle);

        s.setFont(
            new Font("Segoe UI", Font.PLAIN, 12)
        );

        s.setForeground(UiStyle.MUTED);

        p.add(t);
        p.add(Box.createVerticalStrut(5));
        p.add(s);

        return p;
    }

    // ================= WARNING MESSAGE =================

    private void warn(String message) {

        JOptionPane.showMessageDialog(
            this,
            message,
            "Please check",
            JOptionPane.WARNING_MESSAGE
        );
    }

    // ================= GRID BAG ROW TRACKER =================

    private static final class GridBagLayoutInfo {

        private static final java.util.Map<Container, Integer> rows =
            new java.util.WeakHashMap<>();

        static int next(Container c) {

            int n = rows.getOrDefault(c, 0);

            rows.put(c, n + 1);

            return n;
        }
    }
}