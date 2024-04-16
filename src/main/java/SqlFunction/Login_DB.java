package SqlFunction;

import Utils.SqlAnalysis;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Utils.ConnectSqlParser.connectSql;

public class Login_DB {
    public static void main(String[] args) throws IOException, DocumentException {
        System.out.println("-----------------------------------");
        System.out.println("|欢迎使用AD_DBMS来进行数据库操作        |");
        System.out.println("|您可以使用help;来进行本系统的相关用法查询 |");
        System.out.println("-----------------------------------");
        while(true){
            Scanner input = new Scanner(System.in);
            System.out.print(">>");
            String sql = input.nextLine();
            while(sql.lastIndexOf(";") != sql.length()-1){
                sql = sql + " " + input.nextLine();
            }
            sql = sql.trim();
            sql = sql.toLowerCase();
            sql = sql.replaceAll("\\s+"," ");
            sql = sql.substring(0,sql.lastIndexOf(";"));
            sql = " " + sql + ";";
            System.out.println(sql);

            List<List<String>> parameterList = new ArrayList<List<String>>();

            if(sql.equals(" quit;")){
                return;
            } else if (sql.equals(" help;")) {
                SqlHelp.Help();
            } else{
                parameterList = SqlAnalysis.generateParser(sql);
//                System.out.println("hello");
                connectSql(parameterList);
            }
        }

    }
}
