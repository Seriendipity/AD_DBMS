package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login_GUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login_GUI() {
        setTitle("登录界面");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("账号:");
        panel.add(usernameLabel, constraints);

        usernameField = new JTextField(20);
        constraints.gridy = 1;
        panel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("密码:");
        constraints.gridy = 2;
        panel.add(passwordLabel, constraints);

        passwordField = new JPasswordField(20);
        constraints.gridy = 3;
        panel.add(passwordField, constraints);

        JButton loginButton = new JButton("登录");
        constraints.gridy = 4;
        panel.add(loginButton, constraints);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loging();
            }
        });

        add(panel);
        setLocationRelativeTo(null); // 将窗口显示在屏幕中央
    }
    //这是调用登录方法
    private void loging() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Login_GUI frame = new Login_GUI();
                frame.setVisible(true);
            }
        });
    }
}
