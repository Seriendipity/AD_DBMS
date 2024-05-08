package SqlFunction;

public class SqlHelp {
    public static void Help(){
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("下面是本系统的使用说明(大小写不敏感)：");
        System.out.println("(1)Create语句");
        System.out.println("--Create database yourDatabaseName;");
        System.out.println("--Create User yourUserName;");
        System.out.println("--Create index on yourTableName (ColumnName [DESC|ASC]);");
        System.out.println("--Create index YourIndexName on yourTableName (ColumnName [DESC|ASC]);");
        System.out.println("--Create table yourTableName (ColumnName ColumnType constriction,[...]);");
        System.out.println("(2)Drop语句");
        System.out.println("--Drop database yourDatabaseName; ");
        System.out.println("--Drop table yourTableName;");
        //System.out.println("---");
        System.out.println("(3)Show语句");
        System.out.println("--Show databases MyDatabases;(Tips:databases)");
        System.out.println("--Show tables yourDatabaseName;(Tips:tables)");
        System.out.println("---describe yourTableName;");
        System.out.println("(4)Insert into语句");
        System.out.println("--Insert into yourTableName (ColumnName,[...]) values (ColumnValue,[...]),[(...)];");
        System.out.println("(5)Update语句");
        System.out.println("--Update table yourTableName set Column=ChangeValue where Column=OriginalValue---(Tip:set和where的等号前后不要有空格)");
        System.out.println("(6)Select语句");
        System.out.println("--Select * from yourTableName;");
        System.out.println("--Select * from yourTableName where yourCondition");
        System.out.println("--Select columnName from yourTableName");
        System.out.println("--Select columnName from yourTableName where yourCondition");
        System.out.println("(7)Alter语句");
        System.out.println("--Alter table yourTableName add column yourColumnName ColumnType");
        System.out.println("--Alter table yourTableName drop column yourColumnName");
        System.out.println("(8)查看日志文件");
        System.out.println("--watch LogFile");
        System.out.println("(9)Use语句(以下两个语句需要先执行)");
        System.out.println("--use user YourUserName;");
        System.out.println("--use YourDatabaseName;");
        System.out.println("----------------------------------------------------------------------------------------");
    }
}
