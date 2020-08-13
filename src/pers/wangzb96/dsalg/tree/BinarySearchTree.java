package pers.wangzb96.dsalg.tree;

import pers.wangzb96.util.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * 二叉搜索树
 * @param <E> 元素泛型
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月8日 12:00:00
 */
public class BinarySearchTree<E> implements Tree<E>{
    public static final boolean NULL = false;
    public static final boolean LEFT = false;
    public static final boolean RIGHT = true;

    public static boolean another(boolean which){
        return !which;
    }

    public static class Node<E> implements Tree.Node<E>{
        public static class Creator<E> implements Tree.Node.Creator<E>{
            public Node<E> newNode(E elem, Node<E> parent, boolean which){
                return new Node<>(elem, parent, which);
            }
        }

        private E elem;
        private Node<E> parent;
        private boolean which;
        private Node<E> left = null;
        private Node<E> right = null;

        public Node(E elem, Node<E> parent, boolean which){
            this.elem = elem;
            this.parent = parent;
            this.which = which;
        }

        public E getElem(){
            return elem;
        }
        public void setElem(E elem){
            this.elem = elem;
        }

        public void swapElem(Node<E> node){
            var elem = getElem();
            setElem(node.getElem());
            node.setElem(elem);
        }

        public Node<E> getParent(){
            return parent;
        }
        public boolean getWhich(){
            return which;
        }
        public void setParentWhich(Node<E> parent, boolean which){
            this.parent = parent;
            this.which = which;
        }

        public Node<E> getLeft(){
            return left;
        }
        public void setLeft(Node<E> left){
            this.left = left;
        }

        public Node<E> getRight(){
            return right;
        }
        public void setRight(Node<E> right){
            this.right = right;
        }

        public Node<E> getChild(boolean which){
            if(which==LEFT) return getLeft();
            return getRight();
        }
        public void setChild(boolean which, Node<E> child){
            if(which==LEFT) setLeft(child);
            else setRight(child);
        }

        public int numChildren(){
            var num = 0;
            if(getLeft()!=null) ++num;
            if(getRight()!=null) ++num;
            return num;
        }

        public Node<E> firstChildNotNull(){
            Node<E> child;
            if((child=getLeft())!=null) return child;
            if((child=getRight())!=null) return child;
            return null;
        }

        public Node<E> getNear(boolean which){
            var near = getChild(which);
            if(near!=null) near = near.getFar(another(which));
            return near;
        }
        public Node<E> getFar(boolean which){
            var far = this;
            Node<E> child;
            if(which==LEFT)
                while((child=far.getLeft())!=null) far=child;
            else
                while((child=far.getRight())!=null) far=child;
            return far;
        }

        public void linkParent(){
            var parent = getParent();
            if(parent!=null) parent.setChild(getWhich(), this);
        }
        public void unlinkParent(){
            var parent = getParent();
            if(parent!=null) parent.setChild(getWhich(), null);
        }

        public Node<E> getBrother(){
            Node<E> brother = null;
            var parent = getParent();
            if(parent!=null) brother = parent.getChild(another(getWhich()));
            return brother;
        }

        public void rotate(boolean which){
            var child = getChild(another(which));
            if(child==null) return;
            var parent = getParent();
            var grandChild = child.getChild(which);

            if(parent!=null)
                parent.setChild(getWhich(), child);
            child.setParentWhich(parent, getWhich());

            child.setChild(which, this);
            setParentWhich(child, which);

            setChild(another(which), grandChild);
            if(grandChild!=null)
                grandChild.setParentWhich(this, another(which));
        }

        @Override
        public String toString(){
            return Util.toString(getElem());
        }
    }

    private final Node.Creator<E> nodeCreator;
    private final Comparator<E> comparator;
    private final Node<E> vroot;

