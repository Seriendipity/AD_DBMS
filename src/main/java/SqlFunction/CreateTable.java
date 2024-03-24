package SqlFunction;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

public class CreateTable {
    //只能创建表格，还需要加入列的信息
    static public int entry_num = 10;
    public static void createTable(String DatabaseName, String TableName, List<String> body)throws IOException{
        //在databaseName下创建表格
        //先判断有没有数据库
        File dir = new File("./MyDatabase/"+DatabaseName+"");
        if(dir.exists()){
            File file = new File("./MyDatabase/"+DatabaseName+"/"+TableName+"");
            if(!file.exists()){
                file.mkdir();
            }else{
                System.out.println(TableName+"表格已经存在");
            }
        }else{
            System.out.println("不存在该数据库！");
            return;
        }
        File table = new File("./MyDatabase/"+DatabaseName+"/"+TableName+"/"+TableName+"-config.xml");
        Document document = DocumentHelper.createDocument();
        Element rootElem = document.addElement(TableName+"s");

        //第一个列名，第二个数据类型 根节点的属性名称=数据类型
        for(int i = 0; i < body.size(); i++){
            String[] list = body.get(i).split(" ");
            rootElem.addAttribute(list[0],list[1]);

        }
        //第一张子表
        rootElem.addElement(TableName).setText("0");
        //存储物理层可插入子表的下标
        rootElem.addElement("insertTables");
        //记录是否建有索引
        rootElem.addElement("index");
        //记录主键的名称
        rootElem.addElement("indexName");
        //写入操作
        writeIO(table,document);

        //创建表的物理层第一张子表
        File firstFile=new File("./mydatabase/"+DatabaseName+"/"+TableName+"/"+TableName+"0.xml");
        Document firstDocument=DocumentHelper.createDocument();
        firstDocument.addElement(TableName+"s");
        //写入操作
        writeIO(firstFile,firstDocument);

        System.out.println(TableName+"表创建成功");
    }

    public static void writeIO(File file, Document document) throws IOException {
        //文件输出的位置
        FileOutputStream outputStream = new FileOutputStream(file);
        //文件的排版模式清晰易读
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        //创建写入对象，写入document对象
        XMLWriter xmlWriter = new XMLWriter(outputStream,outputFormat);
        xmlWriter.write(document);
        //关闭XMLWriter流
        xmlWriter.close();
    }
}
