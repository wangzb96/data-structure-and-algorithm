package pers.wangzb96.dsalg.tree;

import pers.wangzb96.util.Util;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * AVL树
 * @param <E> 元素泛型
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月14日 11:21:35
 */
public class AVLTree<E> extends BinarySearchTree<E>{
    protected static <E> int getNodeHeight(Node<E> node){
        if(node==null) return 0;
        return node.getHeight();
    }

    public static class Node<E> extends BinarySearchTree.Node<E>{
        public static class Creator<E> extends BinarySearchTree.Node.Creator<E>{
            @Override
            public Node<E> newNode(E elem, BinarySearchTree.Node<E> parent, boolean which){
                return new Node<>(elem, (Node<E>)parent, which);
            }
        }

        private int height = 1;

        public Node(E elem, Node<E> parent, boolean which){
            super(elem, parent, which);
        }

        public int getHeight(){
            return height;
        }
        public void setHeight(int height){
            this.height = height;
        }

        public int calculateHeight(){
            var left = (Node<E>)getLeft();
            var right = (Node<E>)getRight();
            return Math.max(getNodeHeight(left), getNodeHeight(right))+1;
        }
        public void updateHeight(){
            setHeight(calculateHeight());
        }

        public int getBalanceFactor(){
            var left = (Node<E>)getLeft();
            var right = (Node<E>)getRight();
            return getNodeHeight(right)-getNodeHeight(left);
        }

        @Override
        public String toString(){
            var balanceFactor = getBalanceFactor();
            var str = balanceFactor==0? "0": balanceFactor<0? "L": "R";
            return "%s %s".formatted(str, Util.toString(getElem()));
        }
    }

    public AVLTree(){
        this(null, null);
    }
    public AVLTree(Node.Creator<E> nodeCreator){
        this(nodeCreator, null);
    }
    public AVLTree(Comparator<E> comparator){
        this(null, comparator);
    }
    public AVLTree(Node.Creator<E> nodeCreator, Comparator<E> comparator){
        super(nodeCreator!=null? nodeCreator: new Node.Creator<>(), comparator);
    }

    @Override
    protected void insert(BinarySearchTree.Node<E> bstNode){
        var node = (Node<E>)bstNode;
        node.linkParent();

        while(true){
            // node是根节点
            if(isRoot(node)) break;

            // node不是根节点
            var parent = (Node<E>)node.getParent();
            var which = node.getWhich();
            parent.updateHeight();
            var balanceFactor = parent.getBalanceFactor();

            // node的爸爸是完全平衡的，插入前是基本平衡的，高度未变
            if(balanceFactor==0) break;

            // node的爸爸是不平衡的
            if(balanceFactor<-1 || balanceFactor>1){
                // node和node的爸爸不在同一边失衡
                if(node.getBalanceFactor()*balanceFactor<0){
                    // node的儿子一定存在
                    var child = (Node<E>)node.getChild(another(which));
                    node.rotate(which);
                    node.updateHeight();
                    child.updateHeight();
                    node = child;
                }

                // node和node的爸爸在同一边失衡
                parent.rotate(another(which));
                parent.updateHeight();
                node.updateHeight();
                break;
            }

            // node的爸爸是基本平衡的，插入前是完全平衡的，高度加一
            node = parent;
        }
    }

    @Override
    protected Node<E> removeLeaf(BinarySearchTree.Node<E> bstNode){
        var node = (Node<E>)bstNode;
        node.unlinkParent();
        Node<E> ret = node;

        while(true){
            // node是根节点
            if(isRoot(node)) break;

            // node不是根节点
            var parent = (Node<E>)node.getParent();
            var which = node.getWhich();
            parent.updateHeight();
            var balanceFactor = parent.getBalanceFactor();

            // node的爸爸是基本平衡的，删除前是平衡的，高度未变
            if(balanceFactor==-1 || balanceFactor==1) break;

            // node的爸爸是不平衡的
            if(balanceFactor<-1 || balanceFactor>1){
                // node的兄弟一定存在
                var brother = (Node<E>)node.getBrother();
                var brotherBalanceFactor = brother.getBalanceFactor();

                // node的兄弟是平衡的
                if(brotherBalanceFactor==0){
                    parent.rotate(which);
                    parent.updateHeight();
                    brother.updateHeight();
                    break;
                }

                // node的兄弟是不平衡的
                // node的兄弟和node的爸爸不在同一边失衡
                if(brotherBalanceFactor*balanceFactor<0){
                    // node的侄子一定存在
                    var nephew = (Node<E>)brother.getChild(which);
                    brother.rotate(another(which));
                    brother.updateHeight();
                    nephew.updateHeight();
                    brother = nephew;
                }

                // node的兄弟和node的爸爸在同一边失衡
                parent.rotate(which);
                parent.updateHeight();
                brother.updateHeight();
                parent = brother;
            }

            // node的爸爸是完全平衡的，删除前是基本平衡的，高度减一
            node = parent;
        }
        return ret;
    }

    @Override
    public boolean validate(){
        return super.validate() && validateHeight() && validateBalance();
    }
    protected boolean validateHeight(){
        var sk = new LinkedList<Node<E>>();
        var sk1 = new LinkedList<Node<E>>();
        sk.offerLast((Node<E>)getRoot());
        while(!sk.isEmpty()){
            var node = sk.pollLast();
            if(node==null) continue;
            var left = (Node<E>)node.getLeft();
            var right = (Node<E>)node.getRight();
            sk1.offerLast(node);
            sk.offerLast(left);
            sk.offerLast(right);
        }
        while(!sk1.isEmpty()){
            var node = sk1.pollLast();
            if(node.calculateHeight()!=node.getHeight()) return false;
        }
        return true;
    }
    protected boolean validateBalance(){
        var q = new LinkedList<Node<E>>();
        q.offerLast((Node<E>)getRoot());
        while(!q.isEmpty()){
            var node = q.pollFirst();
            if(node==null) continue;
            var balanceFactor = node.getBalanceFactor();
            if(balanceFactor<-1 || balanceFactor>1) return false;
            q.offerLast((Node<E>)node.getLeft());
            q.offerLast((Node<E>)node.getRight());
        }
        return true;
    }
}
