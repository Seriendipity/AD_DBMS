package SqlFunction;

import org.apache.xerces.xni.XMLAttributes;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DropColumn {
    public static void dropColumn(String DatabaseName, String TableName, List<String> columnName) throws DocumentException, IOException {
        File file = new File("./"+UseUser.userName+"/MyDatabase/"+DatabaseName+"/"+TableName+"/"+TableName+"-config.xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element rootElement = (Element) document.selectSingleNode(TableName+"s");
        for(int i = 0; i < columnName.size();i++) {
            Attribute attr = rootElement.attribute(columnName.get(i));
            attr.detach();
        }
        CreateTable.writeIO(file,document);
        System.out.println("成功删除列");
    }
}
