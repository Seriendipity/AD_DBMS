package GUI;
import SqlFunction.CreateTable;
import SqlFunction.Judge;
import SqlFunction.RecordOperationLog;
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

public class Register_GUI extends JFrame {

    public Register_GUI() {
        setTitle("注册");
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
        JLabel nameLabel = new JLabel("输入您的用户名:");
        subPanel.add(nameLabel, gbc);

        gbc.gridy++;
        JTextField nameTextField = new JTextField(20);
        subPanel.add(nameTextField, gbc);

        // 添加密码标签和密码框
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("您的密码:");
        subPanel.add(passwordLabel, gbc);

        gbc.gridy++;
        JPasswordField passwordField = new JPasswordField(20);
        subPanel.add(passwordField, gbc);

        JButton okButton = new JButton("注册");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String password = new String(passwordField.getPassword());
                //这里调用创建用户的方法
                try {
                    createUser(name, password);

                    dispose();
                } catch (DocumentException | IOException ex) {
                    JOptionPane.showMessageDialog(Register_GUI.this, "创建用户失败: " + ex.getMessage());
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
        JButton regisButton = new JButton("前往登录");
        regisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Login_GUI();
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

    public void createUser(String userName, String password) throws DocumentException, IOException {
        //获取用户的输入
            boolean isLegal;
            isLegal = true;
            if(Judge.findUser(userName)){
                JOptionPane.showMessageDialog(null, "用户名已存在请重新输入");
                this.dispose();
                new Register_GUI();
                isLegal = false;
            }
            if(isLegal){
                File dkmir =new File ("./"+userName+"");
                dkmir.mkdir();
                File file = new File("./"+userName+"/user.xml");
                File dkmir2 = new File("./"+userName+"/MyDatabase");
                dkmir2.mkdir();
                Document document = DocumentHelper.createDocument();
                Element rootElem = document.addElement(userName+"i");
                rootElem.addElement("user").addAttribute("name",userName).addAttribute("userKey",password);
                CreateTable.writeIO(file,document);
                JOptionPane.showMessageDialog(null, "新用户"+userName+"创建成功！");
                new Login_GUI();
            }
    }

}
