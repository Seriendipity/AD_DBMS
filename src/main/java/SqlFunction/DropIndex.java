package SqlFunction;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;

public class DropIndex {
    public static void dropIndex(String DatabaseName,String TableName,String indexFileName) throws DocumentException, IOException {
        //数据库不为空
        if(Judge.isDatabaseEmpty()){
            return;
        }
        //表存在且索引存在
        File file = Judge.isTable(DatabaseName,TableName);
        if(!Judge.hasIndex(DatabaseName,TableName)){
            System.out.println(TableName+"表没有建立主键索引");
            return;
        }
        //clear 该表格对应的B+树
        for(int i = 0; i < CreateIndex.treeList.size();i++){
            if(CreateIndex.treeList.get(i).containsKey(TableName)){
                CreateIndex.treeList.get(i).clear();
                break;
            }
        }
        //设置该表格的配置文件的索引情况为0；
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element element = (Element) document.getRootElement().selectSingleNode("index");
        element.setText("0");
        CreateTable.writeIO(file,document);
        //删除文件中的索引文件
        File indexFile = new File("./MyDataBase/"+DatabaseName+"/"+TableName+"/"+indexFileName+".xml");
        indexFile.delete();
        System.out.println("索引删除成功");
    }

}
