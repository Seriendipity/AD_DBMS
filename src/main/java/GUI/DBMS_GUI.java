package GUI;

import SqlFunction.*;
import Utils.SqlAnalysis;
import Utils.ConnectSqlParser;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;


public class DBMS_GUI extends JFrame {
    private static final JTextArea sqlTextArea = new JTextArea();
    private static final JTextArea consoleTextArea = new JTextArea();
    private JScrollPane textScrollPane;
    private JScrollPane consoleScrollPane;

    private static JPanel managePanel = new JPanel();

    private static final JScrollPane dbExplorerScrollPane= new JScrollPane();

    private static JLabel dbLabel = new JLabel("数据库资源管理器");

    private JSplitPane splitPane2;
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
        JMenuItem logfile = new JMenuItem("查看日志");
        JMenuItem newDataBaseMenu = new JMenuItem("新建数据库");

        JMenuItem cut = new JMenuItem("剪切");
        JMenuItem copy = new JMenuItem("复制");
        JMenuItem paste = new JMenuItem("粘贴");
        JCheckBox find = new JCheckBox("查找");

        JMenuItem help= new JMenuItem("使用指南");

        JMenuItem login = new JMenuItem("登录");
        JMenuItem register= new JMenuItem("注册");
        JMenuItem exitLogin= new JMenuItem("退出登录");
        //菜单功能实现
        cut.addActionListener(e -> sqlTextArea.cut());
        copy.addActionListener(e -> sqlTextArea.copy());
        paste.addActionListener(e -> sqlTextArea.paste());
        //查看日志
        logfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(UseUser.userName != ""){
                    new Logfile_GUI();
                }else{
                    JOptionPane.showMessageDialog(null, "请先登录！\n 菜单->用户->登录");
                }
            }
        });

        //连接数据库
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectDatabase();
            }
        });
        //新建数据库
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
        //退出登录
        exitLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UseUser.userName = "";
                setDBLabel("数据库资源管理器");
                System.out.println("成功退出登录");
            }
        });
        //退出程序
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //帮助
        help.addActionListener(e -> new Help_GUI());

        //添加到各自板块
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        menuBar.add(userMenu);

        fileMenu.add(newDataBaseMenu);
        fileMenu.add(open);
        fileMenu.add(logfile);
        fileMenu.add(exitMenuItem);

        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(find);

        helpMenu.add(help);

        userMenu.add(login);
        userMenu.add(register);
        userMenu.add(exitLogin);

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
        dbLabel.setFont(new Font("宋体",Font.BOLD,15));
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
        JButton refreshButton = new JButton("刷新");
        toolBar.add(executeButton);
        toolBar.add(actionButton);
        toolBar.add(connectButton);
        toolBar.add(refreshButton);
        //点击查询控制台之后
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //创建分隔面板(上下)
                splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,textScrollPane,consoleScrollPane);
                splitPane2.setDividerLocation(380);
                splitPane2.setResizeWeight(0.7);
                splitPane2.setDividerSize(3);

                managePanel.remove(consoleScrollPane);
                managePanel.add(splitPane2, BorderLayout.CENTER);

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
        //点击刷新
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
                if(UseDatabase.databaseName!=""){//已经连接数据库的话就要重新刷新显示树结构
                    showTree(UseDatabase.databaseName);
                }
            }
        });

        /*-------------------文本区域的滚动面板-------------------*/

        sqlTextArea.setFont(new Font("微软雅黑",Font.PLAIN,18));
        textScrollPane = new JScrollPane(sqlTextArea);//输入sql的
        consoleScrollPane = new JScrollPane(consoleTextArea);//返回结果的


        //大小适配
        managePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                consoleScrollPane.setPreferredSize(new Dimension(managePanel.getWidth(), managePanel.getHeight()-toolBar.getHeight()));
                if(splitPane2 == null){
                    return;
                }
                splitPane2.setPreferredSize(new Dimension(managePanel.getWidth(), managePanel.getHeight()-toolBar.getHeight()));
                toolBar.setPreferredSize(new Dimension(managePanel.getWidth(), 40));
            }
        });

        managePanel.add(toolBar,BorderLayout.NORTH);
        managePanel.add(consoleScrollPane,BorderLayout.SOUTH);

        //控制台
        // 创建一个文本区域用于控制台输出

        consoleTextArea.setEditable(false); // 设置为不可编辑，只用于显示输出
        consoleTextArea.setLineWrap(true); // 自动换行
        consoleTextArea.setFont(new Font("宋体", Font.BOLD, 16)); // 设置字体和大小

        //创建分隔面板(左右)
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

        /*------------------更改输出流-----------------------*/
        // 创建一个ByteArrayOutputStream来捕获输出
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 创建一个新的PrintStream，将输出写入到ByteArrayOutputStream
        final PrintStream printStream = new PrintStream(baos);

        // 保存原始的System.out
        final PrintStream originalOut = System.out;

        // 重定向System.out
        System.setOut(printStream);

        // 使用一个线程来定期刷新并显示输出
        new Thread(() -> {
            while (true) {
                try {
                    // 等待一段时间（例如100毫秒）
                    Thread.sleep(100);

                    // 刷新并获取新的输出
                    printStream.flush();
                    byte[] bytes = baos.toByteArray();

                    // 重置ByteArrayOutputStream，以便我们可以继续捕获新的输出
                    baos.reset();

                    // 将字节解码为字符串并添加到JTextArea（在EDT中执行）
                    String text = new String(bytes, "UTF-8");
                    SwingUtilities.invokeLater(() -> {
                        consoleTextArea.append(text);
                    });
                } catch (InterruptedException e) {
                    // 处理线程中断
                    Thread.currentThread().interrupt();
                    break;
                } catch (IOException e) {
                    // 处理IOException（这里不太可能出现，但为了完整性）
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("程序输出：");

        //构造函数结尾
    }

    /*--------------------连接数据库-------------------*/
    public static void connectDatabase(){
        if(UseUser.userName != ""){
            String dbName = JOptionPane.showInputDialog(null,"请输入数据库的名称","连接数据库",JOptionPane.PLAIN_MESSAGE);
            if(dbName!=null){
                showTree(dbName);
            }

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

                JLabel dbLabel = new JLabel(UseUser.userName+"的数据库资源管理器");
                dbLabel.setFont(new Font("宋体", Font.BOLD, 15));
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
        List<List<String>> result = SqlAnalysis.generateParser(useDataBase);
        try {
            ConnectSqlParser.connectSql(result);
        } catch (IOException | DocumentException e) {
        }

    }
    /*--------------------点击后显示表结构方法-------------------*/

    //显示表结构方法
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
                    Object[] rowData = new Object[rootAttributes.size()];

                    // 将每个属性的值添加到数组中
                    for (int i = 0; i < rootAttributes.size(); i++) {
                        Attribute attribute = rootAttributes.get(i);
                        // 检查当前属性是否存在于数据行中
                        boolean attributeExists = false;
                        for (Attribute dataAttribute : dataAttributes) {
                            if (dataAttribute.getName().equals(attribute.getName())) {
                                attributeExists = true;
                                rowData[i] = row.attributeValue(attribute.getName());
                                break;
                            }
                        }
                        // 如果当前属性不存在于数据行中，则设置为null
                        if (!attributeExists) {
                            rowData[i] = "null";
                        }
                    }
                    // 将数据行添加到表格模型中
                    model.addRow(rowData);
                }


                // 创建表格并设置模型
                JTable table = new JTable(model);
                // 创建滚动面板，将表格放入其中
                JScrollPane tableScrollPane = new JScrollPane(table);

                //新建一个对话框存放该表格
                JDialog tableDialog = new JDialog();
                tableDialog.setTitle(tableName);
                tableDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                tableDialog.setLayout(new BorderLayout());
                tableDialog.add(tableScrollPane,BorderLayout.NORTH);
                //关闭按钮
                JButton closeButton = new JButton("关闭");
                closeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tableDialog.dispose();
                    }
                });
                // 提交修改按钮
                JButton submitButton = new JButton("提交修改");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        // 存储所有要更新的值
                        List<List<String>> allUpdateValues = new ArrayList<>();
                        for (int row = 0; row < table.getRowCount(); row++) {
                            List<String> updateValues = new ArrayList<>(); // 存储当前行要更新的值
                            for (int column = 0; column < table.getColumnCount(); column++) {
                                Object cellValue = table.getValueAt(row, column);
                                String columnName = table.getColumnName(column);
                                if(cellValue!=null){
                                    updateValues.add(columnName + " = " + cellValue.toString());
//                                    System.out.println("第"+row+"行"+"第"+column+"列"+"属性名是"+columnName+"::"+cellValue);
                                }else{
                                    String s = "null";
                                    updateValues.add(columnName + " = " + s);
                                }
                            }
                            allUpdateValues.add(updateValues);
                        }

                        // 更新 XML 文件
                        try {
                            // 遍历表格中的更新值，并将其应用到 XML 文件中
                            for (int i = 0; i < allUpdateValues.size(); i++) {
                                List<String> updateValues = allUpdateValues.get(i);
                                List<Element> everyrows = dataRootElement.elements();
                                Element xmlRow = everyrows.get(i);
                                // 创建一个 HashMap 用于存储属性名与更新值的对应关系
                                LinkedHashMap<String, String> updateMap = new LinkedHashMap<>();
                                // 将更新值按照属性名与属性值的形式存储到 HashMap 中
                                for (String update : updateValues) {
                                    String[] parts = update.split(" = ");
                                    if (parts.length == 2) {
                                        updateMap.put(parts[0], parts[1]);
                                    } else {
//                                        System.out.println("更新字符串格式错误: " + parts[0]);
                                        updateMap.put(parts[0], "null");
                                    }
                                }
//                                // 遍历 HashMap 中的键值对并输出
//                                for (Map.Entry<String, String> entry : updateMap.entrySet()) {
//                                    String key = entry.getKey();       // 获取键
//                                    String value = entry.getValue();   // 获取值
//                                    System.out.println("第"+i+"行："+"属性名：" + key + ", 更新值：" + value);
//                                }
                                // 更新 XML 节点的属性对应的值
                                for (Map.Entry<String, String> entry : updateMap.entrySet()) {
                                    String attributeName = entry.getKey();
                                    String attributeValue = entry.getValue();

                                    if (attributeName != null && attributeValue != null) {
                                        Attribute attribute = xmlRow.attribute(attributeName);
                                        if (attribute != null) {
                                            attribute.setValue(attributeValue);
                                        } else {
                                            // 找不到指定名称的属性则直接新加入这个属性
                                            xmlRow.addAttribute(attributeName,attributeValue);
                                        }
                                    } else {
                                        System.out.println("属性名称或值为空");
                                    }
                                }
                            }

                            // 保存更新后的 XML 文件
                            CreateTable.writeIO(dataFile,dataDoc);

                            // 提示用户更新成功
                            JOptionPane.showMessageDialog(null, "更新成功！");
                            tableDialog.repaint();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // 提示用户更新失败
                            JOptionPane.showMessageDialog(null, "更新失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(closeButton);
                buttonPanel.add(submitButton);
                tableDialog.add(buttonPanel,BorderLayout.SOUTH);
                tableDialog.setSize(600,500);
                tableDialog.setLocationRelativeTo(null);
                tableDialog.setVisible(true);

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
            RecordOperationLog.recordLog(sql);
        } catch (IOException | DocumentException e) {
            System.out.println("sssssssssssssssssssssssssssssss");
            JOptionPane.showMessageDialog(null,"解析出现错误" + e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
        }catch (Exception e){
            RecordOperationLog.recordLog(sql + " WRONG");
        }
    }

    //新建数据库的界面
    private void openNewDatabaseWindow() {
        if(UseUser.userName != ""){
            new NewDatabase_GUI();
        }else{
            JOptionPane.showMessageDialog(null, "请先登录！\n 菜单->用户->登录");
        }
    }

    // 公共方法用于设置 dbLabel 的文字
    public static void setDBLabel(String labelText) {
        dbLabel.setText(labelText);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DBMS_GUI first_frame = new DBMS_GUI();
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
