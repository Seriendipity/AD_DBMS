package Utils;

import SqlFunction.*;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.*;

import static SqlFunction.CreateDatabase.createDatabase;
import static SqlFunction.CreateIndex.createIndex;
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
            //CreateUser.createUser();
        }else if(operation.equals("create table")){
            System.out.println("调用创建table方法");
            String tableName = list.get(1);
            List<String> listNameTmp = parameterList.get(1);
            List<String> listName = listNameTmp.subList(1,listNameTmp.size());
            createTable(UseDatabase.databaseName,tableName,listName);
        } else if (operation.equals("create index")) {
            System.out.println("调用create index方法");
            String indexFileName = list.get(1);
            String tableName = parameterList.get(1).get(1);
            String[] parts = parameterList.get(2).get(1).split(" ");
            String indexName = parts[0];
            createIndex(UseDatabase.databaseName,tableName,indexName,indexFileName);
        } else if (operation.equals("create index on")) {
            System.out.println("调用create index on方法");
            String tableName = list.get(1);
            String indexFileName = tableName+"_index";
            String[] parts = parameterList.get(1).get(1).split(" ");
            String indexName = parts[0];
            createIndex(UseDatabase.databaseName,tableName,indexName,indexFileName);
        } else if(operation.equals("drop database")){
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
            List<String> insertColumnsSubList = new ArrayList<>() ;//获取从第二个位置开始的所有列名
            List<String> insertColumns;
            if(insertColumnsTmp != null && !insertColumnsTmp.isEmpty()){
                insertColumnsSubList = insertColumnsTmp.subList(1,insertColumnsTmp.size());
                insertColumns = new ArrayList<String>(insertColumnsSubList);
            }else{
                insertColumns = new ArrayList<>();
            }
            List<String> insertColumnValuesTmp = parameterList.get(2);//获取带有所有用户输入的数据
            List<String> insertColumnValuesCombined = new ArrayList<>();
            List<String> insertColumnValues = new ArrayList<>() ;
            if(insertColumnValuesTmp != null && !insertColumnValuesTmp.isEmpty()){
                for(int index = 0 ; index < insertColumnValuesTmp.size() ; index++){
                    if(index > 0 && index % 2 == 0){
                        insertColumnValuesCombined.add(insertColumnValuesTmp.get(index));
                    }
                }
            }
            for(String insert : insertColumnValuesCombined){
                String[] tmp = insert.split(",");
                insertColumnValues.addAll(Arrays.asList(tmp));
                InsertDataIntoTable.insertDataIntoTable(UseDatabase.databaseName,insertTableName,insertColumns,insertColumnValues);
                insertColumnValues.clear();
            }
        } else if (operation.equals("update table")) {
            String tableName = list.get(1);
            List<String> UpdateInformationTmp = parameterList.get(1);
            List<List<String>> values = new ArrayList<List<String>>();
            List<String> ConditionInformationTmp = parameterList.get(2);
            List<String> ConditionInformation = ConditionInformationTmp.subList(1,ConditionInformationTmp.size());
            List<String> UpdateInformation = UpdateInformationTmp.subList(1,UpdateInformationTmp.size());
            values.add(UpdateInformation);
            values.add(ConditionInformation);
            UpdateDataFromTable.updateDataFromTable(UseDatabase.databaseName,tableName,values);
        } else if (operation.equals("select")) {
            System.out.println("调用select方法");
            String tableName = parameterList.get(1).get(1);
            List<String> selectedColumn = parameterList.get(0).subList(1,parameterList.get(0).size());
            List<String> selectedCondition;
            if(parameterList.size() !=3){
                selectedCondition = null;
            }else{
                selectedCondition = parameterList.get(2);
            }
            SelectDataFromTable.select(UseDatabase.databaseName,tableName,selectedColumn,selectedCondition);
        } else if (operation.equals("select *") || operation.equals("select*")) {
            System.out.println("调用select*方法");
            String tableName = parameterList.get(1).get(1);
            List<String> selectedColumn = null;
            List<String> selectedCondition;
            if(parameterList.size() != 3){
                selectedCondition = null;
            }else{
                selectedCondition = parameterList.get(2);
            }
            SelectDataFromTable.select(UseDatabase.databaseName,tableName,selectedColumn,selectedCondition);
        } else if (operation.equals("show tables")) {
            System.out.println("调用showTables方法");
            ShowTables.ShowTables(UseDatabase.databaseName);
        } else if (operation.equals("describe")) {
            System.out.println("调用ShowTableStruct方法");
            String tableName = list.get(1);
            ShowTableStruct.showTableStruct(UseDatabase.databaseName,tableName);
        } else if (operation.equals("show databases")) {
            System.out.println("调用showDatabases方法");
            ShowDatabases.showDatabases();
        } else{
            System.out.println("有待开发");
        }
    }
}