    public BinarySearchTree(){
        this(null, null);
    }
    public BinarySearchTree(Node.Creator<E> nodeCreator){
        this(nodeCreator, null);
    }
    public BinarySearchTree(Comparator<E> comparator){
        this(null, comparator);
    }
    @SuppressWarnings("unchecked")
    public BinarySearchTree(Node.Creator<E> nodeCreator, Comparator<E> comparator){
        if(nodeCreator==null) nodeCreator = new Node.Creator<>();
        if(comparator==null) comparator = (x, y) -> ((Comparable<E>)x).compareTo(y);
        this.nodeCreator = nodeCreator;
        this.comparator = comparator;
        vroot = nodeCreator.newNode(null, null, NULL);
    }

    protected Node.Creator<E> getNodeCreator(){
        return nodeCreator;
    }

    protected Comparator<E> getComparator(){
        return comparator;
    }

    protected Node<E> getVRoot(){
        return vroot;
    }

    protected Node<E> getRoot(){
        return getVRoot().getLeft();
    }

    protected boolean isRoot(Node<E> node){
        return node==getRoot();
    }

    protected Node<E> search(E elem){
        if(elem==null) return null;
        var parent = getVRoot();
        var which = LEFT;
        var node = getRoot();
        while(node!=null){
            var tmp = getComparator().compare(elem, node.getElem());
            if(tmp==0) return node;
            else if(tmp<0){
                parent = node;
                which = LEFT;
                node = node.getLeft();
            }
            else{
                parent = node;
                which = RIGHT;
                node = node.getRight();
            }
        }
        return getNodeCreator().newNode(null, parent, which);
    }

    /**
     * 判断树非空
     * @return 非空：true；空：false
     */
    public boolean isNotEmpty(){
        return getRoot()!=null;
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
        var tmp = getComparator().compare(newElem, node.getElem());
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


    /**
     * 前序遍历
     * @return 前序遍历的元素序列
     */
    public ArrayList<E> preOrder(){
        var ret = new ArrayList<E>();
        var sk = new LinkedList<Node<E>>();
        sk.offerLast(getRoot());
        while(!sk.isEmpty()){
            var node = sk.pollLast();
            if(node==null) continue;
            ret.add(node.getElem());
            sk.offerLast(node.getRight());
            sk.offerLast(node.getLeft());
        }
        return ret;
    }
    /**
     * 中序遍历
     * @return 中序遍历的元素序列
     */
    public ArrayList<E> inOrder(){
        var ret = new ArrayList<E>();
        var sk = new LinkedList<Node<E>>();
        sk.offerLast(getRoot());
        while(!sk.isEmpty()){
            var node = sk.peekLast();
            if(node!=null){
                sk.offerLast(node.getLeft());
                continue;
            }
            sk.pollLast();
            if(sk.isEmpty()) break;
            node = sk.pollLast();
            ret.add(node.getElem());
            sk.offerLast(node.getRight());
        }
        return ret;
    }
    /**
     * 后序遍历
     * @return 后序遍历的元素序列
     */
    public ArrayList<E> postOrder(){
        var ret = new ArrayList<E>();
        var sk = new LinkedList<Node<E>>();
        var sk1 = new LinkedList<E>();
        sk.offerLast(getRoot());
        while(!sk.isEmpty()){
            var node = sk.pollLast();
            if(node==null) continue;
            sk1.offerLast(node.getElem());
            sk.offerLast(node.getLeft());
            sk.offerLast(node.getRight());
        }
        while(!sk1.isEmpty()) ret.add(sk1.pollLast());
        return ret;
    }

    @Override
    public String toString(){
        class Elem{
            public final Node<E> node;
            public final int num;

            public Elem(Node<E> node, int num){
                this.node = node;
                this.num = num;
            }
        }

        var sb = new StringBuilder();
        var sk = new LinkedList<Elem>();
        sk.offerLast(new Elem(getRoot(), 0));
        var bool = false;
        while(!sk.isEmpty()){
            var elem = sk.pollLast();
            var node = elem.node;
            var num = elem.num;
            if(bool) sb.append("\n");
            else bool = true;
            sb.append(" ".repeat(num*4));
            sb.append("-");
            if(node==null) continue;
            sb.append(" %s".formatted(node));
            if(node.numChildren()<=0) continue;
            ++num;
            sk.offerLast(new Elem(node.getRight(), num));
            sk.offerLast(new Elem(node.getLeft(), num));
        }
        return sb.toString();
    }
}
