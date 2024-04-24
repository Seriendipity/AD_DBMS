package SqlFunction;

import org.dom4j.DocumentException;

import java.util.Scanner;

public class UseUser {
    public static String userName = "";
    public static void setUserName(String user) throws DocumentException {
        Scanner scanner = new Scanner(System.in);
        if(Judge.findUser(user)){
            System.out.println("请输入密码：");
            String key = scanner.nextLine();
            if(Judge.isUser(user,key))
            userName = user;
        }
    }
}
