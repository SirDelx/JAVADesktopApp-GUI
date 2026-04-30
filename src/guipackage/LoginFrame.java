import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login");
        setSize(420, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        add(buildLeftPanel(), BorderLayout.WEST);
        add(buildFormPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel buildLeftPanel() {
        JPanel left = new JPanel(null);
        left.setBackground(new Color(30, 55, 95));
        left.setPreferredSize(new Dimension(10, 0));
        return left;
    }

    private JPanel buildFormPanel() {
        JPanel form = new JPanel(null);
        form.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 55, 95));
        titleLabel.setBounds(40, 55, 300, 35);
        form.add(titleLabel);

        JLabel subLabel = new JLabel("Sign in to your account");
        subLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subLabel.setForeground(new Color(120, 130, 150));
        subLabel.setBounds(40, 92, 300, 20);
        form.add(subLabel);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setBounds(40, 125, 320, 2);
        sep.setForeground(new Color(230, 235, 245));
        form.add(sep);

        // Email
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        emailLabel.setForeground(new Color(70, 80, 100));
        emailLabel.setBounds(40, 145, 300, 20);
        form.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(40, 168, 320, 38);
        styleField(emailField);
        form.add(emailField);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passLabel.setForeground(new Color(70, 80, 100));
        passLabel.setBounds(40, 222, 300, 20);
        form.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 245, 320, 38);
        styleField(passwordField);
        form.add(passwordField);

        // Show/hide password toggle
        JCheckBox showPass = new JCheckBox("Show password");
        showPass.setFont(new Font("Arial", Font.PLAIN, 11));
        showPass.setForeground(new Color(100, 110, 130));
        showPass.setBackground(Color.WHITE);
        showPass.setBounds(40, 288, 150, 22);
        showPass.addActionListener(e -> {
            passwordField.setEchoChar(showPass.isSelected() ? (char) 0 : '•');
        });
        form.add(showPass);

        // Login button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(40, 330, 320, 42);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setBackground(new Color(30, 55, 95));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> loginUser());
        form.add(loginBtn);

        // Back link
        JLabel backLabel = new JLabel("← Back to Welcome", SwingConstants.CENTER);
        backLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        backLabel.setForeground(new Color(30, 55, 95));
        backLabel.setBounds(40, 390, 320, 22);
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

    private void loginUser() {
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE email = ? AND password = ?"
            );
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int    userId     = rs.getInt("id");
                String username   = rs.getString("username");
                int    userTypeId = rs.getInt("user_type_id");
                switch (userTypeId) {
                    case 1 -> new InstructorDashboard(username);
                    case 2 -> new StudentDashboard(userId, username);
                    case 3 -> new StaffDashboard(username);
                    default -> new DashboardFrame(username);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
