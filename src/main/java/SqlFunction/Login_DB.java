package SqlFunction;

import Utils.SqlAnalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Utils.ConnectSqlParser.connectSql;

public class Login_DB {


    public static void main(String[] args) throws IOException {
        while(true){
            Scanner input = new Scanner(System.in);
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
            }else{
                parameterList = SqlAnalysis.generateParser(sql);
                connectSql(parameterList);
            }
        }

    }
}
