package SqlFunction;

import BPlusTree.BPlusTree;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.time.chrono.ThaiBuddhistChronology;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DropDataFromTable {
    //delete from TableName where column = value
    public static void dropFromTable(String DatabaseName, String TableName, List<String> temp) throws DocumentException, IOException {
        //数据库是否为空
        File dir = new File("./MyDatabase/"+DatabaseName+"");
        if(Judge.isDatabaseEmpty()){
            return;
        }
        //表存在则返回物理层的最后一张子表的下标并且得到配置文件
        File configFile = Judge.isTable(DatabaseName,TableName);
        String writeFileLastNum = Judge.lastFileName(DatabaseName,TableName);
        //获取where列的名称
        String[] columns = temp.get(0).split("=");
        String column = columns[0];
        //find用来标记是否找到了数据
        boolean find = false;
        //非主键查询
        if(!Judge.isIndex(configFile,column)){
            for(int i = Integer.parseInt(writeFileLastNum); i >= 0; i--){
                //traverseFile 用来遍历所有文件
                String lastNum = ""+i;
                //创建写入的对象，创建sax的解析器，document对象，获得root节点
                File file = new File("./MyDatabase/"+DatabaseName+"/"+TableName+"/"+TableName+lastNum+".xml");
                find = delete(file, DatabaseName,TableName,columns,lastNum);
                if(find){
                    return;
                }
            }
            System.out.println("没有找到要删除的记录");
        }else{
            //主键查询删除
            //TODO:
            BPlusTree tree = CreateIndex.findTree(TableName);
            String fileName = tree.search(Integer.parseInt(columns[1]));
            String num = fileName.substring(fileName.length()-1,fileName.length());
            File file = new File("./MyDatabase/"+DatabaseName+"/"+TableName+"/"+fileName+".xml");
            find = delete(file,DatabaseName,TableName,columns,num);
            //删数据后更新索引
            String indexFileName = CreateIndex.indexFileName;
            if(find){
                CreateIndex.updateIndexDelete(DatabaseName,TableName,columns[1],indexFileName);
                return;
            }
            System.out.println("没找到要删除的记录");
        }
    }

    public static boolean delete(File file,String DatabaseName,String TableName,String[] value, String lastNum) throws DocumentException, IOException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element root = document.getRootElement();
        boolean find = false;

        //设置一个节点用来遍历表格中的所有数据
        Element element;
        List<Node> nodes = root.selectNodes(TableName);
        boolean isFull = false;
        if(nodes.size() == 10){
            isFull = true;
        }
        for(Node node: nodes){
            //获取当前节点的所有属性值,也就是获取了表格中的某一行的所有数据
            Element currentNode = (Element) node;
            List<Attribute> lists = currentNode.attributes();
            for(Iterator i = lists.iterator(); i.hasNext();){
                //从第一个开始获取
                Attribute attribute = (Attribute) i.next();
                if(attribute.getName().equals(value[0]) && attribute.getText().equals(value[1])){
                    find = true;
                    break;
                }
            }
            if(find){

                root.remove(currentNode);
                //写入到文件当中
                CreateTable.writeIO(file,document);

                //更新xml配置文件,确保表格的信息准确
                if(isFull){
                    File file1 = new File("./MyDatabase/"+DatabaseName+"/"+TableName+"-config.xml");
                    SAXReader saxReader1 = new SAXReader();
                    Document document1 = saxReader1.read(file1);
                    Element element1 = (Element) document1.getRootElement().addElement("insertTables");
                    element1.addElement("insertTable").setText(lastNum);
                    //写入到IO流当中
                    CreateTable.writeIO(file1,document1);
                }
                System.out.println("删除记录成功");
                return true;
            }

        }
        return false;
    }
}
