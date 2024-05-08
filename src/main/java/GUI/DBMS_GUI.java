package GUI;

import SqlFunction.UseDatabase;
import SqlFunction.UseUser;
import Utils.SqlAnalysis;
import Utils.ConnectSqlParser;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class DBMS_GUI extends JFrame {
    private static final JTextArea sqlTextArea = new JTextArea();
    private JScrollPane textScrollPane;
    private static JPanel managePanel = new JPanel();
    private static int searchIndex = -1;

    private static final JScrollPane dbExplorerScrollPane= new JScrollPane();

    private static JLabel dbLabel = new JLabel("数据库资源管理器");


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
        JMenu userMenu = new JMenu("用户(U)");

        //菜单内元素
        JMenuItem exitMenuItem = new JMenuItem("退出AD_DBMS");
        JMenuItem open = new JMenuItem("连接数据库");
        JMenuItem save = new JMenuItem("保存");
        JMenuItem newDataBaseMenu = new JMenuItem("新建数据库");

        JMenuItem cut = new JMenuItem("剪切");
        JMenuItem copy = new JMenuItem("复制");
        JMenuItem paste = new JMenuItem("粘贴");
        JCheckBox find = new JCheckBox("查找");

        JMenuItem soft= new JMenuItem("使用指南");

        JMenuItem login = new JMenuItem("登录");
        JMenuItem register= new JMenuItem("注册");
        //菜单功能实现
        cut.addActionListener(e -> sqlTextArea.cut());
        copy.addActionListener(e -> sqlTextArea.copy());
        paste.addActionListener(e -> sqlTextArea.paste());

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectDatabase();
            }
        });



        newDataBaseMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNewDatabaseWindow();
            }
        });
        //登录
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login_GUI();
            }
        });
        //注册
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Register_GUI();
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        soft.addActionListener(e -> new Help_GUI());

        //添加到各自板块
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        menuBar.add(userMenu);

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(newDataBaseMenu);
        fileMenu.add(exitMenuItem);

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(find);

        helpMenu.add(soft);

        userMenu.add(login);
        userMenu.add(register);

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
        userMenu.setFont(menuFont);

        /*----------------------------------创建数据库资源管理器与右侧区域面板--------------------------*/
        //数据库资源管理器
        JPanel dbExplorer= new JPanel();
        dbExplorer.setBackground( new Color(232, 245, 230, 173));
//        JLabel dbLabel = new JLabel("数据库资源管理器");
        dbLabel.setFont(new Font("宋体",Font.BOLD,13));
        dbExplorer.add(dbLabel,BorderLayout.NORTH);
        dbExplorerScrollPane.setViewportView(dbExplorer);

        /*--------------------------------右侧面板------------------------------*/

        managePanel.setBackground(new Color(255, 255, 255, 107));

        /*-------------------顶端滚动面板(显示文件栏)-------------------*/

        /*-------------------工具栏面板-------------------*/
        JPanel toolBar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(Color.gray); // 设置边框颜色为灰色
                g2d.fillRect(0, 1, getWidth(), 1); // 绘制上边框
                g2d.fillRect(0, getHeight() - 3, getWidth(), 1); // 绘制下边框
                g2d.dispose();
            }
        };
        toolBar.setBackground(Color.white);


        JButton executeButton = new JButton("查询控制台");
        JButton actionButton = new JButton("运行");
        JButton connectButton = new JButton("连接已存在数据库");
        toolBar.add(executeButton);
        toolBar.add(actionButton);
        toolBar.add(connectButton);
        //点击查询控制台之后
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在点击查询控制台按钮后显示文本区域
                managePanel.add(textScrollPane, BorderLayout.CENTER);
                // 重新布局以确保文本区域显示
                managePanel.revalidate();
                managePanel.repaint();
            }
        });
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
                connectDatabase();
            }
        });

        /*-------------------文本区域的滚动面板-------------------*/
