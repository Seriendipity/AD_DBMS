package GUI;

import Utils.SqlAnalysis;
import Utils.ConnectSqlParser;
import org.dom4j.DocumentException;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.io.IOException;
import java.util.List;


public class DBMS_GUI extends JFrame {
    private JTextArea sqlTextArea;

    public DBMS_GUI() {
        setTitle("AD_DBMS");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.white);
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
        JMenuItem find = new JMenuItem("查找");
        JMenuItem replace = new JMenuItem("替换");

        JMenuItem function = new JMenuItem("功能介绍");
        JMenuItem soft= new JMenuItem("软件介绍");

        //按钮添加监听
        find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findText();
            }
        });





        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

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
        editMenu.add(replace);

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
        JMenuItem find1 = new JMenuItem("查找");
        JMenuItem run1= new JMenuItem("运行");

        jPopupMenu.add(cut1);
        jPopupMenu.add(copy1);
        jPopupMenu.add(paste1);
        jPopupMenu.add(find1);
        jPopupMenu.add(run1);

        cut.addActionListener(e -> sqlTextArea.cut());
        copy.addActionListener(e -> sqlTextArea.copy());
        paste.addActionListener(e -> sqlTextArea.paste());
//        find.addActionListener(e -> sqlTextArea.findComponentAt());

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

    //查找功能
    private void findText() {
        String findText = JOptionPane.showInputDialog(null, "Find:");
        if (findText != null && !findText.isEmpty()) {
            String content = sqlTextArea.getText();
            int index = content.indexOf(findText, sqlTextArea.getCaretPosition());
            if (index != -1) {
                sqlTextArea.requestFocusInWindow();
                sqlTextArea.select(index, index + findText.length());
                sqlTextArea.setCaretPosition(index + findText.length());
            } else {
                JOptionPane.showMessageDialog(null, "Text not found");
            }
        }
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
