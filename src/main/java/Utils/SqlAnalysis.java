package Utils;

import SqlParser.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class SqlAnalysis {
    public static List<List<String>> generateParser(String sql){
        BaseParser bp = null;

        if(contains(sql,"(create database)(.+)")){
            System.out.println("匹配正则表达式：create database");
            bp = new CreateDatabaseSqlParser(sql);
        }else if(contains(sql,"(create table)(.+)")){
            System.out.println("匹配正则表达式: create table");
            bp = new CreateTableSqlParser(sql);
        }else if (contains(sql,"(create user)(.+)")){
            System.out.println("匹配正则表达式：create user");
            bp = new CreateUserSqlParser(sql);
        }else if(contains(sql,"(create index on)(.+)")){
            System.out.println("匹配正则表达式：create index on ");
            bp = new CreateIndexSqlParser(sql);
        } else if (contains(sql,"(create index)(.+)(on)(.+)")) {
            System.out.println("匹配正则表达式：create index XX on");
            bp = new CreateIndexWithNameSqlParser(sql);
        } else if(contains(sql,"(drop database)(.+)")){
            System.out.println("匹配正则表达式：drop database");
            bp = new DropDatabaseSqlParser(sql);
        }else if(contains(sql,"(drop index)(.+)")){
            System.out.println("匹配正则表达式：drop index");
            bp = new DropIndexSqlParser(sql);
        }else if(contains(sql,"(drop table)(.+)")){
            System.out.println("匹配正则表达式：drop table");
            bp = new DropTableSqlParser(sql);
        }else if (contains(sql,"(use user)(.+)")) {
            System.out.println("匹配正则表达式：use user XX");
            bp = new UseUserSqlParser(sql);
        } else if(contains(sql,"(use)(.+)")){
            System.out.println("匹配正则表达式：use");
            bp = new UseSqlParser(sql);
        } else if(contains(sql,"(insert into)([^\\(]+)(values)(.+)")){
            System.out.println("匹配正则表达式：insert into XX values()");
            bp = new InsertAllValuesSqlParser(sql);
        }else if(contains(sql,"(insert into)(.+)(values)(.+)")){
            System.out.println("匹配正则表达式：insert into XX () values ()");
            bp = new InsertPartSqlParser(sql);
        }  else if (contains(sql,"(select\\s*\\*)(.+)")) {
            if(contains(sql,"(select\\s*\\*)(.+)(where)(.+)")){
                System.out.println("匹配正则表达式：select * from XX where XX ;");
                bp = new SelectAllFromTableWhereSqlParser(sql);
            }else{
                System.out.println("匹配正则表达式：select * from XX");
                bp = new SelectAllFromTableSqlParser(sql);
            }
        } else if (contains(sql,"(select)(.+)(from)(.+)(where)(.+)")) {
            System.out.println("匹配正则表达式：select XX from XX where XX;");
            bp = new SelectDataFromTableWhereSqlParser(sql);
        } else if (contains(sql,"(select)(.+)(from)(.+)(;)")) {
            System.out.println("匹配正则表达式：select XX from XX");
            bp = new SelectDataFromTableSqlParser(sql);
        } else if (contains(sql,"(show tables)(.+)")) {
            System.out.println("匹配正则表达式：show tables");
            bp = new ShowTablesSqlParser(sql);
        } else if (contains(sql,"(show databases)(.+)")) {
            System.out.println("匹配正则表达式：show databases");
            bp = new ShowDataBasesSqlParser(sql);
        } else if (contains(sql,"(update table )(.+)(set)")) {
            System.out.println("调用updateData方法");
            bp = new UpdateDataFromTableSqlParser(sql);
        } else if (contains(sql,"(describe)(.+)")) {
            System.out.println("匹配正则表达式(describe)(.+)");
            bp = new ShowTableStructSqlParser(sql);
        } else if (contains(sql,"(alter table)(.+)(add column)(.+)")) {
            System.out.println("匹配正则表达式(alter table XX add column XX)");
            bp = new AlterTableAddColumnSqlParser(sql);
        } else if(contains(sql,"(alter table)(.+)(drop column)(.+)")){
            System.out.println("匹配正则表达式:alter table XX drop column XX");
            bp = new AlterTableDropColumnSqlParser(sql);
        } else if (contains(sql,"(delete from)(.+)")) {
            System.out.println("匹配正则表达式：delete from XX where ");
            bp = new deleteDataFromTable(sql);
        } else if (contains(sql,"(alter table)(.+)(modify)(.+)")) {
            System.out.println("匹配正则表达式 ： alter table XX modify column newType");
            bp = new AlterTableModifyColumnType(sql);
        } else{
            System.out.println("Sql语句错误。请重新输入");
            return null;
        }
        return bp.splitSqlSegment();
    }

    //sql为要解析的sql语句，regExp为一个正则表达式，判断sql是否包含regExp
    private static boolean contains(String sql , String regExp){
        Pattern pattern = Pattern.compile(regExp,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        return matcher.find();
    }
}
