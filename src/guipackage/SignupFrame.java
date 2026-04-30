import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignupFrame extends JFrame {

    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo, collegeCombo, courseCombo;
    private List<Integer> userTypeIds = new ArrayList<>();
    private List<Integer> collegeIds  = new ArrayList<>();
    private List<Integer> courseIds   = new ArrayList<>();

    public SignupFrame() {
        setTitle("Sign Up");
        setSize(820, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        add(buildLeftPanel(),  BorderLayout.WEST);
        add(buildFormPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    // ── Left accent panel ────────────────────────────────────────────────────

    private JPanel buildLeftPanel() {
        JPanel left = new JPanel(null);
        left.setBackground(new Color(30, 55, 95));
        left.setPreferredSize(new Dimension(220, 0));

        JLabel title = new JLabel("Student Portal");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 160, 220, 35);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(title);

        JLabel sub = new JLabel("<html><center>Create your account<br>to get started</center></html>");
        sub.setFont(new Font("Arial", Font.PLAIN, 13));
        sub.setForeground(new Color(170, 190, 220));
        sub.setBounds(20, 200, 180, 50);
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        left.add(sub);

        return left;
    }

    // ── Right form panel ─────────────────────────────────────────────────────

    private JPanel buildFormPanel() {
        JPanel form = new JPanel(null);
        form.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Create Account");
        heading.setFont(new Font("Arial", Font.BOLD, 22));
        heading.setForeground(new Color(30, 55, 95));
        heading.setBounds(40, 35, 320, 35);
        form.add(heading);

        JLabel subHeading = new JLabel("Fill in the details below to register");
        subHeading.setFont(new Font("Arial", Font.PLAIN, 12));
        subHeading.setForeground(new Color(120, 130, 150));
        subHeading.setBounds(40, 72, 320, 20);
        form.add(subHeading);

        JSeparator sep = new JSeparator();
        sep.setBounds(40, 102, 500, 1);
        sep.setForeground(new Color(230, 235, 245));
        form.add(sep);

        // ── Row 1: Username + Email ───────────────────────────────────────
        form.add(buildLabel("Username",      40,  118));
        form.add(buildLabel("Email Address", 290, 118));

        usernameField = new JTextField();
        styleField(usernameField);
        usernameField.setBounds(40, 140, 225, 36);
        form.add(usernameField);

        emailField = new JTextField();
        styleField(emailField);
        emailField.setBounds(290, 140, 250, 36);
        form.add(emailField);

        // ── Row 2: Password ───────────────────────────────────────────────
        form.add(buildLabel("Password", 40, 192));

        passwordField = new JPasswordField();
        styleField(passwordField);
        passwordField.setBounds(40, 214, 225, 36);
        form.add(passwordField);

        JCheckBox showPass = new JCheckBox("Show");
        showPass.setFont(new Font("Arial", Font.PLAIN, 11));
        showPass.setForeground(new Color(100, 110, 130));
        showPass.setBackground(Color.WHITE);
        showPass.setBounds(272, 221, 60, 22);
        showPass.addActionListener(e ->
            passwordField.setEchoChar(showPass.isSelected() ? (char) 0 : '•')
        );
        form.add(showPass);

        // ── Row 3: User Type ──────────────────────────────────────────────
        form.add(buildLabel("User Type", 40, 268));

        userTypeCombo = new JComboBox<>();
        styleCombo(userTypeCombo);
        userTypeCombo.setBounds(40, 290, 225, 36);
        loadUserTypes();
        form.add(userTypeCombo);

        // ── Row 4: College + Course ───────────────────────────────────────
        form.add(buildLabel("College", 40,  344));
        form.add(buildLabel("Course",  290, 344));

        collegeCombo = new JComboBox<>();
        styleCombo(collegeCombo);
        collegeCombo.setBounds(40, 366, 225, 36);
        loadColleges();
        form.add(collegeCombo);

        courseCombo = new JComboBox<>();
        styleCombo(courseCombo);
        courseCombo.setBounds(290, 366, 250, 36);
        if (!collegeIds.isEmpty()) loadCourses(collegeIds.get(0));
        form.add(courseCombo);

        collegeCombo.addActionListener(e -> {
            int idx = collegeCombo.getSelectedIndex();
            if (idx >= 0 && idx < collegeIds.size()) loadCourses(collegeIds.get(idx));
        });

        // ── Signup button ─────────────────────────────────────────────────
        JButton signupBtn = new JButton("Create Account");
        signupBtn.setBounds(40, 432, 500, 42);
        signupBtn.setFont(new Font("Arial", Font.BOLD, 14));
        signupBtn.setBackground(new Color(30, 55, 95));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFocusPainted(false);
        signupBtn.setBorderPainted(false);
        signupBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupBtn.addActionListener(e -> signupUser());
        form.add(signupBtn);

        // ── Back link ─────────────────────────────────────────────────────
        JLabel backLabel = new JLabel("← Back to Welcome", SwingConstants.CENTER);
        backLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        backLabel.setForeground(new Color(30, 55, 95));
        backLabel.setBounds(40, 490, 500, 22);
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new WelcomeFrame();
                dispose();
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backLabel.setForeground(new Color(50, 100, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                backLabel.setForeground(new Color(30, 55, 95));
            }
        });
        form.add(backLabel);

        return form;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JLabel buildLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(new Color(70, 80, 100));
        lbl.setBounds(x, y, 220, 20);
        return lbl;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        field.setBackground(new Color(247, 249, 252));
        field.setForeground(new Color(30, 40, 60));
        field.setCaretColor(new Color(30, 55, 95));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 225)),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        combo.setBackground(new Color(247, 249, 252));
        combo.setForeground(new Color(30, 40, 60));
        combo.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 225)));
    }

    // ── DB loaders ────────────────────────────────────────────────────────────

    private void loadUserTypes() {
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement()
                .executeQuery("SELECT user_type_id, user_description FROM user_type");
            while (rs.next()) {
                userTypeIds.add(rs.getInt("user_type_id"));
                userTypeCombo.addItem(rs.getString("user_description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadColleges() {
        try {
            Connection conn = DBConnection.getConnection();
            ResultSet rs = conn.createStatement()
                .executeQuery("SELECT college_id, college_name FROM college");
            while (rs.next()) {
                collegeIds.add(rs.getInt("college_id"));
                collegeCombo.addItem(rs.getString("college_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCourses(int collegeId) {
        courseCombo.removeAllItems();
        courseIds.clear();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT course_id, course_name FROM course WHERE college_id = ?"
            );
            ps.setInt(1, collegeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                courseIds.add(rs.getInt("course_id"));
                courseCombo.addItem(rs.getString("course_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ── Submit ────────────────────────────────────────────────────────────────

    private void signupUser() {
        String username = usernameField.getText().trim();
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (courseIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a valid college and course.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            int userTypeId = userTypeIds.get(userTypeCombo.getSelectedIndex());
            int courseId   = courseIds.get(courseCombo.getSelectedIndex());

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users(username, email, password, user_type_id, course_id) VALUES (?, ?, ?, ?, ?)"
            );
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setInt(4, userTypeId);
            ps.setInt(5, courseId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            new WelcomeFrame();
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Signup failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
