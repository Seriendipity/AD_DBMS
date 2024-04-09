package BPlusTree;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map.Entry;

/**
 * 对BPlusTree类假设：
 * 1.没有重复的键值被插入
 * 2.阶数D<=一个节点中的Key数量 <= 20
 * 3.所有key均为非负数
 */
public class BPlusTree {
    public Node root;
    public static final int D = 2;

    /**
     *
     * @param key
     * @return value
     */
    public String search(int key){

        //查找包含key的叶子节点
        LeafNode leaf = findLeafNodeWithKey(root,key);

        //查找key对应的value并返回
        for(int i = 0; i < leaf.keys.size(); i++){
            if(leaf.keys.get(i) == key){
                return leaf.values.get(i);
            }
        }
        return null;
    }
    //更新特定节点的值
    public void updateIndexNodeKeyWithKey(Node node, int searchKey,int newKey){
        if(node == null){
            return;
        }
        if(node.isLeafNode){
            return;
        }
        IndexNode indexNode = (IndexNode) node;
        for(int i = 0; i < node.keys.size();i++){
            //找到索引节点
            if(indexNode.keys.get(i) > searchKey){
                break;
            }
            if(indexNode.keys.get(i) == searchKey){
                indexNode.keys.set(i,newKey);
                return;
            }
        }
        //没找到索引节点，可能其他子节点
        if(searchKey < indexNode.keys.get(0)){
            updateIndexNodeKeyWithKey(indexNode.children.get(0),searchKey,newKey);
        }else if(searchKey > indexNode.keys.get(indexNode.keys.size()-1)){
            updateIndexNodeKeyWithKey(indexNode.children.get(indexNode.children.size()-1),searchKey,newKey);
        }else{
            for(int i = 0; i < node.keys.size(); i++){
                if(indexNode.keys.get(i) > searchKey){
                    updateIndexNodeKeyWithKey(indexNode.children.get(i),searchKey,newKey);
                }
            }
        }
    }
    //索引更新：更新叶子节点
    public void update(int key,String value){
        LeafNode leafNode = findLeafNodeWithKey(root,key);
        ArrayList<Integer> keys = leafNode.keys;
        ArrayList<String> values = leafNode.values;
        ListIterator<Integer> iterator = keys.listIterator();
        while(iterator.hasNext()){
            if(iterator.next() == key){
                int position = iterator.previousIndex();
                values.set(position,value);
                break;
            }
        }
    }
    //找到要插入的关键点的LeafNode
    private LeafNode findLeafNodeWithKey(Node node, int key){
        if(node == null){
            return null;
        }
        if(node.isLeafNode){
            return (LeafNode)node;
        }
        else{
            //是indexNode
            IndexNode indexNode = (IndexNode)node;
            if(key < node.keys.get(0)){
                return findLeafNodeWithKey(indexNode.children.get(0),key);
            }else if(key >= node.keys.get(node.keys.size()-1)){
                return findLeafNodeWithKey(indexNode.children.get(indexNode.children.size()-1),key);
            }else{//如果值在第一个和最后一个之间
                ListIterator<Integer> iterator = indexNode.keys.listIterator();
                while(iterator.hasNext()){
                    if(iterator.next()>key){
                        return findLeafNodeWithKey(indexNode.children.get(iterator.previousIndex()),key);
                    }
                }
            }
        }
        return null;
    }
    //TODO 插入一堆键值
    public void insert(int key, String value){
        //初始化插入的树
        if(root == null){
            root = new LeafNode(key,value);
        }

        Entry<Integer,Node> overflowed = insertHelper(root,key,value);
        if(overflowed != null){
            //root层发生溢出
            root = new IndexNode(overflowed.getKey(),root,overflowed.getValue());
        }

    }
    //TODO 插入方法帮助
    private Entry<Integer,Node> insertHelper(Node node, int key, String value){
        Entry<Integer,Node> overflow = null;
        if(node.isLeafNode){
            LeafNode leaf = (LeafNode) node;
            leaf.insertSorted(key,value);
            if(leaf.isOverflowed()){
                Entry<Integer,Node> rightSplit = splitLeafNode(leaf);
                return rightSplit;
            }
            return null;
        }else{
            IndexNode idNode = (IndexNode) node;
            if(key < node.keys.get(0)){
                overflow = insertHelper(idNode.children.get(0),key,value);
            }else if(key >= node.keys.get(idNode.keys.size()-1)){
                overflow = insertHelper(idNode.children.get(idNode.children.size()-1),key,value);
            }else{
                //在其中之一增加
                for(int i = 0; i < idNode.children.size(); i++){
                    if(idNode.keys.get(i) > key){
                        overflow = insertHelper(idNode.children.get(i),key,value);
                        break;
                    }
                }
            }
        }
        if(overflow != null){
            IndexNode idNode = (IndexNode) node;

            //之处插入溢出索引的位置
            int splittingKey = overflow.getKey();
            int indexAtParent = idNode.keys.size();
            if(splittingKey < idNode.keys.get(0)){
                indexAtParent = 0;
            }else if(splittingKey > idNode.keys.get(idNode.keys.size()-1)){
                indexAtParent = idNode.children.size();
            }else{
                for(int i = 0; i < idNode.keys.size(); i++){
                    if(i < idNode.keys.get(i)){
                        indexAtParent = i;
                    }
                }
            }
            return null;
        }
        return overflow;
    }
    //TODO 重新排列leftLeaf的nextLeaf指针的指针
    private void manageSiblingPtrs(LeafNode leftLeaf, LeafNode rightLeaf){
        if (leftLeaf.nextLeaf != null){
            rightLeaf.nextLeaf = leftLeaf.nextLeaf;
        }
        leftLeaf.nextLeaf = rightLeaf;
    }
    //TODO 拆分叶子节点并返回一个新的右节点和拆分
    private Entry<Integer,Node> splitLeafNode(LeafNode leaf){
        int RIGHT_BUCKET_SIZE = D+1;

        ArrayList<Integer> rightKeys = new ArrayList<Integer>(RIGHT_BUCKET_SIZE);
        ArrayList<String> rightValues = new ArrayList<String>(RIGHT_BUCKET_SIZE);

        rightKeys.addAll(leaf.keys.subList(D,leaf.keys.size()));
        rightValues.addAll(leaf.values.subList(D,leaf.values.size()));

        //从左删除右侧
        leaf.keys.subList(D,leaf.keys.size()).clear();
        leaf.values.subList(D,leaf.values.size()).clear();

        LeafNode rightLeaf = new LeafNode(rightKeys,rightValues);

        //管理新的兄弟节点
        manageSiblingPtrs(leaf,rightLeaf);

        return new AbstractMap.SimpleEntry<Integer, Node>(rightLeaf.keys.get(0),rightLeaf);

    }
    //拆分indexNode并返回新的右节点和拆分
    public Entry<Integer,Node> splitIndexNode(IndexNode index){
        int BUCKET_SIZE = D;
        ArrayList<Integer> rightKeys = new ArrayList<Integer>(BUCKET_SIZE);
        ArrayList<Node> rightChildren = new ArrayList<Node>(BUCKET_SIZE + 1);

        rightKeys.addAll(index.keys.subList(D+1,index.keys.size()));
        rightChildren.addAll(index.children.subList(D+1,index.children.size()));

        //加上新节点
        IndexNode rightNode = new IndexNode(rightKeys,rightChildren);
        AbstractMap.SimpleEntry<Integer,Node> splitted = new AbstractMap.SimpleEntry<Integer, Node>(index.keys.get(D),rightNode);

        //从右往左删除
        index.keys.subList(D,index.keys.size()).clear();
        index.children.subList(D+1,index.children.size()).clear();

        return splitted;
    }
    //TODO 从B+Tree删除某一个键值对
    public void delete(int key){
        int index = deleteHelper(null,root,key);
        if(index != -1){
            root.keys.remove(index);
            if(root.keys.size() == 0){
                root = ((IndexNode) root).children.get(0);
            }
        }
        if(root.keys.size() == 0){
            root = null;
        }
    }
    //TODO 删除方法帮助
    private int deleteHelper(IndexNode parent, Node node, int key){
        int indexToDelete = -1;

        //在父节点中找Node的索引
        int indexInParent = -1;
        if(parent != null){
            for(indexInParent = 0; indexInParent < parent.children.size(); indexInParent++){
                if(parent.children.get(indexInParent) == node){
                    break;
                }
            }
        }
        if(node.isLeafNode){
            LeafNode leafNode = (LeafNode) node;
            for(int i = 0; i < leafNode.keys.size();i++){
                if(leafNode.keys.get(i) == key){
                    //从叶节点的keys中删除
                    leafNode.keys.remove(i);
                    //删除对应的value
                    leafNode.values.remove(i);
                    break;
                }
            }
            //检查下溢
            if(leafNode.isUnderflowed() && leafNode != root){
                //找到驻留在父节点中的索引叶节点
                if(indexInParent - 1 >= 0){
                    //节点有左孩子
                    LeafNode left = (LeafNode) parent.children.get(indexInParent - 1);
                    return handleLeafNodeUnderflow(left, leafNode, parent);
                }else{
                    //节点没有左孩子
                    LeafNode right = (LeafNode) parent.children.get(indexInParent + 1);
                    return handleLeafNodeUnderflow(leafNode, right, parent);
                }
            }else{
                //如果删除拆分key，可能需要更新父节点/祖先节点
                if(leafNode.keys.size() > 0){
                    updateIndexNodeKeyWithKey(root,key,leafNode.keys.get(0));
                }
                return -1;
            }
        }
        else{
            //节点是索引节点
            IndexNode idNode = (IndexNode) node;
            if(key < idNode.keys.get(0)){
                //找到第一个子节点
                indexToDelete = deleteHelper(idNode,idNode.children.get(0),key);
            }else if(key >= idNode.keys.get(idNode.keys.size()-1)){
                //找到最后一个子节点
                indexToDelete = deleteHelper(idNode,idNode.children.get(idNode.children.size()-1),key);
            }else{
                //找到中间的子节点
                for(int i = 0; i < idNode.keys.size(); i++){
                    if(idNode.keys.get(i) > key){
                        indexToDelete = deleteHelper(idNode,idNode.children.get(i),key);
                    }
                }
            }
        }
        //是否有要删除的索引存在
        if(indexToDelete != -1){
            if(node == root){
                return indexToDelete;
            }
            node.keys.remove(indexToDelete);

            //删除造成下滥
            if(node.isUnderflowed()){
                //如果有左兄弟
                IndexNode left = (IndexNode) node;
                IndexNode right = (IndexNode) node;

                //检查索引节点是否有兄弟
                if(indexInParent - 1 >= 0){
                    left = (IndexNode) parent.children.get(indexInParent - 1);
                }else{
                    right = (IndexNode) parent.children.get(indexInParent + 1);
                }
                return handleIndexNodeUnderFlow(left,right,parent);
            }
        }
        return -1;
    }
    //TODO 处理LeafNode下溢出
    public int handleLeafNodeUnderflow(LeafNode left, LeafNode right,IndexNode parent){
        //合并
        if(left.keys.size() + right.keys.size() < 2*D){
            left.keys.addAll(right.keys);
            left.values.addAll(right.values);
            left.nextLeaf = right.nextLeaf;

            //删除其他节点
            int indexInParent = parent.children.indexOf(right);
            parent.children.remove(indexInParent);
            return indexInParent -1;
        }
        //重新分离
        int childsIndexInParent;
        if(left.isUnderflowed()){
            childsIndexInParent = parent.children.indexOf(right);
            //得到最小的右节点值
            left.insertSorted(right.keys.remove(0),right.values.remove(0));
        }else{
            childsIndexInParent = parent.children.indexOf(right);
            //得到最小的左节点值
            right.insertSorted(left.keys.remove(left.keys.size()-1),left.values.remove(left.values.size()-1));
            parent.keys.set(childsIndexInParent - 1, parent.children.get(childsIndexInParent).keys.get(0));
        }
        //更新双亲节点的下标值
        parent.keys.set(childsIndexInParent - 1, parent.children.get(childsIndexInParent).keys.get(0));

        return -1;
    }
    //处理indexNode下溢出
    public int handleIndexNodeUnderFlow(IndexNode leftIndex, IndexNode rightIndex, IndexNode parent){
        //独立值
        int separatKey;
        int index;

        //找到从双亲分割的值
        for(index = 0; index < parent.keys.size();index++){
            if(parent.children.get(index) == leftIndex && parent.children.get(index+1) == rightIndex){
                break;
            }
        }
        separatKey = parent.keys.get(index);

        //合并
        if (leftIndex.keys.size()+ rightIndex.keys.size() < 2*D){
            //将独立值往下移 B树操作
            leftIndex.keys.add(separatKey);
            leftIndex.keys.addAll(rightIndex.keys);

            leftIndex.children.addAll(rightIndex.children);

            //删除右边
            parent.children.remove(parent.children.indexOf(rightIndex));
            return index;
        }
        //右的节点下端溢出
        if(leftIndex.isUnderflowed()){
            //将独立值移动到左节点的下面
            leftIndex.keys.add(separatKey);
            //将右节点的值提上来作为双亲节点的值
            parent.keys.set(index,rightIndex.keys.remove(0));
            //左边的子节点加上原先的右节点的子节点
            leftIndex.children.add(rightIndex.children.remove(0));
            //做了一个B+树平衡点的移动
        }else if(rightIndex.isUnderflowed()){
            //将独立值移动到右节点的下面
            rightIndex.keys.add(0,separatKey);
            //将右节点的值提上来作为双亲结点的值
            parent.keys.set(0,leftIndex.keys.remove(0));
            //右边的子节点加上原先左边节点的子节点
            rightIndex.children.add(parent.keys.size()-1,leftIndex.children.remove(0));
        }
        return -1;
    }

}
