package SqlFunction;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.List;

public class ShowTableStruct {
    public static void showTableStruct(String DatabaseName,String TableName) throws DocumentException {
        //判断数据库是否为空
        if(Judge.isDatabaseEmpty()){
            System.out.println(DatabaseName+"为空");
            return;
        }
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read("./MyDatabase/"+DatabaseName+"/"+TableName+"/"+TableName+"-config.xml");
        Element rootElement = document.getRootElement();
        List<Attribute> list = rootElement.attributes();
        System.out.println("NAME:TYPE");
        for(Attribute attribute : list){
            System.out.println(attribute.getName() + ":" + attribute.getValue());
        }
    }
}
