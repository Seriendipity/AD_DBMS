package SqlFunction;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AddColumn {
    public static void addColumn(String DatabaseName, String TableName,List<String> column) throws DocumentException, IOException {
        File file = new File("./"+UseUser.userName+"/MyDatabase/"+DatabaseName+"/"+TableName+"/"+TableName+"-config.xml");
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(file);
        Element rootElement = (Element) document.getRootElement();
        for(int i = 0; i < column.size(); i++){
            String[] list = column.get(i).split(" ");
            rootElement.addAttribute(list[0],list[1]);
        }
        CreateTable.writeIO(file,document);
      //  System.out.println("成功增加新的列");
    }
}
