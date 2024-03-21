package SqlFunction;

import java.io.*;

public class DropDatabase {
    public DropDatabase(String DatabaseName){
        boolean flag = false;
        File dir = new File("./MyDatabase/"+DatabaseName+"");
            //如果该目录不存在的话
        if(!dir.exists()){
            System.out.println("数据库"+DatabaseName+"不存在！");
        }else{
            //将该文件夹下的文件全部删除
            File[] files = dir.listFiles();
            for(int i = 0; i < files.length; i++){
                //按顺序删除文件
                flag = files[i].delete();
                //如果删除失败
                if(!flag) break;
            }
            dir.delete();
            System.out.println("数据库"+DatabaseName+"删除成功！");
        }
    }
}
