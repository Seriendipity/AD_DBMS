package SqlParser;
import SqlFunction.*;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
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
       //UseDatabase.databaseName = "demo1";
       // InsertDataIntoTable.insertDataIntoTable("demo1","table2", List.of("Sno","Sname"),List.of("13","RJ"));

       // UpdateDataFromTable.updateDataFromTable("demo1","table2",List.of(List.of("Sno=011"),List.of("Sname=jj")));
      //  ShowTableStruct.showTableStruct("demo1","table2");
      List<String> list = new ArrayList<>();
    //  list.add("Where");
  //    list.add("Sname=jj");
//      SelectDataFromTable.select("demo1","table2", null,list);
//     System.out.println("This is 1a test for the sql");

      // CreateIndex.createIndex("demo1","table2","Sno","table2_index");
    //   DropIndex.dropIndex("demo1","table2","table2_index");

     //   list.add("grade int-primary");
    //    AddColumn.addColumn("demo1","table2",list);
        //DropColumn.dropColumn(UseDatabase.databaseName,"table2",list);
       // CreateUser.createUser("cxh");
       // Judge.isUser("cxh","123456");
        UseUser.setUserName("zsm");
    //    CreateDatabase.createDatabase("demo1");
   //     list.add("Sno int");
    //    CreateTable.createTable("demo1","table1",list);
//        InsertDataIntoTable.insertDataIntoTable("demo1","table1", List.of("Sno"),List.of("11"));

    }
}
