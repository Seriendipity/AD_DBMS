package SqlFunction;

import java.io.File;

public class ShowTables {
    //显示数据库下的表格
    public static void ShowTables(String DatabaseName){
        File dir = new File("./MyDatabase/"+DatabaseName+"");
        if(dir.exists()){
        File[] files = dir.listFiles();
        //打印目前目录下的表
        for(int i = 0; i < files.length; i++) {
            System.out.println(files[i].getName());
            }
        }else{
            System.out.println("数据库"+DatabaseName+"不存在！");
        }
    }
}
