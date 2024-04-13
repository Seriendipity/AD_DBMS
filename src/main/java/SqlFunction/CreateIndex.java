package SqlFunction;

import BPlusTree.*;
import BPlusTree.BPlusTree;
//import com.ibm.icu.impl.data.CalendarData_ms_MY;
import org.dom4j.*;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CreateIndex {
    static public String indexFileName;
    //B+树的地方
    static public Map<String, BPlusTree> bMap = new HashMap<String,BPlusTree>();
    //存储所有索引B+树的list,每次进入系统都会把索引文件以B+树的形式加载到内存当中去
    static public List<Map<String,BPlusTree>> treeList = new ArrayList<Map<String,BPlusTree>>();

    public static void createIndex(String DatabaseName,String TableName,String IndexName,String IndexFileName) throws DocumentException, IOException {
        //判断数据库是否为空
        if(Judge.isDatabaseEmpty()){
            System.out.println("数据库为空");
            return;
        }
        //判断是否有表格
        File configFile = Judge.isTable(DatabaseName,TableName);
        //是否有索引
        if(Judge.hasIndex(DatabaseName,TableName)){
            System.out.println(TableName+"表存在主键索引");
            return;
        }

        //声明一个索引id的list
        List<List<String>> indexFileList = new ArrayList<List<String>>();

        SAXReader configFileSaxReader = new SAXReader();
        Document configFileDocument = configFileSaxReader.read(configFile);
        Element fileNameElement = (Element)configFileDocument.getRootElement().selectSingleNode(TableName);

        //从最后一个表开始往前遍历
        for(int j = Integer.parseInt(fileNameElement.getText());j >= 0; j--) {
            String fileName = TableName + Integer.toString(j);
            //打开逻辑表的物理层的文件，解析每一份文件，获取并列的所有记录的父节点
            File file = new File("./MyDatabase/" + DatabaseName + "/" + TableName + "/" + fileName + ".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(file);
            Element rootElement = document.getRootElement();
            List<Node> nodes = rootElement.selectNodes(TableName);


            for(Node node : nodes){
                Element element = (Element) node;
                List<Attribute> list = element.attributes();
                for(Iterator i = list.iterator(); i.hasNext();){
                    Attribute attribute = (Attribute) i.next();
                    if(attribute.getName().equals(IndexName)){
                        //暂时存入每一个id对应的文件名
                        List<String> tmp = new ArrayList<String>();
                        tmp.add(attribute.getText());
                        tmp.add(fileName);
                        indexFileList.add(tmp);
                        break;
                    }
                }
            }
        }
        //构建B+树，存入B+树List
        BPlusTree newTree = Main.createBPlusTree(indexFileList);
        Map<String,BPlusTree> tmpMap = new HashMap<String,BPlusTree>();
        tmpMap.put(TableName,newTree);
        treeList.add(tmpMap);

        System.out.println("索引创建成功");
        indexFileName = IndexFileName;
        //更新配置文件，记录表中是否建立索引，建立索引为true，名称为index
        Element indexElement = (Element) configFileDocument.getRootElement().selectSingleNode("index");
        indexElement.setText("1");
        Element indexName = (Element) configFileDocument.getRootElement().selectSingleNode("indexName");
        indexName.setText(IndexName);
        //写入操作
        CreateTable.writeIO(configFile,configFileDocument);
        //写入索引文件
        File indexFile = new File("./MyDatabase/"+IndexFileName+".xml");
        Document document =  DocumentHelper.createDocument();
        Element rootElem = document.addElement(TableName+"s");

        for(int i = 0; i < indexFileList.size(); i++){
            rootElem.addAttribute("k"+indexFileList.get(i).get(0),IndexName);
        }
        //写入操作
        CreateTable.writeIO(indexFile,document);

        return;
    }
    public static BPlusTree findTree(String TableName){
        for(int i = 0; i < treeList.size(); i++){
            if(treeList.get(i).containsKey(TableName)){
                return treeList.get(i).get(TableName);
            }
        }
        return null;
    }

    //插入新数据后修改索引文件和B+树
    public static void updateIndexInsert(String TableName,String key,String value,String indexFileName) throws DocumentException, IOException {
        for(int i = 0; i < treeList.size();i++){
            if(treeList.get(i).containsKey(TableName)){
                treeList.get(i).get(TableName).insert(Integer.parseInt(key),value);
                break;
            }
        }
        File file = new File("./MyDatabase/"+indexFileName+".xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element root = document.getRootElement();
        for(Iterator i = root.elementIterator();i.hasNext();){
            Element element = (Element)i.next();
            if(element.getName().equals(TableName)){
                element.addAttribute("k"+key,value);
                break;
            }
        }
        CreateTable.writeIO(file,document);
        System.out.println("索引更新成功");
    }
    //更新数据后修改索引文件和B+树
    public static void updateIndexUpdate(String TableName,String key,String value,String indexFileName) throws DocumentException, IOException {
        for(int i = 0; i < treeList.size();i++){
            if(treeList.get(i).containsKey(TableName)){
                treeList.get(i).get(TableName).update(Integer.parseInt(key),value);
                break;
            }
        }
        //更新索引文件
        File file  = new File("./MyDatabase/"+indexFileName+".xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element root = document.getRootElement();
        for(Iterator i = root.elementIterator();i.hasNext();){
            Element element = (Element) i.next();
            if(element.getName().equals(TableName)){
                List<Attribute> list = element.attributes();
                for(Iterator j = list.iterator(); j.hasNext();){
                    Attribute attribute = (Attribute) j.next();
                    if(attribute.getName().equals("k"+key)){
                        attribute.setValue(value);
                        break;
                    }
                }
                break;
            }
        }
        CreateTable.writeIO(file,document);
        System.out.println("索引更新成功");
    }
    //删除数据后修改索引文件和B+树
    public static void updateIndexDelete(String TableName,String key,String IndexFileName) throws DocumentException, IOException {
        //删除B+树节点
        for(int i = 0; i < treeList.size();i++){
            if(treeList.get(i).containsKey(TableName)){
                treeList.get(i).get(TableName).delete(Integer.parseInt(key));
            }
        }
        //更新索引文件
        File file =  new File("./MyDatabase/"+IndexFileName+".xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element root = document.getRootElement();
        for(Iterator i = root.elementIterator();i.hasNext();){
            Element element = (Element) i.next();
            if(element.getName().equals(TableName)){
                List<Attribute> list = element.attributes();
                for(Iterator j = list.iterator(); j.hasNext();){
                    Attribute attribute = (Attribute)j.next();
                    if(attribute.getName().equals("k"+key)){
                        element.remove(attribute);
                        break;
                    }
                }
                break;
            }
        }
        CreateTable.writeIO(file,document);
        System.out.println("索引更新成功");
    }
    //每次登录加载索引
    public static void loadIndex(String IndexFileName) throws DocumentException {
        File file = new File("./MyDatabase/"+IndexFileName+"xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element element = document.getRootElement();
        //遍历每个建有主键索引的表节点
        for(Iterator i = element.elementIterator();i.hasNext();){
            Element tableElement = (Element) i.next();
            List<List<String>> list = new ArrayList<List<String>>();
            List<Attribute> listAttribute = tableElement.attributes();
            //遍历该表的所有索引关系
            for(Iterator j = listAttribute.iterator();j.hasNext();){
                Attribute attribute = (Attribute) j.next();
                List<String> tmp = new ArrayList<String>();
                tmp.add(attribute.getName());
                tmp.add(attribute.getText());
                list.add(tmp);
            }
            //构建该表对应的B+树
            BPlusTree newTree = Main.loadIndex(list);
            Map<String,BPlusTree> tmpMap = new HashMap<String,BPlusTree>();
            tmpMap.put(tableElement.getName(),newTree);
            treeList.add(tmpMap);
        }
    }
}
