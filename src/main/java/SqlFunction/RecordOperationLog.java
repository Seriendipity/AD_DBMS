package SqlFunction;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class RecordOperationLog {
    public static void recordLog(String originalSql) {
        File file = new File("./"+UseUser.userName+"/MyDatabase/LogFile.txt");
        if(!file.exists()){
            try {
                boolean success = file.createNewFile();
                if(success){
                    System.out.println("日志文件成功创建");
                }else{
                    System.out.println("日志文件创建失败");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile(),true))){
           //获取当前时间
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedTime = currentTime.format(formatter);
            writer.append(formattedTime);

            writer.append(originalSql);
            writer.newLine(); // 添加一个新行，以便于区分不同的日志条目
            writer.flush(); // 刷新输出流，确保内容被写入文件
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("向日志文件追加内容失败");
        }
    }

    public static void printLogFile() {
        String filePath = "./MyDatabase/LogFile.txt";
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 将文件的每一行输出到控制台
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("读取日志文件失败");
        }
    }
}
