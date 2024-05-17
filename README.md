>  这是一个用Java实现的IDEA编写的DBMS管理系统，实现了B+树索引，UI界面交互、从UI中修改表中的信息、日志记录、批量解析sql命令行代码、正则表达式解析字符串匹配、xml文件存储数据等。
>  xml文件的配置需要使用到dom4j包，已放入lib文件夹，可自行配置到对应的IDE中。
简述使用流程：
1.java版本为java8以上
2.将dom4j配置到自己的IDE环境中
3.在maven项目中配置
     <dependencies>
            <!--dom4j resolve xml-->
            <dependency>
                <groupId>dom4j</groupId>
                <artifactId>dom4j</artifactId>
                <version>1.6.1</version>
            </dependency>
    
        <!--xpath library-->
        <dependency>
            <groupId>jaxen</groupId>
            <artifactId>jaxen</artifactId>
            <version>1.1.1</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/junit/junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
        </dependency>
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.12</version>
                <scope>compile</scope>
            </dependency>
    
        </dependencies>
4.Login.java为命令行执行对应的sql操作，DBMS_GUI.java为通过UI界面对数据库进行操作。

包说明：
--
> BPlusTree包：B+树创建索引的结构，包括节点、方法等实现。
> GUI包： 存放UI代码
> SqlFunction包：存放sql底层的函数以及登录、帮助（Sqlhelp）等
> SqlPar包：存放对于输入Sql语句解析的方法
> Utils包：实现连接的方法，包括从Sql转到对应的SqlParser以及将解析后的参数传递给SqlFunction的操作、

使用说明：
---
1.首先需要创建一个用户。我们的所有数据库、表、索引等文件都是存放在对应的用户下的，即每一个用户只能对他“所见”的Relation进行操作。
  如果使用命令行，那么需要首先运行 create user <YourUserName> , 然后按照提示输入用户名，密码
  如果使用UI，那么需要在首先登录，登录时会有选项，先选择注册用户，输入用户名密码后在登陆。
2.建好用户后，创建数据库，然后切换到该数据库中。
  如果使用命令行，执行 use database <YourDataBaseName>
  如果使用UI，直接点击连接已存在的数据库，输入数据库名称即可
3.其他所有操作详情请看SqlFunction中的SqlHelp.java进行查看
4.使用UI进行操作时，登录成功后，如果出现有组件重叠的情况，点击一下刷新按钮即可。如果发现全屏后未统一的情况，将左侧的界限拉动一下，即可。
