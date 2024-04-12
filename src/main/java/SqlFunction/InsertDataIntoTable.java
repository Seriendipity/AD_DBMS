package SqlFunction;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class InsertDataIntoTable {
    public static void insertDataIntoTable(String DatabaseName, String TableName, List<String> value1, List<String> value2) throws DocumentException, IOException {
        //数据库是否合法
        if(!Judge.isDatabase()){
            return;
        }
        //表存在则打开配置文件
        File configFile = Judge.isTable(DatabaseName,TableName);
        //解析配置文件
        SAXReader configFileReader = new SAXReader();
        Document configFileDocument = configFileReader.read(configFile);
        Element writeFileElement = (Element) configFileDocument.getRootElement().selectSingleNode(TableName);
        Element insertableElement = (Element) configFileDocument.getRootElement().selectSingleNode("insertTables");
        //判断是否存在索引，TODO
        boolean needUpdateIndex = Judge.hasIndex(DatabaseName,TableName);
        //写入的文件名
        String writeFileName;
        if(insertableElement.selectNodes("insertTables").size()>0){

        }
    }
}