//        sqlTextArea = new JTextArea();
        textScrollPane = new JScrollPane(sqlTextArea);
        //大小适配
        managePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                textScrollPane.setPreferredSize(new Dimension(managePanel.getWidth(), managePanel.getHeight()-60));
                toolBar.setPreferredSize(new Dimension(managePanel.getWidth(), 40));
            }
        });

        managePanel.add(toolBar,BorderLayout.NORTH);


        //创建分隔面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,dbExplorerScrollPane,managePanel);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.2);
        splitPane.setDividerSize(2);

        // 将组件添加到界面中
        Container container = getContentPane();
        container.add(splitPane, BorderLayout.CENTER);

        /*----------------------------右键菜单--------------------------*/
        // 创建一个 JPopupMenu 作为右键菜单
        JPopupMenu popupMenu = new JPopupMenu();

        // 创建菜单项
        JMenuItem actionItem = new JMenuItem("运行");
        JMenuItem pasteItem = new JMenuItem("粘贴");
        JMenuItem cutItem = new JMenuItem("剪切");

        actionItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeSQL();
            }
        });


        // 将菜单项添加到 JPopupMenu
        popupMenu.add(actionItem);
        popupMenu.add(pasteItem);
        popupMenu.add(cutItem);

        // 为文本域添加鼠标监听器
        sqlTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            // 判断鼠标事件并显示弹出菜单
            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

    //构造函数结尾
    }

    /*--------------------连接数据库-------------------*/
    public static void connectDatabase(){
        if(UseUser.userName != ""){
            String dbName = JOptionPane.showInputDialog(null,"请输入数据库的名称","连接数据库",JOptionPane.PLAIN_MESSAGE);
            showTree(dbName);
        }else{
            JOptionPane.showMessageDialog(null, "请先登录！\n 菜单->用户->登录");
        }

    }

    /*--------------------显示树结构方法-------------------*/
    public static void showTree(String dbName) {
        if (dbName!= null){
            File folder = new File("./"+UseUser.userName+"./MyDatabase/"+dbName+"");
            if (folder.exists() && folder.isDirectory()) {
                // 创建根节点
                DefaultMutableTreeNode root = new DefaultMutableTreeNode(dbName);
                // 获取数据库文件夹中的所有文件
                File[] files = folder.listFiles();
                // 遍历所有文件
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            // 获取文件名作为表名
                            String tableName = file.getName();
                            // 创建表节点，并将其添加到根节点中
                            DefaultMutableTreeNode tableNode = new DefaultMutableTreeNode(tableName);
                            root.add(tableNode);
                        }
                    }
                }
                //创建树
                JTree tree = new JTree(root);
                tree.setBackground(new Color(232, 245, 230, 173));
                tree.setRootVisible(true); // 设置根节点可见

                //对节点进行监听
                tree.addTreeSelectionListener(new TreeSelectionListener() {
                    @Override
                    public void valueChanged(TreeSelectionEvent e) {
                        //获取当前节点
                        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                        if (selectedNode != null && selectedNode.isLeaf()) {
                            // 获取选中的表名
                            String tableName = selectedNode.toString();
                            // 获取选中表的父目录（即数据库名称）
                            String dbName = selectedNode.getParent().toString();
                            // 加载表结构到文本区域
                            loadTable(dbName, tableName);
                        }
                    }
                });

                //界面更新
                JPanel dbPanel = new JPanel(new BorderLayout());
                dbPanel.setBackground(new Color(232, 245, 230, 173));

                JLabel dbLabel = new JLabel("数据库资源管理器");
                dbLabel.setFont(new Font("宋体", Font.BOLD, 13));
                dbLabel.setHorizontalAlignment(SwingConstants.CENTER);

                dbPanel.add(dbLabel, BorderLayout.NORTH);
                dbPanel.add(tree, BorderLayout.CENTER);

                dbExplorerScrollPane.setViewportView(dbPanel);
            }else if (!folder.exists()){
                JOptionPane.showMessageDialog(null,"未找到该数据库","错误",JOptionPane.ERROR_MESSAGE);
            }
        }
        //切换到use dbname;
        String useDataBase = "use" + dbName + ";";
        System.out.println("sssssss");
        List<List<String>> result = SqlAnalysis.generateParser(useDataBase);
        try {
            ConnectSqlParser.connectSql(result);
        } catch (IOException | DocumentException e) {
        }


    }
    /*--------------------点击后显示表结构方法-------------------*/
    private static void loadTable(String dbName,String tableName) {
        // 加载配置文件
        File configFile = new File("./"+UseUser.userName+"./MyDatabase/" + dbName + "/" + tableName + "/" + tableName+"-config.xml");
        // 加载数据文件
        File dataFile = new File("./"+UseUser.userName+"./MyDatabase/" + dbName + "/" + tableName + "/" + tableName+"0.xml");
        if (configFile.exists() && dataFile.exists()) {
            try {
                /*------------解析配置文件-------------*/
                SAXReader reader = new SAXReader();
                Document configDoc = reader.read(configFile);
                // 获取xml文件根元素
                Element conRootElement = configDoc.getRootElement();

                // 获取根元素的属性列表(列名)
                List<Attribute> rootAttributes = conRootElement.attributes();

                // 创建表格模型
                DefaultTableModel model = new DefaultTableModel();
                // 将属性名作为列名添加到表格模型中
                for (Attribute attribute : rootAttributes) {
                    model.addColumn(attribute.getName());
                }

                /*--------------解析数据文件-------------*/
                Document dataDoc = reader.read(dataFile);
                // 获取数据文件根元素
                Element dataRootElement = dataDoc.getRootElement();

                // 获取数据行
                List<Element> rows = dataRootElement.elements();

                for (Element row : rows) {
                    // 获取数据行的子元素
                    List<Attribute> dataAttributes = row.attributes();
                    // 创建一个存储属性值的数组
                    Object[] rowData = new Object[dataAttributes.size()];

                    // 将每个属性的值添加到数组中
                    for (int i = 0; i < dataAttributes.size(); i++) {
                        Attribute attribute = dataAttributes.get(i);
                        rowData[i] = attribute.getValue();
                    }

                    // 将数据行添加到表格模型中
                    model.addRow(rowData);
                }

                // 创建表格并设置模型
                JTable table = new JTable(model);
                // 创建滚动面板，将表格放入其中
                JScrollPane tableScrollPane = new JScrollPane(table);
                managePanel.add(tableScrollPane,BorderLayout.CENTER);
                // 重新布局
                managePanel.revalidate();
                managePanel.repaint();

            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "未找到数据文件", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    //点击运行后的解析
    private void executeSQL() {
        String sql = sqlTextArea.getText().replaceAll("\\n|\\r", "");
        String[] sqlArray = sql.split(";");
        new SQLlist_GUI(sqlArray);
    }

    //运行sql语句
    public static void actionSQL(String sql) {
        List<List<String>> result = SqlAnalysis.generateParser(sql);
        try {
            ConnectSqlParser.connectSql(result);
        } catch (IOException | DocumentException e) {
            JOptionPane.showMessageDialog(null,"解析出现错误" + e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void executeSQL2() {
        String sql = sqlTextArea.getText().replaceAll("\\n|\\r", "");
        String[] sqlArray = sql.split(";");
        List<List<String>> result = SqlAnalysis.generateParser(sql);
        try {
            ConnectSqlParser.connectSql(result);
        } catch (IOException | DocumentException e) {
            JOptionPane.showMessageDialog(null,"解析出现错误" + e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
        }
    }
    //新建数据库的界面
    private void openNewDatabaseWindow() {
        // 创建子窗口
        JFrame newDatabase = new JFrame("新建数据库");
        // 设置子窗口关闭时不退出程序，而是只关闭子窗口
        newDatabase.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 设置子窗口居中显示
//        newDatabase.setLocationRelativeTo(null);
        newDatabase.setLocation(this.getX() + (this.getWidth()- 550) / 2, this.getY()+ (this.getHeight()- 350) / 2);
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

    // 公共方法用于设置 dbLabel 的文字
    public static void setDBLabel(String labelText) {
        dbLabel.setText(labelText);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final DBMS_GUI first_frame = new DBMS_GUI();
                first_frame.setVisible(true);
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
