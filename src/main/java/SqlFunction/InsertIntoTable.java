package SqlFunction;

import com.ibm.icu.util.IslamicCalendar;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xerces.xni.XMLLocator;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.List;

public class InsertIntoTable {
    //insert into 表名（列名称1，列名称2）values(列值1，列值2);
    public static void insertIntoTable(String DatabaseName, String TableName, List<String> tmp1, List<String> tmp2) throws DocumentException, IOException {
        //数据库是否合法
        if(!Judge.isDatabase()){
            return;
        }
        //表格是否存在
        File file = new File("./MyDatabase/" + DatabaseName + "/" + TableName + "");
        if(!file.exists()){
            System.out.println(TableName + "表不存在");
            return;
        }
        //打开配置文件，获取文件名作为写入文件
        File configFile = new File("./MyDatabase/"+DatabaseName+"/"+TableName+"/"+TableName+"-config.xml");
        SAXReader configFileReader = new SAXReader();
        Document configFileDocument =  configFileReader.read(configFile);
        Element writeFileElement = (Element) configFileDocument.getRootElement().selectSingleNode(TableName);

        //若有可插入文件，则插入数据到该文件
        String writeFileName;
        if(writeFileElement.selectNodes("insertTables").size() > 0){
            writeFileName = TableName + writeFileElement.selectSingleNode("insertTables").getText();
            //创建写入对象，获取记录数量
            File writeFile = new File("./MyDatabase/"+DatabaseName+"/"+TableName+writeFileName+".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(writeFile);
            Element rootElement = document.getRootElement();
            Element element = rootElement.addElement(TableName);
            for(int i = 1; i < tmp1.size();i++){
                element.addAttribute(tmp1.get(i),tmp2.get(i));
            }
            //若可插入文件记录数>=10,更新配置信息
            List<Node> nodes = document.getRootElement().selectNodes(TableName);
            if(nodes.size() >= 10){
                writeFileElement.remove(writeFileElement.selectSingleNode("insertTables"));
                OutputFormat outputFormat2 = OutputFormat.createPrettyPrint();
                outputFormat2.setEncoding("UTF-8");
                XMLWriter xmlWriter2 = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./MyDatabase/"+DatabaseName+"/"+TableName+"/"+TableName+"-config.xml")),outputFormat2);
                xmlWriter2.write(configFileDocument);
                xmlWriter2.close();
            }

            OutputFormat outputFormat1 = OutputFormat.createPrettyPrint();
            outputFormat1.setEncoding("UTF-8");
            XMLWriter xmlWriter1 = new XMLWriter(new OutputStreamWriter(new FileOutputStream("./MyDatabase/"+DatabaseName+"/"+TableName+"/"+writeFileName+".xml")),outputFormat1);
            xmlWriter1.write(document);
            xmlWriter1.close();
            System.out.println("插入成功");

        }else{
            //否则插入数据到最后一个文件
            writeFileName = TableName+writeFileElement.getText();
            //创建写入对象，获取记录数量
            File writeFile = new File("./MyDatabase/"+DatabaseName+"/"+TableName+"/"+writeFileName+".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(writeFile);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(TableName);

            //如果该文件中记录个数大于等于10，新建文件写入
            if(nodes.size() >= 10){
                Document newDocument = DocumentHelper.createDocument();
                Element newRoot = newDocument.addElement(TableName + "s");
                Element newChild = newRoot.addElement(TableName);
                //更新文件数量
                int fileAmountInt = (Integer.parseInt(writeFileElement.getText())+1);
                String fileAmount = (""+fileAmountInt);
                //设置新文件名
                String newFile = TableName + fileAmount;
                for(int i = 0; i < tmp1.size(); i++){
                    newChild.addAttribute(tmp1.get(i),tmp2.get(i));
                }

                //TODO
            }
        }
    }
}
