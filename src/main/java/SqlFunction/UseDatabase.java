package SqlFunction;

import java.io.File;

public class UseDatabase {
    public static String databaseName = "";
    public static void setDatabaseName(String databaseName1){
        File file = new File("./"+UseUser.userName+"/MyDatabase/"+databaseName1+"");
        if(file.exists()){
            databaseName = databaseName1;
        }
    }
}
