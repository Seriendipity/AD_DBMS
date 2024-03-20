package SqlFunction;
import java.io.*;
public class CreateDatabase {
    public CreateDatabase(String DatabaseName){
        try{
            //用户创建数据库目录
            File file = new File("./MyDatabase/"+DatabaseName+"");
            file.createNewFile();
            System.out.println("数据库"+DatabaseName+"创建完成！");
        }catch(IOException e){
            //打印异常信息
            e.printStackTrace();
        }
    }
}
