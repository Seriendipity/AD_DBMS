package SqlParser;
import SqlFunction.*;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SqlParserTest {
    public static void main(String[] args) throws IOException, DocumentException {
        //测试创建数据库
//        CreateDatabase database = new CreateDatabase("demo1");
//        CreateTable table = new CreateTable("demo1","table1");
        //测试删除数据库
//        CreateDatabase database = new CreateDatabase("demo2");
//        CreateTable table = new CreateTable("demo2","table1");
//        DropDatabase dropDatabase = new DropDatabase("demo2");
        //测试显示数据库
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
//        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("11","bj"));
        InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("13","RJ"));
//        System.out.println("This is a test for the sql");
    }
}
