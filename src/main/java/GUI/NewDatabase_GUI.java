package GUI;

import SqlFunction.CreateDatabase;
import org.dom4j.DocumentException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class NewDatabase_GUI extends JFrame {
    public NewDatabase_GUI(){
        setTitle("新建数据库");
        setSize(400, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel subPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件之间的间距

        // 添加名称标签和文本框
        JLabel nameLabel = new JLabel("新建数据库名称");
        subPanel.add(nameLabel, gbc);

        gbc.gridy++;
        JTextField nameTextField = new JTextField(40);
        subPanel.add(nameTextField, gbc);


        JButton okButton = new JButton("确认创建");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dataBaseName = nameTextField.getText();
                CreateDatabase.createDatabase(dataBaseName);
                JOptionPane.showMessageDialog(null, "数据库"+dataBaseName+"创建成功！");
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        getContentPane().add(subPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

}
