import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        setTitle("Welcome");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Welcome to the System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(50, 60, 300, 40);
        add(titleLabel);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(100, 140, 180, 35);
        add(loginBtn);

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(100, 190, 180, 35);
        add(signupBtn);

        loginBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        signupBtn.addActionListener(e -> {
            new SignupFrame();
            dispose();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
