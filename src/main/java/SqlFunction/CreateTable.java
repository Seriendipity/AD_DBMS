package SqlFunction;
import java.io.*;
public class CreateTable {
    //只能创建表格，还需要加入列的信息
    public CreateTable(String DatabaseName,String TableName)throws IOException{
        //在databaseName下创建表格
        //先判断有没有数据库
        File dir = new File("./MyDatabase/"+DatabaseName+"");
        if(dir.exists()){
            File file = new File("./MyDatabase/"+DatabaseName+"/"+TableName+"");
            file.createNewFile();
            System.out.println(TableName+"创建成功！");
        }else{
            System.out.println("不存在该数据库！");
        }
    }

}
