package SqlFunction;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.util.*;
import java.io.File;
import java.io.*;

public class CreateUser {
    public static void createUser() throws DocumentException, IOException {
        //打开用户的表格，获取所有的用户节点
        File file = new File("./MyDatabase/user/user.xml");
        SAXReader saxReader = new SAXReader();
        //获取XML文件的管理
        Document document = saxReader.read(file);
        //获取user的根节点
        List<Node> list = document.getRootElement().selectNodes("user");

        //获取用户的输入
        Scanner scanner = new Scanner(System.in);
        String userName,password;
        while(true){
            System.out.println("请输入您的用户名");
            userName = scanner.nextLine();
            boolean isLegal;
            isLegal = true;
            for(Node node: list){
                Element element = (Element) node;
                Attribute name = element.attribute("name");
                if(name.getText().equals(userName)){
                    System.out.println("用户名已存在请重新输入");
                    isLegal = false;
                    break;
                }
            }
            if(isLegal){
                break;
            }
        }
        System.out.println("请输入密码:");
        password = scanner.nextLine();

        document.getRootElement().addElement("user").addAttribute("id",list.size()+1+"").addAttribute("name",userName).addAttribute("userKey",password);
        CreateTable.writeIO(file,document);
        System.out.println("新用户"+userName+"创建成功");
    }
}
