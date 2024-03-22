package SqlFunction;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.awt.desktop.OpenURIEvent;
import java.io.*;
public class CreateTable {
    //只能创建表格，还需要加入列的信息
    public static void createTable(String DatabaseName,String TableName)throws IOException{
        //在databaseName下创建表格
        //先判断有没有数据库
        File dir = new File("./MyDatabase/"+DatabaseName+"");
        if(dir.exists()){
            File file = new File("./MyDatabase/"+DatabaseName+"/"+TableName+"");
            file.createNewFile();
            System.out.println(TableName+"创建成功！");
        }else{
            System.out.println("不存在该数据库！");
        }
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
