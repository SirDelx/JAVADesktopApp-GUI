import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentDashboard extends JFrame {

    private static final Color SIDEBAR_BG   = new Color(30, 55, 95);
    private static final Color SIDEBAR_HOVER = new Color(50, 85, 140);
    private static final Color SIDEBAR_TEXT  = Color.WHITE;
    private static final Color HEADER_BG    = new Color(245, 247, 250);
    private static final Color CONTENT_BG   = new Color(255, 255, 255);

    private JPanel contentArea;
    private CardLayout cardLayout;
    private JButton activeMenuBtn;
    private int userId;

    public StudentDashboard(int userId, String username) {
        this.userId = userId;
        setTitle("Student Dashboard");
        setSize(900, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildHeader(username), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel buildHeader(String username) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setPreferredSize(new Dimension(0, 55));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 215, 225)));

        JLabel title = new JLabel("  Student Portal");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(30, 55, 95));
        header.add(title, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        right.setBackground(HEADER_BG);

        JLabel userLabel = new JLabel("Logged in as: " + username);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setForeground(new Color(80, 90, 110));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            new WelcomeFrame();
            dispose();
        });

        right.add(userLabel);
        right.add(logoutBtn);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel menuTitle = new JLabel("  MENU");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 11));
        menuTitle.setForeground(new Color(160, 180, 210));
        menuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuTitle.setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 0));
        sidebar.add(menuTitle);

        String[] menus = {"Dashboard", "My Courses", "Grades", "Schedule", "Profile"};
        String[] cards  = {"dashboard", "courses", "grades", "schedule", "profile"};

        for (int i = 0; i < menus.length; i++) {
            JButton btn = createMenuButton(menus[i], cards[i]);
            sidebar.add(btn);
            if (i == 0) {
                setActiveButton(btn);
                activeMenuBtn = btn;
            }
        }

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton createMenuButton(String label, String cardName) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setForeground(SIDEBAR_TEXT);
        btn.setBackground(SIDEBAR_BG);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(200, 42));
        btn.setPreferredSize(new Dimension(200, 42));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != activeMenuBtn) btn.setBackground(SIDEBAR_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeMenuBtn) btn.setBackground(SIDEBAR_BG);
            }
        });

        btn.addActionListener(e -> {
            if (activeMenuBtn != null) {
                activeMenuBtn.setBackground(SIDEBAR_BG);
                activeMenuBtn.setFont(new Font("Arial", Font.PLAIN, 13));
            }
            setActiveButton(btn);
            activeMenuBtn = btn;
            cardLayout.show(contentArea, cardName);
        });

        return btn;
    }

    private void setActiveButton(JButton btn) {
        btn.setBackground(SIDEBAR_HOVER);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
    }

    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(CONTENT_BG);

        contentArea.add(buildDashboardPanel(), "dashboard");
        contentArea.add(buildPlaceholder("My Courses",  "View your enrolled courses here."), "courses");
        contentArea.add(buildPlaceholder("Grades",      "Your grades and academic records."), "grades");
        contentArea.add(buildPlaceholder("Schedule",    "Your class schedule and timetable."), "schedule");
        contentArea.add(buildProfilePanel(), "profile");

        return contentArea;
    }

    private JPanel buildDashboardPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(CONTENT_BG);

        JLabel heading = new JLabel("Dashboard Overview");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(30, 55, 95));
        heading.setBounds(30, 30, 400, 35);
        panel.add(heading);

        JLabel sub = new JLabel("Here's a quick summary of your academic activity.");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(new Color(100, 110, 130));
        sub.setBounds(30, 68, 500, 20);
        panel.add(sub);

        String[][] cards = {
            {"Enrolled Courses", "0"},
            {"Pending Grades",   "0"},
            {"Upcoming Classes", "0"}
        };

        for (int i = 0; i < cards.length; i++) {
            panel.add(buildStatCard(cards[i][0], cards[i][1], 30 + i * 195, 110));
        }

        return panel;
    }

    private JPanel buildStatCard(String title, String value, int x, int y) {
        JPanel card = new JPanel(null);
        card.setBounds(x, y, 175, 100);
        card.setBackground(new Color(245, 247, 252));
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));

        JLabel valLabel = new JLabel(value, SwingConstants.CENTER);
        valLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valLabel.setForeground(new Color(30, 55, 95));
        valLabel.setBounds(0, 18, 175, 35);
        card.add(valLabel);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        titleLabel.setForeground(new Color(100, 110, 130));
        titleLabel.setBounds(0, 58, 175, 20);
        card.add(titleLabel);

        return card;
    }

    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(CONTENT_BG);

        JLabel heading = new JLabel("My Profile");
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(30, 55, 95));
        heading.setBounds(30, 30, 400, 35);
        panel.add(heading);

        JLabel sub = new JLabel("Edit your information below and click Update to save.");
        sub.setFont(new Font("Arial", Font.PLAIN, 12));
        sub.setForeground(new Color(100, 110, 130));
        sub.setBounds(30, 65, 500, 20);
        panel.add(sub);

        JPanel card = new JPanel(null);
        card.setBounds(30, 95, 560, 415);
        card.setBackground(new Color(245, 247, 252));
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        panel.add(card);

        // --- read-only values ---
        String[] readLabels = {"User ID", "User Type", "Course", "College"};
        JLabel[] readVals   = new JLabel[readLabels.length];

        // --- editable fields ---
        JTextField usernameField = new JTextField();
        JTextField emailField    = new JTextField();
        JPasswordField passField = new JPasswordField();

        // fetch current data
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT u.id, u.username, u.email, " +
                "ut.user_description, c.course_name, col.college_name " +
                "FROM users u " +
                "LEFT JOIN user_type ut ON u.user_type_id = ut.user_type_id " +
                "LEFT JOIN course c ON u.course_id = c.course_id " +
                "LEFT JOIN college col ON c.college_id = col.college_id " +
                "WHERE u.id = ?"
            );
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                readVals[0] = new JLabel(String.valueOf(rs.getInt("id")));
                readVals[1] = new JLabel(rs.getString("user_description") != null ? rs.getString("user_description") : "—");
                readVals[2] = new JLabel(rs.getString("course_name")       != null ? rs.getString("course_name")       : "—");
                readVals[3] = new JLabel(rs.getString("college_name")      != null ? rs.getString("college_name")      : "—");
                usernameField.setText(rs.getString("username"));
                emailField.setText(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            for (int i = 0; i < readVals.length; i++) readVals[i] = new JLabel("—");
        }

        // row layout helper
        String[] rowLabels  = {"User ID", "Username", "Email", "New Password", "User Type", "Course", "College"};
        int rowCount = rowLabels.length;

        for (int i = 0; i < rowCount; i++) {
            int y = 18 + i * 48;

            JLabel lbl = new JLabel(rowLabels[i] + ":");
            lbl.setFont(new Font("Arial", Font.BOLD, 12));
            lbl.setForeground(new Color(80, 90, 110));
            lbl.setBounds(20, y + 4, 120, 25);
            card.add(lbl);

            switch (i) {
                case 0 -> {   // User ID — read-only
                    styleReadLabel(readVals[0]);
                    readVals[0].setBounds(150, y + 4, 370, 25);
                    card.add(readVals[0]);
                }
                case 1 -> {   // Username — editable
                    styleField(usernameField);
                    usernameField.setBounds(150, y, 370, 30);
                    card.add(usernameField);
                }
                case 2 -> {   // Email — editable
                    styleField(emailField);
                    emailField.setBounds(150, y, 370, 30);
                    card.add(emailField);
                }
                case 3 -> {   // New Password — editable
                    styleField(passField);
                    passField.setBounds(150, y, 370, 30);
                    passField.setToolTipText("Leave blank to keep current password");
                    card.add(passField);

                    JLabel hint = new JLabel("Leave blank to keep current password");
                    hint.setFont(new Font("Arial", Font.ITALIC, 10));
                    hint.setForeground(new Color(150, 160, 175));
                    hint.setBounds(150, y + 31, 370, 14);
                    card.add(hint);
                }
                case 4 -> {   // User Type — read-only
                    styleReadLabel(readVals[1]);
                    readVals[1].setBounds(150, y + 4, 370, 25);
                    card.add(readVals[1]);
                }
                case 5 -> {   // Course — read-only
                    styleReadLabel(readVals[2]);
                    readVals[2].setBounds(150, y + 4, 370, 25);
                    card.add(readVals[2]);
                }
                case 6 -> {   // College — read-only
                    styleReadLabel(readVals[3]);
                    readVals[3].setBounds(150, y + 4, 370, 25);
                    card.add(readVals[3]);
                }
            }
        }

        // Update button — placed inside card below the last row
        JButton updateBtn = new JButton("Update Profile");
        updateBtn.setFont(new Font("Arial", Font.BOLD, 13));
        updateBtn.setBackground(new Color(30, 55, 95));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        updateBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        updateBtn.setBounds(20, 360, 160, 38);
        card.add(updateBtn);

        updateBtn.addActionListener(e -> {
            String newUsername = usernameField.getText().trim();
            String newEmail    = emailField.getText().trim();
            String newPassword = new String(passField.getPassword()).trim();

            if (newUsername.isEmpty() || newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Username and Email cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                if (newPassword.isEmpty()) {
                    PreparedStatement ps = conn.prepareStatement(
                        "UPDATE users SET username = ?, email = ? WHERE id = ?"
                    );
                    ps.setString(1, newUsername);
                    ps.setString(2, newEmail);
                    ps.setInt(3, userId);
                    ps.executeUpdate();
                } else {
                    PreparedStatement ps = conn.prepareStatement(
                        "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?"
                    );
                    ps.setString(1, newUsername);
                    ps.setString(2, newEmail);
                    ps.setString(3, newPassword);
                    ps.setInt(4, userId);
                    ps.executeUpdate();
                }
                passField.setText("");
                JOptionPane.showMessageDialog(panel, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Failed to update profile.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 225)),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
    }

    private void styleReadLabel(JLabel lbl) {
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(new Color(30, 55, 95));
    }

    private JPanel buildPlaceholder(String title, String description) {
        JPanel panel = new JPanel(null);
        panel.setBackground(CONTENT_BG);

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        heading.setForeground(new Color(30, 55, 95));
        heading.setBounds(30, 30, 400, 35);
        panel.add(heading);

        JLabel desc = new JLabel(description);
        desc.setFont(new Font("Arial", Font.PLAIN, 13));
        desc.setForeground(new Color(100, 110, 130));
        desc.setBounds(30, 68, 500, 20);
        panel.add(desc);

        return panel;
    }
}
