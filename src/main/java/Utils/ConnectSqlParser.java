package Utils;

import SqlFunction.*;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static SqlFunction.CreateDatabase.createDatabase;
import static SqlFunction.CreateTable.createTable;
import static SqlFunction.DropDatabase.dropDatabase;
import static SqlFunction.DropTable.dropTable;


public class ConnectSqlParser {
    public static void connectSql(List<List<String>> parameterList) throws IOException, DocumentException {
        List<String> list = new ArrayList<String>();
        list = parameterList.get(0);
        String operation = list.get(0);
        if(operation.equals("create database")){
            System.out.println("调用创建数据库方法");
            String dataBaseName = list.get(1);
            createDatabase(dataBaseName);
        }else if(operation.equals("create user")){
            System.out.println("创建一个用户");
            CreateUser.createUser();
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
        } else if(operation.equals("insert into")){
            System.out.println("调用insert方法");
            String insertTableName = list.get(1);
            List<String> insertColumnsTmp = parameterList.get(1);
            List<String> insertColumnsSubList = new ArrayList<>() ;
            List<String> insertColumns;
            if(insertColumnsTmp != null && !insertColumnsTmp.isEmpty()){
                insertColumnsSubList = insertColumnsTmp.subList(1,insertColumnsTmp.size());
                insertColumns = new ArrayList<String>(insertColumnsSubList);
            }else{
                insertColumns = new ArrayList<>();
            }
            List<String> insertColumnValuesTmp = parameterList.get(2);
            List<String> insertColumnValues = new ArrayList<>();
            if(insertColumnValuesTmp != null && !insertColumnValuesTmp.isEmpty()){
                for(int index = 0 ; index < insertColumnValuesTmp.size() ; index++){
                    if(index > 0 && index % 2 == 0){
                        insertColumnValues.add(insertColumnValuesTmp.get(index));
                    }
                }
            }
            InsertDataIntoTable.insertDataIntoTable(UseDatabase.databaseName,insertTableName,insertColumns,insertColumnValues);
        } else if (operation.equals("show tables")) {
            System.out.println("调用showTables方法");
            ShowTables.ShowTables(UseDatabase.databaseName);
        } else if (operation.equals("show databases")) {
            System.out.println("调用showDatabases方法");
            ShowDatabases.showDatabases();
        } else{
            System.out.println("有待开发");
        }
    }
}
