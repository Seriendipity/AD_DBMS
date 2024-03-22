package SqlFunction;

import Utils.SqlAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Login_DB {


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String sql = input.nextLine();
        while(sql.lastIndexOf(";") != sql.length()-1){
            sql = sql + " " + input.nextLine();
        }
        sql = sql.trim();
        sql = sql.toLowerCase();
        sql = sql.replaceAll("\\s+"," ");
        sql = sql.substring(0,sql.lastIndexOf(";"));
        sql = " " + sql + "END";
        System.out.println(sql);

        List<List<String>> parameterList = new ArrayList<List<String>>();

        if(sql.equals("quit END")){
            return;
        }else{
            parameterList = SqlAnalysis.generateParser(sql);
        }
    }
}
