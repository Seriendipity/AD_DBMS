package SqlFunction;

import java.io.*;

public class DropTable {
    public static void dropTable(String databaseName, String tableName) {
        File directory = new File("./MyDatabase/" + databaseName + "/" + tableName);
        if (directory.exists()) {
            if (directory.isDirectory()) {
                // 如果是文件夹，则依次删除文件夹内的内容
                try {
                    deleteDirectory(directory);
                    System.out.println("表格" + tableName + "删除成功！");
                } catch (IOException e) {
                    System.out.println("表格" + tableName + "删除失败：" + e.getMessage());
                }
            } else {
                // 如果不是文件夹直接删除
                boolean deleted = directory.delete();
                if (deleted) {
                    System.out.println("表格" + tableName + "删除成功！");
                } else {
                    System.out.println("表格" + tableName + "删除失败！");
                }
            }
        } else {
            System.out.println("表格" + tableName + "不存在！");
        }
    }

    private static void deleteDirectory(File dir) throws IOException {
        if (dir.isDirectory()) {
            for (File child : dir.listFiles()) {
                deleteDirectory(child);
            }
        }
        // 当文件夹为空时删除
        boolean success = dir.delete();
        if (!success) {
            throw new IOException("无法删除目录 " + dir.getName());
        }
    }
}
