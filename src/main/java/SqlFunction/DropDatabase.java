package SqlFunction;

import java.io.*;
import java.util.Scanner;

import static SqlFunction.DropTable.dropTable;

public class DropDatabase {
    public static void dropDatabase(String DatabaseName){
        boolean flag = false;
        File dir = new File("./MyDatabase/"+DatabaseName+"");
            //如果该目录不存在的话
        if(!dir.exists()){
            System.out.println("数据库"+DatabaseName+"不存在！");
        }else{
            //将该文件夹下的文件全部删除
            File[] files = dir.listFiles();
            if(files.length > 0){
                System.out.println("数据库中有表格，是否继续删除？（Y/N）");

                Scanner scanner = new Scanner(System.in);
                String answer = scanner.next();
                if(answer.toUpperCase().equals("Y")){
                    for(int i = 0; i < files.length; i++){
                        //按顺序删除文件
                       dropTable(DatabaseName,files[i].getName());
                    }
                    dir.delete();
                    System.out.println("数据库"+DatabaseName+"删除成功！");
                }else{
                    System.out.println("数据库"+DatabaseName+"未被删除！");
                }
            }else{
                //数据库为空，直接删除数据库
                dir.delete();
                System.out.println("数据库"+DatabaseName+"删除成功！");
            }
        }
    }
}
