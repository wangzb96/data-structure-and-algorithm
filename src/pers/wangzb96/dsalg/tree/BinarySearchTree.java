package pers.wangzb96.dsalg.tree;

import java.util.Comparator;

/**
 * 二叉搜索树
 * @param <E> 元素泛型
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月11日 14:52:51
 */
public class BinarySearchTree<E> extends BinaryTree<E>{
    private final Comparator<E> comparator;

    public BinarySearchTree(Comparator<E> comparator){
        this(null, comparator);
    }
    public BinarySearchTree(Node.Creator<E> nodeCreator, Comparator<E> comparator){
        super(nodeCreator);
        this.comparator = comparator;
    }

    protected Comparator<E> getComparator(){
        return comparator;
    }

    protected Node<E> search(E elem){
        if(elem==null) return null;
        var parent = getVRoot();
        var which = LEFT;
        Node<E> node;
        while((node = parent.getChild(which))!=null){
            var tmp = getComparator().compare(elem, node.getElem());
            if(tmp==0) return node;
            else{
                parent = node;
                which = tmp<0? LEFT: RIGHT;
            }
        }
        return getNodeCreator().newNode(null, parent, which);
    }

    /**
     * 判断元素是否存在
     * @param elem 要判断的元素（外部的）
     * @return 存在：true；不存在：false
     */
    public boolean has(E elem){
        if(elem==null) return false;
        var node = search(elem);
        return node.getElem()!=null;
    }

    /**
     * 获取元素
     * @param elem 要获取的元素（外部的）
     * @return 存在：获取的元素（内部的）；不存在：null
     */
    public E get(E elem){
        if(elem==null) return null;
        var node = search(elem);
        return node.getElem();
    }

    /**
     * 设置元素
     * @param elem 要设置的元素（外部的）
     * @param newElem 新元素（外部的）
     * @return if elem&newElem==null: null
     *         elif elem==null: insert(newElem)
     *         elif newElem==null: remove(elem)
     *         elif elem==newElem: update(elem, newElem)
     *         else: remove(elem), insert(newElem)
     */
    public E set(E elem, E newElem){
        var node = search(elem);
        if(node==null || node.getElem()==null) return insert(newElem);
        if(newElem==null) return remove(node).getElem();
        var tmp = getComparator().compare(newElem, elem);
        if(tmp==0){
            var oldElem = node.getElem();
            node.setElem(newElem);
            return oldElem;
        }
        node = remove(node);
        insert(newElem);
        return node.getElem();
    }

    /**
     * 插入元素
     * @param elem 要插入的元素（外部的）
     * @return 不存在：null；存在：已存在的元素（内部的）
     */
    public E insert(E elem){
        if(elem==null) return null;
        var node = search(elem);
        if(node.getElem()==null){
            node.setElem(elem);
            insert(node);
            return null;
        }
        return node.getElem();
    }
    protected void insert(Node<E> node){
        node.linkParent();
    }

    /**
     * 移除元素
     * @param elem 要移除的元素（外部的）
     * @return 存在：移除的元素（内部的）；不存在：null
     */
    public E remove(E elem){
        if(elem==null) return null;
        var node = search(elem);
        if(node.getElem()!=null) node = remove(node);
        return node.getElem();
    }
    protected Node<E> remove(Node<E> node){
        var num = node.numChildren();

        // node有两个孩子
        if(num==2){
            var next = node.getNear(RIGHT);
            node.swapElem(next);
            node = next;
            num = node.numChildren();
        }

        // node有一个孩子
        if(num==1){
            var child = node.firstChildNotNull();
            node.swapElem(child);
            node = child;
        }

        // node没有孩子
        return removeLeaf(node);
    }
    protected Node<E> removeLeaf(Node<E> node){
        node.unlinkParent();
        return node;
    }
}
