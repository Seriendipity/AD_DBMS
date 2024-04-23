package GUI;

import SqlFunction.UseDatabase;
import Utils.SqlAnalysis;
import Utils.ConnectSqlParser;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class DBMS_GUI extends JFrame {
    private static final JTextArea sqlTextArea = new JTextArea();
    private JScrollPane textScrollPane;
    private static JPanel managePanel = new JPanel();

    private static final JScrollPane dbExplorerScrollPane= new JScrollPane();

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
        JPanel dbExplorer= new JPanel();
        dbExplorer.setBackground( new Color(232, 245, 230, 173));
        JLabel dbLabel = new JLabel("数据库资源管理器");
        dbLabel.setFont(new Font("宋体",Font.BOLD,13));
        dbExplorer.add(dbLabel,BorderLayout.NORTH);
        dbExplorerScrollPane.setViewportView(dbExplorer);

        /*--------------------------------右侧面板------------------------------*/

        managePanel.setBackground(new Color(255, 255, 255, 107));

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
                //FIXME: 下述数据库名仅仅为了答辩展示，以后需要在解析
                UseDatabase.databaseName = "demo1";
                showTree();
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
                fileScrollPane.setPreferredSize(new Dimension(managePanel.getWidth(), 40));
                toolBar.setPreferredSize(new Dimension(managePanel.getWidth(), 40));
            }
        });

        managePanel.add(fileScrollPane,BorderLayout.NORTH);
        managePanel.add(toolBar,BorderLayout.NORTH);


        //创建分隔面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,dbExplorerScrollPane,managePanel);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.2);
        splitPane.setDividerSize(2);

        // 将组件添加到界面中
        Container container = getContentPane();
        container.add(splitPane, BorderLayout.CENTER);

    //构造函数结尾
    }
    /*--------------------显示树结构方法-------------------*/
    public static void showTree() {

        String dbName = JOptionPane.showInputDialog(null,"请输入数据库的名称","连接数据库",JOptionPane.PLAIN_MESSAGE);
        if (dbName!= null){
            File folder = new File("./MyDatabase/"+dbName+"");
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
        File configFile = new File("./MyDatabase/" + dbName + "/" + tableName + "/" + tableName+"-config.xml");
        // 加载数据文件
        File dataFile = new File("./MyDatabase/" + dbName + "/" + tableName + "/" + tableName+"0.xml");
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

    private void executeSQL() {
        String sql = sqlTextArea.getText().replaceAll("\\n|\\r", "");
        List<List<String>> result = SqlAnalysis.generateParser(sql);
        try {
            ConnectSqlParser.connectSql(result);
        } catch (IOException | DocumentException e) {
            JOptionPane.showMessageDialog(this,"解析出现错误" + e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
        }
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
