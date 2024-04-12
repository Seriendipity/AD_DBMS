package BPlusTree;

import java.util.List;

public class Main {

    public static BPlusTree createBPlusTree(List<List<String>> list){
        BPlusTree newTree = new BPlusTree();

        //将主键值和文件名插入到B+树的节点
        for(int i = 0; i < list.size();i++){
            int key = Integer.parseInt(list.get(i).get(0));
            String value = list.get(i).get(1);
            newTree.insert(key,value);
        }
        //Utils.printTree(newTree)
        return newTree;
    }

    public static BPlusTree loadIndex(List<List<String>> list){
        BPlusTree tree = new BPlusTree();

        for(int i = 0; i < list.size(); i++){
            int key = Integer.parseInt(list.get(i).get(0).substring(1,list.get(i).get(0).length()));
            String value = list.get(i).get(1);
            tree.insert(key,value);
        }
        return tree;
    }
}
