package SqlFunction;
import java.io.*;
public class CreateDatabase {
    public static void createDatabase(String DatabaseName){
        //用户创建数据库目录
        File dir = new File("./MyDatabase/"+DatabaseName+"");
        if(!dir.exists()) {
            dir.mkdir();//创建目录
            System.out.println("数据库" + DatabaseName + "创建完成！");
        }else{
            System.out.println("数据库" + DatabaseName + "已经存在！");
        }
    }
}
