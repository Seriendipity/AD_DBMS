package GUI;

import javax.swing.*;
import java.awt.*;

public class Connect_GUI extends JFrame {

    public Connect_GUI() {
        setTitle("Connect");
        setSize(550, 80);
        setLocationRelativeTo(null);
        setBackground(Color.white);
        setVisible(true);

        //网格袋布局
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 40;
        JPanel panel = new JPanel();

        JLabel label1 = new JLabel("数据库名称：");

        JTextField databaseName = new JTextField();
        databaseName.setPreferredSize(new Dimension(300,30));

        JButton connectButton = new JButton("连接");
        connectButton.setPreferredSize(new Dimension(100,30));

        //将组件添加到布局管理器

        gridBagLayout.addLayoutComponent(label1,c);
        gridBagLayout.addLayoutComponent(databaseName,c);
        gridBagLayout.addLayoutComponent(connectButton,c);

        // 将组件添加到界面中
        panel.add(label1);
        panel.add(databaseName);
        panel.add(connectButton);

        setContentPane(panel);

    }
}
