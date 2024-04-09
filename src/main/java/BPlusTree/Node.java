package BPlusTree;

import java.util.ArrayList;

public class Node {

    protected boolean  isLeafNode;

    protected ArrayList<Integer> keys;

    public boolean isOverflowed(){
        return keys.size() > 2 * BPlusTree.D;
    }

    public boolean isUnderflowed(){
        return keys.size() < 2 * BPlusTree.D;
    }

}
