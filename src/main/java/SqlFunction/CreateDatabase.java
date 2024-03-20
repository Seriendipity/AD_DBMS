package SqlFunction;
import java.io.*;
public class CreateDatabase {
    public CreateDatabase(String DatabaseName){
        //用户创建数据库目录
        File file = new File("./MyDatabase/"+DatabaseName+"");
        if(file.mkdir() == true) {
            System.out.println("数据库" + DatabaseName + "创建完成！");
        }else{
            System.out.println("数据库" + DatabaseName + "创建失败！");
        }
    }
}
