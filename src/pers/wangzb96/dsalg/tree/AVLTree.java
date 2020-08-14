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
    public static final boolean INSERT = true;
    public static final boolean REMOVE = false;

    public static class Node<E> extends BinarySearchTree.Node<E>{

        public static class Creator<E> extends BinarySearchTree.Node.Creator<E>{
            @Override
            public Node<E> newNode(E elem, BinarySearchTree.Node<E> parent, boolean which){
                return new Node<>(elem, (Node<E>)parent, which);
            }
        }

        private int balanceFactor = 0;

        public Node(E elem, Node<E> parent, boolean which){
            super(elem, parent, which);
        }

        public int getBalanceFactor(){
            return balanceFactor;
        }
        public void setBalanceFactor(int balanceFactor){
            this.balanceFactor = balanceFactor;
        }

        public void updateBalanceFactor(boolean which, boolean how){
            updateBalanceFactor(which, how, 1);
        }
        public void updateBalanceFactor(boolean which, boolean how, int many){
            var delta = which==how? 1: -1;
            setBalanceFactor(getBalanceFactor()+delta*many);
        }

        @Override
        public String toString(){
            return "%d %s".formatted(getBalanceFactor(), Util.toString(getElem()));
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
            parent.updateBalanceFactor(which, INSERT);
            var balanceFactor = parent.getBalanceFactor();

            // node的爸爸是完全平衡的，插入前是基本平衡的，高度未变
            if(balanceFactor==0) break;

            // node的爸爸是不平衡的
            if(balanceFactor<-1 || balanceFactor>1){
                // node和node的爸爸不在同一边失衡
                if(node.getBalanceFactor()*balanceFactor<0){
                    // node的儿子一定存在
                    var child = (Node<E>)node.getChild(another(which));
                    node.updateBalanceFactor(another(which), REMOVE);
                    child.updateBalanceFactor(which, INSERT);
                    node.rotate(which);
                    node = child;
                }

                // node和node的爸爸在同一边失衡
                parent.updateBalanceFactor(which, REMOVE, 2);
                node.updateBalanceFactor(another(which), INSERT);
                parent.rotate(another(which));
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
            parent.updateBalanceFactor(which, REMOVE);
            var balanceFactor = parent.getBalanceFactor();

            // node的爸爸是基本平衡的，删除前是平衡的，高度未变
            if(balanceFactor==-1 || balanceFactor==1) break;

            // node的爸爸是不平衡的
            if(balanceFactor<-1 || balanceFactor>1){
                // node的兄弟一定存在
                var brother = (Node<E>)node.getBrother();

                // node的兄弟是平衡的
                if(brother.getBalanceFactor()==0){
                    parent.updateBalanceFactor(another(which), REMOVE);
                    brother.updateBalanceFactor(which, INSERT);
                    parent.rotate(which);
                    break;
                }

                // node的兄弟是不平衡的
                // node的兄弟和node的爸爸不在同一边失衡
                if(brother.getBalanceFactor()*balanceFactor<0){
                    // node的侄子一定存在
                    var nephew = (Node<E>)brother.getChild(which);
                    brother.updateBalanceFactor(which, REMOVE);
                    nephew.updateBalanceFactor(another(which), INSERT);
                    brother.rotate(another(which));
                    brother = nephew;
                }

                // node的兄弟和node的爸爸在同一边失衡
                parent.updateBalanceFactor(another(which), REMOVE, 2);
                brother.updateBalanceFactor(which, INSERT);
                parent.rotate(which);
                parent = brother;
            }

            // node的爸爸是完全平衡的，删除前是基本平衡的，高度减一
            node = parent;
        }
        return ret;
    }

    @Override
    public boolean validate(){
        return super.validate() && validateBalanceFactor() && validateBalance();
    }
    protected boolean validateBalanceFactor(){
        class Elem{
            public final Node<E> node;
            public Elem left = null;
            public Elem right = null;
            public int height = 0;

            public Elem(Node<E> node){
                this.node = node;
            }
        }

        var sk = new LinkedList<Elem>();
        var sk1 = new LinkedList<Elem>();
        sk.offerLast(new Elem((Node<E>)getRoot()));
        while(!sk.isEmpty()){
            var elem = sk.pollLast();
            var node = elem.node;
            if(node==null) continue;
            var left = new Elem((Node<E>)node.getLeft());
            var right = new Elem((Node<E>)node.getRight());
            elem.left = left;
            elem.right = right;
            sk1.offerLast(elem);
            sk.offerLast(left);
            sk.offerLast(right);
        }
        while(!sk1.isEmpty()){
            var elem = sk1.pollLast();
            elem.height = Math.max(elem.left.height, elem.right.height)+1;
            var balanceFactor = elem.left.height-elem.right.height;
            if(balanceFactor!=elem.node.getBalanceFactor()) return false;
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
