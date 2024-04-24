package SqlFunction;

import BPlusTree.BPlusTree;
import com.sun.source.tree.ArrayAccessTree;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.w3c.dom.html.HTMLTableElement;

import javax.swing.text.TabExpander;
import javax.xml.crypto.Data;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SelectDataFromTable {
    //FIXME:select存在的问题是，他返回的结果是从找到的第一个匹配的元组及以后得所有元组。
    //FIXME:但神奇的是，select*貌似没有问题，能够正常工作
    //根据用户传入的参数来决定采哪种筛选的措施，这里用多态的方式
    public static void select(String DatabaseName, String TableName, List<String> Colunms, List<String> Conditions) throws DocumentException {

        //判断数据库是否为空
        if (Judge.isDatabaseEmpty()) {
            System.out.println(DatabaseName + "为空");
            return;
        }
        //指定的表格是否存在
        File configFile = Judge.isTable(DatabaseName, TableName);
        if (configFile == null) {
            System.out.println(TableName + "为空");
            return;
        }

        //SELECT * FROM tableName / SELECT * FROM tableName WHERE column = value
        //前面无指定的列
        if (Colunms == null) {
            //查询语句为SELECT * FROM tableName
            //条件为空
            if (Conditions == null) {
                selectFromTable(DatabaseName, TableName);
            }else{
                String[] keyValue = Conditions.get(1).split("=");
                String key = keyValue[0];
                //如果表格没有建立主键索引或者不是通过主键查询，调用未创建索引的方法
                if(!Judge.hasIndex(DatabaseName,TableName) || !Judge.isIndex(configFile,key)){
                    selectAllFromTable(DatabaseName,TableName,Conditions);
                }else{
                    System.out.println("查询带查询");
                    //TODO
                    selectWithIndex(DatabaseName,TableName,Colunms,Conditions);
                }
            }

        }//列名称不为空
        else{
            //但是条件为空
            if(Conditions == null){
                selectFromTable(DatabaseName,TableName,Colunms);
            }else {
                String[] keyValue = Conditions.get(1).split("=");
                String key = keyValue[0];
                //如果表格没有建立主键索引或者不是通过主键查询，调用未创建索引的方法
                if(!Judge.hasIndex(DatabaseName,TableName) || !Judge.isIndex(configFile,key)){
                    selectFromTable(DatabaseName,TableName,Colunms,Conditions);
                }else{
                    System.out.println("带索引的查询");
                    //TODO
                    selectWithIndex(DatabaseName,TableName,Colunms,Conditions);
                }
            }
        }

    }

    public static void selectFromTable(String DatabaseName, String TableName) throws DocumentException {
        //获取最后一张子表的下标
        String lastFileNum = Judge.lastFileName(DatabaseName, TableName);
        for (int j = Integer.parseInt(lastFileNum); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./"+UseUser.userName+"/"+DatabaseName + "/" + TableName + "/" + TableName + num + ".xml");
            //解析xml文件
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(file);
            //获取根节点
            Element rootElement = document.getRootElement();
            //获得节点名字为TableName的节点list
            List<Node> nodes = rootElement.selectNodes(TableName);
            for (Node node : nodes) {
                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    System.out.println(attribute.getName() + "=" + attribute.getText() + " ");
                }
                System.out.println();
            }
        }
    }

    //SELECT * FROM TABLENAME WHERE COLUMN = VALUE
    public static void selectAllFromTable(String DatabaseName, String TableName, List<String> value1) throws DocumentException {
        //表格存在返回最后一张子表的下标
        String lastFileNum = Judge.lastFileName(DatabaseName, TableName);
        //是否找到记录
        boolean conditionFind = false;
        boolean find;
        //存在where条件的condition
        String[] condition = value1.get(1).split("=");

        for (int j = Integer.parseInt(lastFileNum); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./"+UseUser.userName+"/"+DatabaseName + "/" + TableName + "/" + TableName + num + ".xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(file);
            Element rootElement = document.getRootElement();
            List<Node> nodes = rootElement.selectNodes(TableName);
            for (Node node : nodes) {
                find = false;
                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    if (attribute.getName().toLowerCase().equals(condition[0]) && attribute.getText().equals(condition[1])) {
                        conditionFind = true;
                        find = true;
                        if (find) {
                            for (Iterator k = list.iterator(); k.hasNext(); ) {
                                Attribute attribute1 = (Attribute) k.next();
                                System.out.println(attribute1.getName() + "=" + attribute1.getText() + " ");
                            }
                            System.out.println();
                        }
                        find = false;
                    }
                }

            }
        }
        if (!conditionFind) {
            System.out.println("未找到记录");
            return;
        }
    }

    //SELECT COLUMN1，COLUMN2，FROM TABLENAME
    public static void selectFromTable(String DatabaseName, String TableName, List<String> value1) throws DocumentException {
        //表存在返回最后一个子表
        String lastFileNum = Judge.lastFileName(DatabaseName, TableName);
        //标记是否找到
        boolean columnsFind = false;

        for (int j = Integer.parseInt(lastFileNum); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./"+UseUser.userName+"/"+DatabaseName + "/" + TableName + "/" + TableName + num + ".xml");

            //解析XML文件
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(file);
            Element rootElement = document.getRootElement();
            List<Node> nodes = rootElement.selectNodes(TableName);

            for (Node node : nodes) {
                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();
                for (Iterator i = list.iterator(); i.hasNext(); ) {
                    Attribute attribute = (Attribute) i.next();
                    //把列的属性全部打印下来
                    for (int k = 0; k < value1.size(); k++) {
                        //列名相同
                        if (attribute.getName().toLowerCase().equals(value1.get(k))) {
                            columnsFind = true;
                            System.out.println(attribute.getName() + "=" + attribute.getText() + " ");
                            break;
                        }
                    }
                }
                System.out.println();
            }
        }
            if (!columnsFind) {
                System.out.println("未找到列");
                return;
            }

    }
    //SELECT COLUMN1,COLUMN2 FROM TABLENAME WHERE COLUMN = VALUE
    public static void selectFromTable(String DatabaseName,String TableName,List<String>value1,List<String>value2) throws DocumentException {
        //存在表返回最后一个
        String lastFileNum = Judge.lastFileName(DatabaseName, TableName);
        //把WHERE的条件存入condition数组当中
        String[] condition = value2.get(1).split("=");
        boolean conditionFind = false;
        boolean elementFind = false;
        boolean find = false;

        for(int j = Integer.parseInt(lastFileNum);j>=0;j--){
            String num = "" + j;
            File file = new File("./"+UseUser.userName+"/"+DatabaseName + "/" + TableName + "/" + TableName + num + ".xml");
            //解析XML
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(file);
            Element rootElement = document.getRootElement();

            List<Node> nodes = rootElement.selectNodes(TableName);

            for(Node node: nodes){
                Element node1 = (Element) node;
                List<Attribute> list = node1.attributes();
                for(Iterator i = list.iterator();i.hasNext();){
                    Attribute attribute = (Attribute) i.next();
                    if(attribute.getName().toLowerCase().equals(condition[0]) && attribute.getText().equals(condition[1])){
                        conditionFind = true;
                        find = true;
                    }if(find){
                        for(Iterator n = list.iterator();n.hasNext();) {
                            Attribute attribute1 = (Attribute) n.next();
                            for (int k = 0; k < value1.size(); k++) {
                                if (attribute1.getName().toLowerCase().equals(value1.get(k))) {
                                    elementFind = true;
                                    System.out.println(attribute1.getName() + "=" + attribute1.getText() + " ");
                                    break;
                                }
                            }
                            find = false;
                            System.out.println();
                        }
                    }
                }
            }
        }
        if(!conditionFind){
            System.out.println("未找到记录");
            return;
        }
        if(conditionFind && !elementFind){
            System.out.println("未找到列");
            return;
        }
    }
    //TODO
    //建立索引后的查询select 列名称1，列名称2 from where 列名称 = 列值
    public static void selectWithIndex(String DatabaseName, String TableName, List<String> value1, List<String> value2) throws DocumentException {
        //存where条件的condition数组
        String[] condition = new String[0];
        condition = value2.get(1).split("=");
        int key = Integer.parseInt(condition[1]);
        //找到该表索引对应的B+树
        BPlusTree newTree = CreateIndex.findTree(TableName);
        String fileName = newTree.search(key);
        boolean condition_Find = false;
        boolean conditionFind = false;
        boolean elementFind = false;

        File file = new File("./"+UseUser.userName+"/MyDatabase/"+DatabaseName + "/"+ TableName + "/" +fileName + ".xml");
        //解析XML
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element rootElement = document.getRootElement();

        List<Node> nodes = rootElement.selectNodes(TableName);

        for(Node node : nodes){
            Element node1 = (Element) node;
            List<Attribute> list = node1.attributes();
            for(Iterator i = list.iterator();i.hasNext();){
                Attribute attribute = (Attribute) i.next();
                if(attribute.getName().toLowerCase().equals(condition[0]) && attribute.getText().equals(condition[1])){
                    condition_Find = true;
                    conditionFind = true;
                    if(conditionFind){
                        for(Iterator j = list.iterator(); j.hasNext();){
                            Attribute attribute1 = (Attribute) i.next();
                            System.out.println(attribute1.getName() + "=" +attribute1.getText() + " ");
                        }
                    }
                }
            }
            System.out.println();
        }
        if(!condition_Find){
            System.out.println("未找到记录");
            return;
        }
    }
}
