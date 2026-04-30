import javax.swing.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame(String username) {
        setTitle("Dashboard");
        setSize(300, 200);
        setLayout(null);

        JLabel welcome = new JLabel("Welcome, " + username + "!");
        welcome.setBounds(50, 50, 200, 30);
        add(welcome);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}