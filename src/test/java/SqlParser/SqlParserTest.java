package SqlParser;
import SqlFunction.*;

import java.io.IOException;

public class SqlParserTest {
    public static void main(String[] args) throws IOException {
        //测试创建数据库
//        CreateDatabase database = new CreateDatabase("demo1");
//        CreateTable table = new CreateTable("demo1","table1");

     //   CreateDatabase database = new CreateDatabase("demo2");
        CreateTable table = new CreateTable("demo2","table1");
        DropDatabase dropDatabase = new DropDatabase("demo2");
        System.out.println("This is a test for the sql");
        System.out.println("This is CHENXIHONG's modification");
        System.out.println("Hello,World! Congratulations!");
    }
}
