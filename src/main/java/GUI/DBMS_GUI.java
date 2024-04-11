package GUI;

import Utils.SqlAnalysis;
import Utils.ConnectSqlParser;
import org.dom4j.DocumentException;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class DBMS_GUI extends JFrame {
    private JTextArea sqlTextArea;
    private static int searchIndex = -1;
    public DBMS_GUI() {
        setTitle("AD_DBMS");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.white);

        /*----------------------------------搜索栏设置--------------------------*/
        JPanel findPanel=new JPanel(new GridLayout(2, 2, 5, 5));

        JTextField findField = new JTextField(20);
        JTextField replaceField = new JTextField(20);

        JButton findButton = new JButton("查询");
        JButton replaceButton = new JButton("替换");
        JButton prevButton = new JButton("上一个");
        JButton nextButton = new JButton("下一个");

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = findField.getText(); // 获取搜索文本
                String text = sqlTextArea.getText(); // 获取 JTextArea 中的文本内容
                searchIndex = text.indexOf(searchText); // 在文本内容中查找搜索文本的索引位置
                if (searchIndex != -1) { // 如果找到搜索文本
                    sqlTextArea.requestFocusInWindow(); // 将焦点设置回 JTextArea
                    sqlTextArea.setCaretPosition(searchIndex); // 设置光标位置为搜索文本的起始位置
                    sqlTextArea.select(searchIndex, searchIndex + searchText.length()); // 选择搜索文本
                } else {
                    JOptionPane.showMessageDialog(DBMS_GUI.this, "Text not found!"); // 如果未找到搜索文本，显示消息对话框
                }
        }});

        replaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchIndex != -1) { // 如果存在搜索到的文本
                    String replaceText = replaceField.getText(); // 获取替换文本
                    sqlTextArea.replaceSelection(replaceText); // 替换搜索到的文本为替换文本
                }
            }
        });

        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (searchIndex != -1) { // 如果存在搜索到的文本
                    String searchText = findField.getText(); // 获取搜索文本
                    String text = sqlTextArea.getText(); // 获取 JTextArea 中的文本内容
                    searchIndex = text.lastIndexOf(searchText, searchIndex - 1); // 查找搜索文本的上一个索引位置
                    if (searchIndex != -1) { // 如果找到上一个匹配项
                        sqlTextArea.requestFocusInWindow(); // 将焦点设置回 JTextArea
                        sqlTextArea.setCaretPosition(searchIndex); // 设置光标位置为搜索文本的起始位置
                        sqlTextArea.select(searchIndex, searchIndex + searchText.length()); // 选择搜索文本
                    } else {
                        JOptionPane.showMessageDialog(DBMS_GUI.this, "No previous occurrence found!"); // 如果未找到上一个匹配项，显示消息对话框
                    }
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (searchIndex != -1) { // 如果存在搜索到的文本
                    String searchText = findField.getText(); // 获取搜索文本
                    String text = sqlTextArea.getText(); // 获取 JTextArea 中的文本内容
                    searchIndex = text.indexOf(searchText, searchIndex + 1); // 查找搜索文本的下一个索引位置
                    if (searchIndex != -1) { // 如果找到下一个匹配项
                        sqlTextArea.requestFocusInWindow(); // 将焦点设置回 JTextArea
                        sqlTextArea.setCaretPosition(searchIndex); // 设置光标位置为搜索文本的起始位置
                        sqlTextArea.select(searchIndex, searchIndex + searchText.length()); // 选择搜索文本
                    } else {
                        JOptionPane.showMessageDialog(DBMS_GUI.this, "No next occurrence found!"); // 如果未找到下一个匹配项，显示消息对话框
                    }
                }
            }
        });



        findPanel.add(findField);
        findPanel.add(findButton);
        findPanel.add(replaceButton);
        findPanel.add(replaceField);
        findPanel.add(prevButton);
        findPanel.add(nextButton);

        findPanel.setVisible(false);
        /*-----------------------------创建菜单栏---------------------------*/
        GradientMenuBar menuBar = new GradientMenuBar();
        //菜单对象
        JMenu fileMenu = new JMenu("文件(F)");
        JMenu editMenu = new JMenu("编辑(E)");
        JMenu helpMenu = new JMenu("帮助(H)");

        //菜单内元素
        JMenuItem exitMenuItem = new JMenuItem("退出AD_DBMS");
        JMenuItem open = new JMenuItem("打开");
        JMenuItem save = new JMenuItem("保存");
        JMenu newMenu = new JMenu("新建");
        JMenuItem database=new JMenuItem("数据库");
        JMenuItem user=new JMenuItem("用户");

        JMenuItem cut = new JMenuItem("剪切");
        JMenuItem copy = new JMenuItem("复制");
        JMenuItem paste = new JMenuItem("粘贴");
        JCheckBox find = new JCheckBox("查找");
        JMenuItem function = new JMenuItem("功能介绍");
        JMenuItem soft= new JMenuItem("软件介绍");


        //菜单功能实现
        cut.addActionListener(e -> sqlTextArea.cut());
        copy.addActionListener(e -> sqlTextArea.copy());
        paste.addActionListener(e -> sqlTextArea.paste());

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(null, "You selected: " + fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建文件选择对话框
                JFileChooser fileChooser = new JFileChooser();
                // 显示文件选择对话框，并获取用户的操作结果
                int result = fileChooser.showSaveDialog(null);
                // 如果用户选择了保存文件的位置和文件名
                if (result == JFileChooser.APPROVE_OPTION) {
                    // 获取用户选择的文件
                    File file = fileChooser.getSelectedFile();
                    try {
                        // 创建一个用于向文件写入数据的 BufferedWriter
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                        // 将文本区域中的文本写入文件
                        writer.write(sqlTextArea.getText());
                        // 关闭 BufferedWriter
                        writer.close();
                        // 弹出消息框提示文件保存成功
                        JOptionPane.showMessageDialog(null, "File saved successfully.");
                    } catch (IOException ex) {
                        // 弹出消息框提示保存文件时出错
                        JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
                    }
                }

            }
        });

        database.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            openNewDatabaseWindow();
            }
        });

        user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNewUserWindow();
            }
        });

        find.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                findPanel.setVisible(find.isSelected());
            }
        });
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        function.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFunctionFrame();
            }
        });

        soft.addActionListener(e -> openSoftFrame());

        //添加到各自板块
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(newMenu);
        newMenu.add(database);
        newMenu.add(user);
        fileMenu.add(exitMenuItem);

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(find);

        helpMenu.add(function);
        helpMenu.add(soft);

        setJMenuBar(menuBar);
        /*----------------------------------菜单栏样式设置--------------------------*/
        menuBar.setBackground(Color.white);
        //设置菜单栏的高度
        menuBar.setPreferredSize(new Dimension(menuBar.getPreferredSize().width, 30));

        //字体统一
        Font menuFont = new Font("宋体", Font.BOLD, 15); // 创建字体对象
        fileMenu.setFont(menuFont);
        editMenu.setFont(menuFont);
        helpMenu.setFont(menuFont);

        /*----------------------------------创建数据库资源管理器与右侧区域面板--------------------------*/
        //数据库资源管理器
        JPanel dbExplorer = new JPanel();
        dbExplorer.setBackground( new Color(232, 245, 230, 173));
        JLabel dbLabel = new JLabel("数据库资源管理器");
        dbLabel.setFont(new Font("宋体",Font.BOLD,13));
        dbExplorer.add(dbLabel);
        /*--------------------------------右侧面板------------------------------*/
        JPanel managePanel = new JPanel();

        /*-------------------顶端滚动面板(显示文件栏)-------------------*/
        JPanel fileTopPane = new JPanel();
        JScrollPane fileScrollPane = new JScrollPane(fileTopPane);
        //水平垂直滚动条都不出现
        fileScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        fileScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        //文件按钮
        JButton file1 = new JButton();
        file1.setPreferredSize(new Dimension(300,35));
        fileTopPane.add(file1);
        JButton file2 = new JButton();
        file2.setPreferredSize(new Dimension(300,35));
        fileTopPane.add(file2);
        JButton file3 = new JButton();
        file3.setPreferredSize(new Dimension(300,35));
        fileTopPane.add(file3);

        // 获取水平滚动条
        JScrollBar horizontalScrollBar = fileScrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setPreferredSize(new Dimension(10,3));
        // 设置水平滚动条样式
        horizontalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                // 设置滚动条的背景颜色
                this.thumbColor = Color.gray;
                // 设置滚动条的前景色（滑块颜色）
                this.thumbDarkShadowColor = Color.pink;
                // 设置滚动条的边框颜色
                this.thumbHighlightColor = Color.lightGray;
                this.minimumThumbSize = new Dimension(1100,3);
                this.maximumThumbSize = new Dimension(100,3);
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton(); // 隐藏向左滚动的箭头按钮
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton(); // 隐藏向右滚动的箭头按钮
            }
            private JButton createZeroButton() {
                JButton button = new JButton();
                Dimension zeroDim = new Dimension(0, 0);
                button.setPreferredSize(zeroDim);
                button.setMinimumSize(zeroDim);
                button.setMaximumSize(zeroDim);
                return button;
            }
        });



        /*-------------------工具栏面板-------------------*/
        JPanel toolBar = new JPanel();
        JButton executeButton = new JButton("查询控制台");
        JButton actionButton = new JButton("运行");
        JButton connectButton = new JButton("连接已存在数据库");
        toolBar.add(executeButton);
        toolBar.add(actionButton);
        toolBar.add(connectButton);
        //点击运行后
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSQL();
            }
        });
        //点击连接已存在数据库后
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connect_GUI connect_gui = new Connect_GUI();
            }
        });

        /*----------------------------------右键菜单栏设置--------------------------*/
        JPopupMenu jPopupMenu=new JPopupMenu();

        JMenuItem cut1 = new JMenuItem("剪切");
        JMenuItem copy1 = new JMenuItem("复制");
        JMenuItem paste1 = new JMenuItem("粘贴");
        JMenuItem run1= new JMenuItem("运行");

        jPopupMenu.add(cut1);
        jPopupMenu.add(copy1);
        jPopupMenu.add(paste1);
        jPopupMenu.add(run1);

        cut1.addActionListener(e -> sqlTextArea.cut());
        copy1.addActionListener(e -> sqlTextArea.copy());
        paste1.addActionListener(e -> sqlTextArea.paste());
        run1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        /*-------------------文本区域的滚动面板-------------------*/
        sqlTextArea = new JTextArea();

        //给sqlTextArea加上
        sqlTextArea.setComponentPopupMenu(jPopupMenu);

        JScrollPane textScrollPane = new JScrollPane(sqlTextArea);
        //大小适配
        managePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                textScrollPane.setPreferredSize(new Dimension(managePanel.getWidth(), managePanel.getHeight()-60));
                fileScrollPane.setPreferredSize(new Dimension(managePanel.getWidth(), 40));
                toolBar.setPreferredSize(new Dimension(managePanel.getWidth(), 30));
            }
        });

        managePanel.add(fileScrollPane,BorderLayout.NORTH);
        managePanel.add(toolBar,BorderLayout.NORTH);
        managePanel.add(findPanel,BorderLayout.NORTH);
        managePanel.add(textScrollPane,BorderLayout.CENTER);



        //创建分隔面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,dbExplorer,managePanel);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.2);
        splitPane.setDividerSize(2);


        // 将组件添加到界面中
        Container container = getContentPane();
        container.add(splitPane, BorderLayout.CENTER);

    //构造函数结尾
    }

    //打开功能帮助界面
    private void openFunctionFrame() {
        JFrame functionHelp = new JFrame("Sub Window");
        functionHelp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        functionHelp.setLocationRelativeTo(null); // 居中显示

        // 创建文本区域，并设置初始文本
        JTextArea textArea = new JTextArea("功能介绍");
        textArea.setLineWrap(true); // 自动换行
        textArea.setWrapStyleWord(true); // 不截断单词
        textArea.setEditable(false); // 设置文本区域为只读

        // 创建滚动窗格，将文本区域放入滚动窗格中
        JScrollPane scrollPane = new JScrollPane(textArea);
        // 设置滚动窗格的尺寸
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // 将滚动窗格添加到子窗口中
        functionHelp.getContentPane().add(scrollPane, BorderLayout.CENTER);

        functionHelp.pack();
        functionHelp.setVisible(true);
    }

    //打开软件帮助界面
    private void openSoftFrame() {
        JFrame softHelp = new JFrame("软件介绍");
        softHelp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        softHelp.setLocationRelativeTo(null); // 居中显示

        // 创建文本区域，并设置初始文本
        JTextArea textArea = new JTextArea("这是AD_DBMS系统\n使用Java为底层代码编写的DBMS系统");
        textArea.setLineWrap(true); // 自动换行
        textArea.setWrapStyleWord(true); // 不截断单词
        textArea.setEditable(false); // 设置文本区域为只读

        // 创建滚动窗格，将文本区域放入滚动窗格中
        JScrollPane scrollPane = new JScrollPane(textArea);
        // 设置滚动窗格的尺寸
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // 将滚动窗格添加到子窗口中
        softHelp.getContentPane().add(scrollPane, BorderLayout.CENTER);

        softHelp.pack();
        softHelp.setVisible(true);
    }

    private void openNewUserWindow() {
        JFrame newUser = new JFrame("新建用户");
        newUser.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newUser.setLocationRelativeTo(null); // 居中显示

        JPanel subPanel = new JPanel(new GridLayout(3, 1));

        JLabel nameLabel = new JLabel("名称");
        JTextField nameTextField = new JTextField(20);

        JLabel passwordLabel = new JLabel("root密码");
        JPasswordField passwordField = new JPasswordField(20);

        subPanel.add(nameLabel);
        subPanel.add(nameTextField);
        subPanel.add(passwordLabel);
        subPanel.add(passwordField);

        JButton okButton = new JButton("确定");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText();
                String password = new String(passwordField.getPassword());
                //这里调用创建用户的方法




                newUser.dispose();
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newUser.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        newUser.getContentPane().add(subPanel, BorderLayout.CENTER);
        newUser.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        newUser.pack();
        newUser.setVisible(true);
    }

    //新建数据库的界面
    private void openNewDatabaseWindow() {
        // 创建子窗口
        JFrame newDatabase = new JFrame("新建数据库");
        // 设置子窗口关闭时不退出程序，而是只关闭子窗口
        newDatabase.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置子窗口居中显示
        newDatabase.setLocationRelativeTo(null);
        // 创建一个面板，使用网格布局管理器，包含一个标签和一个文本框
        JPanel subPanel = new JPanel(new GridLayout(2, 1));

        JLabel label = new JLabel("名称");
        JTextField textField = new JTextField(20);

        subPanel.add(label);
        subPanel.add(textField);

        // 创建确定和取消按钮
        JButton okButton = new JButton("确定");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在确定按钮的点击事件中处理逻辑，例如获取文本框中的内容并进行处理
                String name = textField.getText();
                // 这部分调用创建数据的函数







                // 关闭子窗口
                newDatabase.dispose();
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 取消按钮直接关闭子窗口
                newDatabase.dispose();
            }
        });

        // 创建一个面板，包含确定和取消按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // 将文本框面板和按钮面板添加到子窗口中
        newDatabase.getContentPane().add(subPanel, BorderLayout.CENTER);
        newDatabase.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // 调整子窗口大小以适应组件的首选大小
        newDatabase.pack();
        // 设置子窗口可见
        newDatabase.setVisible(true);
    }

    private void executeSQL() {
        String sql = sqlTextArea.getText();
        List<List<String>> result = SqlAnalysis.generateParser(sql);
        try {
            ConnectSqlParser.connectSql(result);
        } catch (IOException | DocumentException e) {
            JOptionPane.showMessageDialog(this,"解析出现错误" + e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
        }
        //sqlTextArea.setText(result.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DBMS_GUI().setVisible(true);
            }
        });
    }
    // 自定义渐变色菜单栏
    static class GradientMenuBar extends JMenuBar {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            int w = getWidth();
            int h = getHeight();

            Color color1 = new Color(224, 143, 171, 124);
            Color color2 = new Color(255,255,255, 210);
            Color color3 = new Color(138, 210, 150, 140);

            // 渐变色1
            GradientPaint gp1 = new GradientPaint(200, 0, color1, 600, 0, color2);
            g2d.setPaint(gp1);
            g2d.fillRect(0, 0, 600, h);

            // 渐变色2
            GradientPaint gp2 = new GradientPaint(600, 0, color2, w, 0, color3);
            g2d.setPaint(gp2);
            g2d.fillRect(600, 0, w-600, h);
        }
    }
}
