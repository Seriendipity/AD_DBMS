package SqlFunction;

import BPlusTree.BPlusTree;

import java.util.*;

public class CreateIndex {
    //B+树的地方
    static public Map<String, BPlusTree> bMap = new HashMap<String,BPlusTree>();

    public static void createIndex(String DatabaseName,String TableName,String IndexName){
        //判断数据库是否为空
        if(Judge.isDatabaseEmpty()){
            System.out.println("数据库为空");
            return;
        }
        //判断是否有表格


    }
}
