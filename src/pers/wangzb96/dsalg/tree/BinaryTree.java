package pers.wangzb96.dsalg.tree;

import pers.wangzb96.util.Util;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 二叉树
 * @param <E> 元素泛型
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月8日 12:00:00
 */
public class BinaryTree<E> implements Tree<E>{
    public static final int NUM_CHILDREN = 2;

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    public static int another(int which){
        return 1-which;
    }

    public static class Node<E> implements Tree.Node<E>{
        public static class Creator<E> implements Tree.Node.Creator<E>{
            public Node<E> newNode(E elem, Node<E> parent, int which){
                return new Node<>(elem, parent, which);
            }
        }

        private E elem;
        private Node<E> parent;
        private int which;
        private final Node<E>[] children;

        @SuppressWarnings("unchecked")
        public Node(E elem, Node<E> parent, int which){
            this.elem = elem;
            this.parent = parent;
            this.which = which;
            children = (Node<E>[])new Node[NUM_CHILDREN];
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
        public int getWhich(){
            return which;
        }

        public void setParentWhich(Node<E> parent, int which){
            this.parent = parent;
            this.which = which;
        }

        public Node<E>[] getChildren(){
            return children;
        }

        public Node<E> getChild(int which){
            return getChildren()[which];
        }
        public void setChild(int which, Node<E> child){
            getChildren()[which] = child;
        }

        public int numChildren(){
            var num = 0;
            for(var child: getChildren())
                if(child!=null) ++num;
            return num;
        }

        public Node<E> firstChildNotNull(){
            for(var child: getChildren())
                if(child!=null) return child;
            return null;
        }

        public Node<E> getNear(int which){
            var near = getChild(which);
            if(near!=null) near = near.getFar(another(which));
            return near;
        }
        public Node<E> getFar(int which){
            var far = this;
            Node<E> child;
            while((child=far.getChild(which))!=null) far=child;
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

        public void rotate(int which){
            var parent = getParent();
            var parentWhich = getWhich();
            var child = getChild(another(which));
            if(child==null) return;
            var grandChild = child.getChild(which);

            if(parent!=null)
                parent.setChild(parentWhich, child);
            child.setParentWhich(parent, parentWhich);

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
    private final Node<E> vroot;

    public BinaryTree(){
        this(null);
    }
    public BinaryTree(Node.Creator<E> nodeCreator){
        if(nodeCreator==null) nodeCreator = new Node.Creator<>();
        this.nodeCreator = nodeCreator;
        vroot = nodeCreator.newNode(null, null, -1);
    }

    protected Node.Creator<E> getNodeCreator(){
        return nodeCreator;
    }

    protected Node<E> getVRoot(){
        return vroot;
    }

    protected Node<E> getRoot(){
        return getVRoot().getChild(LEFT);
    }

    protected boolean isRoot(Node<E> node){
        return node==getRoot();
    }

    /**
     * 判断树非空
     * @return 非空：true；空：false
     */
    public boolean isNotEmpty(){
        return getRoot()!=null;
    }

    protected ArrayList<E> preOrder(){
        var ret = new ArrayList<E>();
        var sk = new LinkedList<Node<E>>();
        sk.offerLast(getRoot());
        while(!sk.isEmpty()){
            var node = sk.pollLast();
            if(node==null) continue;
            ret.add(node.getElem());
            sk.offerLast(node.getChild(RIGHT));
            sk.offerLast(node.getChild(LEFT));
        }
        return ret;
    }
    protected ArrayList<E> inOrder(){
        var ret = new ArrayList<E>();
        var sk = new LinkedList<Node<E>>();
        sk.offerLast(getRoot());
        while(!sk.isEmpty()){
            var node = sk.peekLast();
            if(node!=null){
                sk.offerLast(node.getChild(LEFT));
                continue;
            }
            sk.pollLast();
            if(sk.isEmpty()) break;
            node = sk.pollLast();
            ret.add(node.getElem());
            sk.offerLast(node.getChild(RIGHT));
        }
        return ret;
    }
    protected ArrayList<E> postOrder(){
        var ret = new ArrayList<E>();
        var sk = new LinkedList<Node<E>>();
        var sk1 = new LinkedList<E>();
        sk.offerLast(getRoot());
        while(!sk.isEmpty()){
            var node = sk.pollLast();
            if(node==null) continue;
            sk1.offerLast(node.getElem());
            sk.offerLast(node.getChild(LEFT));
            sk.offerLast(node.getChild(RIGHT));
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
            sk.offerLast(new Elem(node.getChild(RIGHT), num));
            sk.offerLast(new Elem(node.getChild(LEFT), num));
        }
        return sb.toString();
    }
}
