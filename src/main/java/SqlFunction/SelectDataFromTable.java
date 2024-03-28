package SqlFunction;

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
                    System.out.println("查询带查询");
                    //TODO
                }
            }
        }

    }

    public static void selectFromTable(String DatabaseName, String TableName) throws DocumentException {
        //获取最后一张子表的下标
        String lastFileNum = Judge.lastFileName(DatabaseName, TableName);
        for (int j = Integer.parseInt(lastFileNum); j >= 0; j--) {
            String num = "" + j;
            File file = new File("./MyDatabase/" + DatabaseName + "/" + TableName + "/" + TableName + num + ".xml");
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
            File file = new File("./MyDatabase/" + DatabaseName + "/" + TableName + "/" + TableName + num + ".xml");
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
                    if (attribute.getName().equals(condition[0]) && attribute.getText().equals(condition[1])) {
                        conditionFind = true;
                        find = true;
                        break;
                    }
                }
                if (find) {
                    for (Iterator i = list.iterator(); i.hasNext(); ) {
                        Attribute attribute = (Attribute) i.next();
                        System.out.println(attribute.getName() + "=" + attribute.getText() + " ");
                    }
                    System.out.println();
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
            File file = new File("./MyDatabase/" + DatabaseName + "/" + TableName + "/" + TableName + num + ".xml");

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
                        if (attribute.getName().equals(value1.get(k))) {
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
            File file = new File("./MyDatabase/" + DatabaseName + "/" + TableName + "/" + TableName + num + ".xml");
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
                    if(attribute.getName().equals(condition[0]) && attribute.getText().equals(condition[1])){
                        conditionFind = true;
                        find = true;
                        break;
                    }
                }
                if(find){
                    for(Iterator i = list.iterator();i.hasNext();){
                        Attribute attribute = (Attribute) i.next();
                        for(int k = 0; k < value1.size();k++){
                            if(attribute.getName().equals(value1.get(k))){
                            elementFind = true;

                            System.out.println(attribute.getName()+"="+attribute.getText()+" ");
                            break;
                        }
                    }
                }
                System.out.println();
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
}