package GUI;
import SqlFunction.CreateTable;
import SqlFunction.Judge;
import SqlFunction.UseUser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Login_GUI extends JFrame {

    public Login_GUI() {
        setTitle("登录");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel subPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件之间的间距

        // 添加名称标签和文本框
        JLabel nameLabel = new JLabel("用户名:");
        subPanel.add(nameLabel, gbc);

        gbc.gridy++;
        JTextField nameTextField = new JTextField(20);
        subPanel.add(nameTextField, gbc);

        // 添加密码标签和密码框
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("密码:");
        subPanel.add(passwordLabel, gbc);

        gbc.gridy++;
        JPasswordField passwordField = new JPasswordField(20);
        subPanel.add(passwordField, gbc);

        JButton okButton = new JButton("登录");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String password = new String(passwordField.getPassword());
                //这里调用登录的方法
                try {
                    useUser(name,password);
                } catch (DocumentException ex) {
                    ex.printStackTrace();
                    // 处理异常
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JButton regisButton = new JButton("前往注册");
        regisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Register_GUI();
            }
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(regisButton);

        getContentPane().add(subPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    public void useUser(String userName, String password) throws DocumentException, IOException {
        if (Judge.findUser(userName)) {
            if (Judge.isUser(userName, password)) {
                UseUser.userName = userName;
                JOptionPane.showMessageDialog(null, "登录成功");
                DBMS_GUI.setDBLabel(UseUser.userName+"的"+"\n"+"数据库资源管理器");
            } else {
                JOptionPane.showMessageDialog(null, "密码错误");
                new Login_GUI();
            }
        } else {
            JOptionPane.showMessageDialog(null, "用户不存在,请先注册");
            new Register_GUI();
        }
    }
}
