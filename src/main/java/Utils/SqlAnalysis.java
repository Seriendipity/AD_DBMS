package Utils;

import SqlParser.BaseParser;
import SqlParser.CreateDatabaseSqlParser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class SqlAnalysis {
    public static List<List<String>> generateParser(String sql){
        BaseParser bp = null;

        if(contains(sql,"(create database)(.+)")){
            System.out.println("匹配正则表达式：create database");
            bp = new CreateDatabaseSqlParser(sql);
        }else{
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
