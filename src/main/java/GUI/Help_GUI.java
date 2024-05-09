package GUI;

import javax.swing.*;
import java.awt.*;

public class Help_GUI extends JFrame {
    public Help_GUI(){
        setTitle("使用说明");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(500,230); // 居中显示
        setSize(550, 350);
        // 创建文本区域，并设置初始文本
        JTextArea textArea = new JTextArea(
                " 下面是本系统的使用说明(大小写不敏感)：\n" +
                        " (1)Create语句\n" +
                        " --Create database yourDatabaseName;\n" +
                        " --Create User yourUserName;\n" +
                        " --Create index on yourTableName (ColumnName [DESC|ASC]);\n" +
                        " --Create index YourIndexName on yourTableName (ColumnName [DESC|ASC]);\n" +
                        " --Create table yourTableName (ColumnName ColumnType constriction,[...]);\n" +
                        " (2)Use语句\n" +
                        " --Use yourUserName;\n" +
                        " --Use yourDatabaseName;\n" +
                        " (3)Drop语句\n" +
                        " --Drop database yourDatabaseName; \n" +
                        " --Drop table yourTableName;\n" +
                        " (4)Show语句\n" +
                        " --Show databases MyDatabases;(Tips:databases)\n" +
                        " --Show tables yourDatabaseName;(Tips:tables)\n" +
                        " ---describe yourTableName;\n" +
                        " (5)Insert into语句\n" +
                        " --Insert into yourTableName (ColumnName,[...]) values (ColumnValue,[...]),[(...)];\n" +
                        " (6)Update语句\n" +
                        " --Update table yourTableName set Column=ChangeValue where Column=OriginalValue---(Tip:set和where的等号前后不要有空格)\n" +
                        " (7)Select语句\n" +
                        " --Select * from yourTableName;\n" +
                        " --Select * from yourTableName where yourCondition\n" +
                        " --Select columnName from yourTableName\n" +
                        " --Select columnName from yourTableName where yourCondition\n" +
                        " (8)Alter语句\n" +
                        " --Alter table yourTableName add column yourColumnName ColumnType\n" +
                        " --Alter table yourTableName drop column yourColumnName\n" +
                        " (9)查看日志文件\n" +
                        " --watch LogFile\n" +
                        " ----------------------------------------------------------------------------------------");
        textArea.setLineWrap(true); // 自动换行
        textArea.setWrapStyleWord(true); // 不截断单词
        textArea.setEditable(false); // 设置文本区域为只读

        // 创建滚动窗格，将文本区域放入滚动窗格中
        JScrollPane scrollPane = new JScrollPane(textArea);
        // 设置滚动窗格的尺寸
        scrollPane.setPreferredSize(new Dimension(550, 350));

        // 将滚动窗格添加到子窗口中
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }
}
