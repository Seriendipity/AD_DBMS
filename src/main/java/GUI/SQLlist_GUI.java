package GUI;

import SqlFunction.RecordOperationLog;
import SqlFunction.UseUser;
import Utils.ConnectSqlParser;
import Utils.SqlAnalysis;
import org.dom4j.DocumentException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class SQLlist_GUI extends JFrame {
    private JList<String> sqlList;//用于运行多条sql语句时
    private JButton runButton;
    public SQLlist_GUI(String[] sqlStatements) {
        setTitle("选择语句(Ctrl选择多条语句)");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建一个滚动面板来放置SQL语句列表
        JScrollPane scrollPane = new JScrollPane();
        sqlList = new JList<>(sqlStatements);
        scrollPane.setViewportView(sqlList);

        // 创建一个按钮用于运行选择的SQL语句
        runButton = new JButton("运行");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedIndices = sqlList.getSelectedIndices(); // 获取所选语句下标值
                if (selectedIndices != null && selectedIndices.length > 0) {
                    for (int index : selectedIndices) {
                        String selectedSQL = (String) sqlList.getModel().getElementAt(index).toLowerCase() + ";"; // 根据下标获取SQL语句
                        System.out.println("运行 SQL 语句: " + selectedSQL);
                        DBMS_GUI.actionSQL(selectedSQL);
                    }
                } else {
                    JOptionPane.showMessageDialog(SQLlist_GUI.this, "请选择要运行的SQL语句！", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // 将滚动面板和按钮添加到窗口中
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(runButton, BorderLayout.SOUTH);
        setVisible(true);
    }
}
