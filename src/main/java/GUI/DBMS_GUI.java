package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class DBMS_GUI extends JFrame {
    private JTextArea outputTextArea;

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
        JMenu viewMenu = new JMenu("视图(V)");
        //菜单内元素
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //添加到各自板块
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        fileMenu.add(exitMenuItem);
        setJMenuBar(menuBar);
        /*----------------------------------菜单栏样式设置--------------------------*/
        menuBar.setBackground(Color.white);
        //设置菜单栏的高度
        menuBar.setPreferredSize(new Dimension(menuBar.getPreferredSize().width, 30));

        //字体统一
        Font menuFont = new Font("宋体", Font.BOLD, 15); // 创建字体对象
        fileMenu.setFont(menuFont);
        editMenu.setFont(menuFont);
        viewMenu.setFont(menuFont);
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
        JButton file4 = new JButton();
        file4.setPreferredSize(new Dimension(300,35));
        fileTopPane.add(file4);
        JButton file5 = new JButton();
        file5.setPreferredSize(new Dimension(300,35));
        fileTopPane.add(file5);
        // 获取水平滚动条
        JScrollBar horizontalScrollBar = fileScrollPane.getHorizontalScrollBar();
        // 设置水平滚动条样式
        horizontalScrollBar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                // 设置滚动条的背景颜色
                this.thumbColor = Color.gray;
                // 设置滚动条的前景色（滑块颜色）
                this.thumbDarkShadowColor = Color.darkGray;
                // 设置滚动条的边框颜色
                this.thumbHighlightColor = Color.lightGray;
                this.minimumThumbSize = new Dimension(50,3);
                this.maximumThumbSize = new Dimension(50,3);
            }
        });



        /*-------------------工具栏面板-------------------*/
        JPanel toolBar = new JPanel();
        JButton executeButton = new JButton("查询控制台");
        toolBar.add(executeButton);

        /*-------------------文本区域的滚动面板-------------------*/
        JTextArea textArea = new JTextArea();
        JScrollPane textScrollPane = new JScrollPane(textArea);
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


    }

    private void executeSQL() {
        // 此处编写SQL执行逻辑
        // 示例：outputTextArea.setText("SQL executed successfully!");
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
