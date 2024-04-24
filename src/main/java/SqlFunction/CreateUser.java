package SqlFunction;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.util.*;
import java.io.File;
import java.io.*;

public class CreateUser {
    public static void createUser(String userName) throws DocumentException, IOException {
        //获取用户的输入
        Scanner scanner = new Scanner(System.in);
        String password = null;
        while(true){
            boolean isLegal;
            isLegal = true;
                if(Judge.findUser(userName)){
                    System.out.println("用户名已存在请重新输入");
                    isLegal = false;
                }
            if(isLegal){
                System.out.println("请输入密码:");
                password = scanner.nextLine();
                break;
            }
            System.out.println("请输入要创建的用户名");
            userName = scanner.nextLine();
        }
        
        File dkmir =new File ("./"+userName+"");
        dkmir.mkdir();
        File file = new File("./"+userName+"/user.xml");

        Document document = DocumentHelper.createDocument();
        Element rootElem = document.addElement(userName+"i");
        rootElem.addElement("user").addAttribute("name",userName).addAttribute("userKey",password);
        CreateTable.writeIO(file,document);
        System.out.println("新用户"+userName+"创建成功");
    }
}
