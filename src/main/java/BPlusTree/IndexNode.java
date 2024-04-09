package BPlusTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndexNode extends Node {

    //存储子节点的链表:n
    protected ArrayList<Node> children; //n+1 children

    public IndexNode(int key,Node child0,Node child1){
        isLeafNode = false;
        keys = new ArrayList<Integer>();
        keys.add(key);
        children = new ArrayList<Node>();
        children.add(child0);
        children.add(child1);
    }

    public IndexNode(List<Integer> newKeys,List<Node> newChildren){
        isLeafNode = false;
        keys = new ArrayList<Integer>(newKeys);
        children = new ArrayList<Node>(newChildren);
    }

    //插入到非叶子节点的指定索引处
    public void insertSorted(Map.Entry<Integer,Node> e, int index){
        int key = e.getKey();
        Node child = e.getValue();
        if(index >= keys.size()){
            keys.add(key);
            children.add(child);
        }else{
            keys.add(index,key);
            children.add(index + 1,child);
        }
    }
}
