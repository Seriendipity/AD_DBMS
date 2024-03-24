package Utils;

import SqlFunction.CheckDatabaseLegal;
import SqlFunction.UseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
            createDatabase(dataBaseName);
        }else if(operation.equals("create table")){
            System.out.println("调用创建table方法");
            String tableName = list.get(1);
            List<String> listName = Collections.singletonList("Sno");
            createTable(UseDatabase.databaseName,tableName,listName);
        }else if(operation.equals("drop database")){
            System.out.println("调用删除数据库方法");
            String databaseName = list.get(1);
            dropDatabase(databaseName);
        }else if(operation.equals("drop table")){
            System.out.println("调用删除表的方法");
            String tableName = list.get(1);
            dropTable(UseDatabase.databaseName,tableName);
        }else if(operation.equals("use")){
            String databaseName = list.get(1);
            boolean isLegal = CheckDatabaseLegal.checkDBisLegal(databaseName);
            if(isLegal){
                System.out.println("切换到use的数据库");
                UseDatabase.databaseName = list.get(1);
            }else{
                System.out.println("当前并没有该数据库，请重新输入");
            }
        }
    }
}
