package GUI;

import SqlFunction.UseUser;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Logfile_GUI extends JFrame {

    public Logfile_GUI(){
        setTitle(UseUser.userName+"的日志");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(500,230); // 居中显示
        setSize(550, 350);
        // 创建文本区域，并设置初始文本
        JTextArea textArea = new JTextArea();
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
        //读入日志文件
        String filePath = "./"+ UseUser.userName+"/MyDatabase/LogFile.txt";
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 将文件的每一行追加到textArea中
                textArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            textArea.append("读取日志文件失败\n");
        }

    }

}
