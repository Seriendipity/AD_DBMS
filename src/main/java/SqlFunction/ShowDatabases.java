package SqlFunction;

import java.io.File;

public class ShowDatabases {
    public ShowDatabases(){
        //获取MyDatabase目录
        File dir = new File("./MyDatabase/");
        //得到MyDatabases下的文件夹
        File[] dirs = dir.listFiles();
        for(int i = 0; i < dirs.length; i++){
            //打印名称
            System.out.println(dirs[i].getName());
        }
    }
}
