package Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static SqlFunction.CreateDatabase.createDatabase;
import static SqlFunction.CreateTable.createTable;
import static SqlFunction.DropDatabase.dropDatabase;
import static SqlFunction.DropTable.dropTable;


public class ConnectSqlParser {
    public static void connectSql(List<List<String>> parameterList) throws IOException {
        List<String> list = new ArrayList<String>();
        list = parameterList.get(0);
        String operation = list.get(0);
        if(operation.equals("create database")){
            System.out.println("调用创建数据库方法");
            String dataBaseName = list.get(1);
            createDatabase(dataBaseName);//OK
        }else if(operation.equals("create table")){
            System.out.println("调用创建table方法");
            String tableName = list.get(1);
            String dataBaseName = "test";
            createTable(dataBaseName,tableName);
        }else if(operation.equals("drop database")){
            System.out.println("调用删除数据库方法");
            String databaseName = list.get(1);
            dropDatabase(databaseName);
        }else if(operation.equals("drop table")){
            System.out.println("调用删除表的方法");
            String tableName = list.get(1);
            String databaseName = "test";
            dropTable(databaseName,tableName);
        }
    }
}
