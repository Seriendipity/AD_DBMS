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
    public static void insertDataIntoTable(String DatabaseName, String TableName, List<String> value1, List<String>value2) throws DocumentException, IOException {
        File dir = new File("./MyDatabase/"+DatabaseName+"");
        if(!dir.exists()){
            System.out.println(DatabaseName+"不存在！");
            return;
        }else {
            File table = new File("./MyDatabase/" + DatabaseName + "/" + TableName + "");
            if (!table.exists()) {
                System.out.println(TableName + "不存在！");
                return;
            } else {
                File configFile = new File("./MyDatabase/" + DatabaseName + "/" + TableName + "/" + TableName + "-config.xml");
                SAXReader configFileReader = new SAXReader();
                Document configFileDocument = configFileReader.read(configFile);
                Element writeFileElement = (Element) configFileDocument.getRootElement().selectSingleNode(TableName);
                Element insertTableElement = (Element) configFileDocument.getRootElement().selectSingleNode("insertTables");
                //判断是是否存在索引，存在的话那么插入数据时要修改索引
                Element indexElement = (Element) configFileDocument.getRootElement().selectSingleNode("index");
                boolean updateIndex = indexElement.getText().equals("1");

                String writeFileName;
                //插入数据
                //********************
                //用来统计外键所参考的表
                if (insertTableElement.selectNodes("insertTbales").size() > 0) {
                    writeFileName = TableName + insertTableElement.selectSingleNode("insertTable").getText();
                    //创建写入对象
                    File writeFile = new File("./MyDatabase/" + DatabaseName + "/" + TableName + "/" + writeFileName + ".xml");
                    SAXReader saxReader = new SAXReader();
                    Document document = saxReader.read(writeFile);
                    Element rootElement = document.getRootElement();
                    Element element = rootElement.addElement(TableName);
                    for (int i = 0; i < value1.size(); i++) {
                        element.addAttribute(value1.get(i), value2.get(i));
                    }
                    //若可插入文件记录数>=10,更新配置信息
                    List<Node> nodes = document.getRootElement().selectNodes(TableName);
                    if (nodes.size() >= CreateTable.entry_num) {
                        insertTableElement.remove(insertTableElement.selectSingleNode("insertTables"));
                        CreateTable.writeIO(configFile, configFileDocument);
                    }

                    CreateTable.writeIO(writeFile, document);

                    System.out.println("插入成功");

                    if (updateIndex) {
                        //TODO
                    }
                } else {
                    writeFileName = TableName + writeFileElement.getText();

                    //创建写入对象，获取记录数量
                    File writeFile = new File("./mydatabase/" + DatabaseName + "/" + TableName + "/" + writeFileName + ".xml");
                    SAXReader saxReader = new SAXReader();
                    Document document = saxReader.read(writeFile);
                    Element rootElement = document.getRootElement();

                    List<Node> nodes = rootElement.selectNodes(TableName);
                    if (nodes.size() >= CreateTable.entry_num) {
                        Document newDocument = DocumentHelper.createDocument();
                        Element newRoot = newDocument.addElement(TableName + "s");
                        Element newChild = newRoot.addElement(TableName);
                        //更新文件数量
                        int fileAmount = (Integer.parseInt(writeFileElement.getText())) + 1;
                        String fileAmountString = ("" + fileAmount);
                        //设置新的文件名称
                        String newFile = TableName + fileAmount;
                        for (int i = 0; i < value1.size(); i++) {
                            newChild.addAttribute(value1.get(i), value2.get(i));
                        }
                        CreateTable.writeIO(new File("./MyDatabase/" + DatabaseName + "/" + TableName + "/" + newFile + ".xml"), newDocument);

                        System.out.println("插入成功");
                        //更新配置文件中表的文件数量
                        writeFileElement.setText(fileAmountString);
                        CreateTable.writeIO(configFile, configFileDocument);
                        //插入之后更新索引
                        if (updateIndex) {
                            //TODO
                        }
                    } else {
                        //有空间就插入到最后一条记录
                        //创建新的节点
                        Element childElement = rootElement.addElement(TableName);
                        for (int i = 0; i < value1.size(); i++) {
                            childElement.addAttribute(value1.get(i), value2.get(i));
                        }

                        CreateTable.writeIO(writeFile, document);

                        System.out.println("插入成功");

                        //更新索引
                        if (updateIndex) {
                            //TODO
                        }
                    }

                }
            }

        }
    }
}