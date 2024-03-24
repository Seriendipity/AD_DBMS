package SqlFunction;

import java.io.File;

public class CheckDatabaseLegal {
    public static boolean checkDBisLegal(String databaseName){
        File dir = new File("./MyDatabase/"+databaseName+"");
        return dir.exists();
    }
}
