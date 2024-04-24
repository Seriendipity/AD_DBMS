package SqlFunction;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class InsertDataIntoTable {
    public static void insertDataIntoTable(String DatabaseName, String TableName, List<String> value1, List<String> value2) throws DocumentException, IOException {
        //数据库是否合法
        if(!Judge.isDatabase()){
            return;
        }
        //表存在则打开配置文件
        File configFile = Judge.isTable(DatabaseName,TableName);
        //解析配置文件
        SAXReader configFileReader = new SAXReader();
        Document configFileDocument = configFileReader.read(configFile);
        Element writeFileElement = (Element) configFileDocument.getRootElement().selectSingleNode(TableName);
        Element insertableElement = (Element) configFileDocument.getRootElement().selectSingleNode("insertTables");
        //判断是否存在索引，TODO
        boolean needUpdateIndex = Judge.hasIndex(DatabaseName,TableName);
        //写入的文件名
        String writeFileName;
        if(insertableElement.selectNodes("insertTables").size()>0){
            writeFileName = TableName + insertableElement.selectSingleNode("insertTables").getText();
            //创建写入对象，获取记录数量
            File writeFile = new File("./"+UseUser.userName+"/MyDatabase/"+DatabaseName+"/"+TableName+"/"+writeFileName+".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(writeFile);
            Element rootElement = document.getRootElement();
            Element element = rootElement.addElement(TableName);
            for(int i = 0; i < value1.size();i++){
                element.addAttribute(value1.get(i),value2.get(i));
            }
            //若可插入文件记录数≥10，更新配置信息
            List<Node> nodes = document.getRootElement().selectNodes(TableName);
            if(nodes.size() >= CreateTable.entry_num){
                insertableElement.remove(insertableElement.selectSingleNode("insertTables"));
                CreateTable.writeIO(writeFile,document);
            }
            CreateTable.writeIO(writeFile,document);

            System.out.println("数据插入成功");
            //插入数据后更新索引
            if(needUpdateIndex){
                String indexFileName = CreateIndex.indexFileName;
                CreateIndex.createIndex(TableName,value2.get(1),writeFileName,indexFileName);
            }
        }
        //否则在最后一条记录后插入
        else{
            writeFileName = TableName + writeFileElement.getText();
            //创建写入对象，获取记录数量
            File writeFile = new File("./"+UseUser.userName+"/MyDatabase/"+DatabaseName+"/"+TableName+"/"+writeFileName+".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(writeFile);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(TableName);

            //如果该文件中记录个数>=10,新建文件写入
            if(nodes.size() >= CreateTable.entry_num){
                Document newDocument = DocumentHelper.createDocument();
                Element newRoot = newDocument.addElement(TableName + "s");
                Element newChild = newRoot.addElement(TableName);
                //更新文件数量
                int fileAmountInt = (Integer.parseInt(writeFileElement.getText())+1);
                String fileAmount = ("" + fileAmountInt);
                for(int i = 0; i < value1.size(); i++){
                    newChild.addAttribute(value1.get(i),value2.get(i));
                }
                CreateTable.writeIO(writeFile,document);

                System.out.println("插入成功");
                //更新配置文件中表的文件数量
                writeFileElement.setText(fileAmount);
                CreateTable.writeIO(configFile,configFileDocument);
                //插入数据后更新索引
                if(needUpdateIndex){
                    String indexFileName = CreateIndex.indexFileName;
                    CreateIndex.updateIndexInsert(DatabaseName,TableName, value2.get(1),writeFileName,indexFileName);
                }
            }
            //否则在最后一条记录后插入
            else{
                //创建新节点
                Element childElement = rootElement.addElement(TableName);
                for(int i = 0; i < value1.size(); i++){
                    childElement.addAttribute(value1.get(i),value2.get(i));
                }
                CreateTable.writeIO(writeFile,document);

                System.out.println("插入成功");
                //插入数据后更新索引
                if(needUpdateIndex){
                    String indexFileName = CreateIndex.indexFileName;
                    CreateIndex.updateIndexInsert(DatabaseName,TableName,value2.get(1),writeFileName,indexFileName);
                }
            }
        }
    }
}