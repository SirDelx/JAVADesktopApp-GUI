import javax.swing.*;
import java.awt.*;

public class StaffDashboard extends JFrame {

    public StaffDashboard(String username) {
        setTitle("Staff Dashboard");
        setSize(600, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel roleLabel = new JLabel("Staff Dashboard", SwingConstants.CENTER);
        roleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roleLabel.setBounds(0, 20, 600, 30);
        add(roleLabel);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setBounds(0, 60, 600, 25);
        add(welcomeLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(240, 310, 120, 35);
        add(logoutBtn);

        logoutBtn.addActionListener(e -> {
            new WelcomeFrame();
            dispose();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
