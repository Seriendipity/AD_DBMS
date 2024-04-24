package SqlFunction;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

public class Judge {
    //判断数据库是否为空
    public static boolean isDatabaseEmpty(){
        //获得数据库的名字
        String DatabaseName = UseDatabase.databaseName;
        //如果数据库为空，表示没有进入数据库
        if(DatabaseName.equals("")){
            System.out.println("数据库名为空，请先进入数据库");
            return true;
        }
        return false;
    }
    //判断数据库是否存在
    public static boolean isDatabase(){
        String DatabaseName = UseDatabase.databaseName;
        File file = new File("./"+UseUser.userName+"/MyDatabase/");
        File[] files = file.listFiles();
        for(int i = 0; i < files.length;i++){
            if(files[i].getName().equals(DatabaseName));
            return true;
        }
        return false;
    }

    //判断表是否存在，并返回xml的配置文件
    public static File isTable(String DatabaseName, String TableName){
        File file = new File("./"+UseUser.userName+"/MyDatabase/"+DatabaseName+"/"+TableName+"");
        if(!file.exists()){
            System.out.println(TableName+"表格不存在");
            return null;
        }else{
            file = new File("./"+UseUser.userName+"/MyDatabase/"+DatabaseName+"/"+TableName+"/"+TableName+"-config.xml");
            return file;
        }
    }

    //判断表格是否存在，存在则返回表的物理层的最后一张子表的下标
    public static String lastFileName(String DatabaseName,String TableName) throws DocumentException {

        //找到写入的对象
        File writeFile = isTable(DatabaseName,TableName);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(writeFile);
        Element element = (Element) document.getRootElement().selectSingleNode(TableName);
        String lastFileNum = element.getText();
        //返回需要写写入的对象
        return lastFileNum;
    }

    //判断一个表是否建立的主键索引
    public static boolean hasIndex(String DatabaseName,String TableName) throws DocumentException {
        File file = new File("./"+UseUser.userName+"/MyDatabase/"+DatabaseName+"/"+TableName+"/"+TableName+"-config.xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element element = (Element) document.getRootElement().selectSingleNode("index");

        if(element.getText().equals("1")){
            return true;
        }
        return false;
    }

    //判断是不是通过逐渐进行查询
    //TODO
    public static boolean isIndex(File file, String key) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element element = (Element) document.getRootElement().selectSingleNode("indexName");

        return false;
    }

    //判断是否需要load index
    //TODO
    public static boolean needLoadIndex(){

        return false;
    }

    public static boolean findUser(String userName){
        File userFile = new File("./"+userName+"/user.xml");
        if(userFile.exists()){
            return true;
        }else{
            return false;
        }
    }
    //TODO:用户匹配完之后的权限控制
    public static boolean isUser(String userName,String key) throws DocumentException {
        File file  = new File("./"+userName+"/user.xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element rootElement = document.getRootElement();
        Element element = (Element)rootElement.selectSingleNode("user");
        if(element.attribute("userKey").getText().equals(key)){
            System.out.println("用户匹配成功");
            return true;
        }else{
            System.out.println("密码错误");
            return false;
        }
    }
}

