package SqlFunction;

import java.io.*;

public class DropTable {
    public static void dropTable(String DatabaseName,String TableName){
        boolean flag = false;
        File file = new File("./MyDatabase/"+DatabaseName+TableName+"");
        if(file.exists()){
            flag = file.delete();
            if(!flag){
                System.out.println("表格"+TableName+"删除失败！");
            }else{
                System.out.println("表格"+TableName+"删除成功！");
            }
        }else{
            System.out.println("表格"+TableName+"不存在！");
        }
    }
}
