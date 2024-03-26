package SqlFunction;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class UpdateDataFromTable {
    public static void updateDataFromTable(String DatabaseName,String TableName, List<List<String>> values) throws DocumentException, IOException {
        //判断数据库是否为空
        if(Judge.isDatabaseEmpty()){
            System.out.println("数据库为空");
            return;
        }
        //返回最后路一张的子表的配置文件；
        File configFile = Judge.isTable(DatabaseName,TableName);
        //find标记是否找到
        boolean find = false;
        //values2表示WHERE里面的列名称和值
        String[] values2 = values.get(1).get(0).split("=");
        //column 为WHERE里的列名
        String column = values2[0];
        //查询是否为主键
        if(Judge.isIndex(configFile,column)){

        }else{
            //遍历所有的文件j记录文件的下标，num用来计数
            String currentFile = Judge.lastFileName(DatabaseName,TableName);
            //倒过来遍历
            for(int j = Integer.parseInt(currentFile);j >= 0; j--){
                String num = ""+j;
                File file = new File("./MyDatabase/"+DatabaseName+"/"+TableName+"/"+TableName+num+".xml");
                find = update(TableName,file,values,values2);
                //找到记录
                if(find){
                    return;
                }
            }
            System.out.println("更新失败，未找到记录");
        }
     }
     public static boolean update(String TableName,File file,List<List<String>> values,String[] values2) throws DocumentException, IOException {
        boolean find = false;
        //创建解析器，获得document对象，获取根节点
         SAXReader saxReader = new SAXReader();
         Document document = saxReader.read(file);
         Element root = document.getRootElement();

         List<Node> nodes = root.selectNodes(TableName);
         for(Node node:nodes){
             //获得当前的节点
             Element currentNode = (Element) node;
             List<Attribute> list = currentNode.attributes();
             //遍历属性
             for(Iterator i = list.iterator(); i.hasNext();){
                 Attribute attribute = (Attribute) i.next();
                 //匹配到相同的列和值
                 if(attribute.getName().equals(values2[0])&& attribute.getText().equals(values2[1])){
                     find = true;
                     break;
                 }
             }
             //找到了对应的列
             if(find){
                 //values.get(0) ={["Sno = 011"],["Sname = cccc"],[]}
                 for(int k = 0; k < values.get(0).size(); k++){
                     String[] values1 = values.get(0).get(k).split("=");
                     for(Iterator i = list.iterator();i.hasNext();){
                         Attribute attribute = (Attribute) i.next();
                         if(attribute.getName().equals(values1[0])){
                             attribute.setText(values1[1]);
                         }
                     }
                 }
                 CreateTable.writeIO(file,document);
                 System.out.println("更新成功");
                 return true;
             }
         }
         return false;
     }
}
