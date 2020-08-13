package pers.wangzb96.dsalg.tree;

import pers.wangzb96.util.Util;

import java.util.Comparator;
import java.util.LinkedList;

/**
 * 红黑树
 * @param <E> 元素泛型
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月8日 12:00:00
 */
public class RedBlackTree<E> extends BinarySearchTree<E>{
    public static final boolean RED = false;
    public static final boolean BLACK = true;

    protected static <E> boolean isNullOrBlack(Node<E> node){
        return node==null || node.isBlack();
    }

    public static class Node<E> extends BinarySearchTree.Node<E>{
        public static class Creator<E> extends BinarySearchTree.Node.Creator<E>{
            @Override
            public Node<E> newNode(E elem, BinarySearchTree.Node<E> parent, boolean which){
                return new Node<>(elem, (Node<E>)parent, which);
            }
        }

        private boolean color;

        public Node(E elem, Node<E> parent, boolean which){
            super(elem, parent, which);
            color = RED;
        }

        public boolean getColor(){
            return color;
        }
        public void setColor(boolean color){
            this.color = color;
        }

        public void swapColor(Node<E> node){
            var color = getColor();
            setColor(node.getColor());
            node.setColor(color);
        }

        public boolean isColor(boolean color){
            return getColor()==color;
        }
        public boolean isRed(){
            return isColor(RED);
        }
        public boolean isBlack(){
            return isColor(BLACK);
        }

        @Override
        public String toString(){
            return "%s %s".formatted(isRed()? "R": "B", Util.toString(getElem()));
        }
    }

    public RedBlackTree(){
        this(null, null);
    }
    public RedBlackTree(Node.Creator<E> nodeCreator){
        this(nodeCreator, null);
    }
    public RedBlackTree(Comparator<E> comparator){
        this(null, comparator);
    }
    public RedBlackTree(Node.Creator<E> nodeCreator, Comparator<E> comparator){
        super(nodeCreator!=null? nodeCreator: new Node.Creator<>(), comparator);
    }

    @Override
    protected void insert(BinarySearchTree.Node<E> bstNode){
        var node = (Node<E>)bstNode;

        node.linkParent();
        while(true){
            // node是根节点
            if(isRoot(node)){
                node.setColor(BLACK);
                break;
            }

            // node不是根节点
            var parent = (Node<E>)node.getParent();

            // node的爸爸是黑色的
            if(parent.isBlack()) break;

            // node的爸爸是红色的
            var grandParent = (Node<E>)parent.getParent();
            var uncle = (Node<E>)parent.getBrother();

            // node的爷爷一定存在
            // node的舅舅不存在或黑色
            if(isNullOrBlack(uncle)){
                // node是舅舅的近侄子
                if(node.getWhich()!=parent.getWhich()){
                    parent.rotate(another(node.getWhich()));
                    node = parent;
                    parent = (Node<E>)node.getParent();
                }

                // node是舅舅的远侄子
                grandParent.swapColor(parent);
                grandParent.rotate(another(parent.getWhich()));
                break;
            }

            // node的舅舅是红色的
            grandParent.setColor(RED);
            parent.setColor(BLACK);
            uncle.setColor(BLACK);

            node = grandParent;
        }
    }

    @Override
    protected Node<E> removeLeaf(BinarySearchTree.Node<E> bstNode){
        var node = (Node<E>)bstNode;

        // node是红色的
        if(node.isRed()){
            node.unlinkParent();
            return node;
        }

        // node是黑色的
        Node<E> ret = node;
        while(true){
            // node是根节点
            if(isRoot(node)) break;

            // node不是根节点
            var parent = (Node<E>)node.getParent();
            var which = node.getWhich();
            var brother = (Node<E>)node.getBrother();

            // node的兄弟一定存在
            // node的兄弟是红色的
            if(brother.isRed()){
                parent.swapColor(brother);
                parent.rotate(which);
                brother = (Node<E>)node.getBrother();
            }

            // node的兄弟是黑色的
            var nearNephew = (Node<E>)brother.getChild(which);
            var farNephew = (Node<E>)brother.getChild(another(which));

            // node的近侄子是红色的且远侄子不存在或黑色
            if(nearNephew!=null && nearNephew.isRed() && isNullOrBlack(farNephew)){
                brother.swapColor(nearNephew);
                brother.rotate(another(which));
                brother = (Node<E>)node.getBrother();
                farNephew = (Node<E>)brother.getChild(another(which));
            }

            // node的远侄子是红色的
            if(farNephew!=null && farNephew.isRed()){
                parent.swapColor(brother);
                farNephew.setColor(BLACK);
                parent.rotate(which);
                break;
            }

            // node没有侄子或侄子都是黑色的
            // node的爸爸是红色的
            if(parent.isRed()){
                parent.swapColor(brother);
                break;
            }

            // node的爸爸是黑色的
            brother.setColor(RED);

            node = parent;
        }
        ret.unlinkParent();
        return ret;
    }

    /**
     * 判断红黑树是否合法
     * @return 合法：true；不合法：false
     */
    public boolean validate(){
        return validateRoot() && validateRed() && validateBlack();
    }
    protected boolean validateRoot(){
        return isNullOrBlack((Node<E>)getRoot());
    }
    protected boolean validateRed(){
        var q = new LinkedList<Node<E>>();
        q.offerLast((Node<E>)getRoot());
        while(!q.isEmpty()){
            var node = q.pollFirst();
            if(node==null) continue;
            var left = (Node<E>)node.getLeft();
            var right = (Node<E>)node.getRight();
            if(node.isRed()){
                if(left!=null && left.isRed()) return false;
                if(right!=null && right.isRed()) return false;
            }
            q.offerLast(left);
            q.offerLast(right);
        }
        return true;
    }
    protected boolean validateBlack(){
        class Elem{
            public final Node<E> node;
            public final int num;

            public Elem(Node<E> node, int num){
                this.node = node;
                this.num = num;
            }
        }

        var q = new LinkedList<Elem>();
        q.offerLast(new Elem((Node<E>)getRoot(), 0));
        var res = -1;
        while(!q.isEmpty()){
            var elem = q.pollFirst();
            var node = elem.node;
            var num = elem.num;
            if(node==null){
                if(res<0) res = num;
                else if(num!=res) return false;
                continue;
            }
            var left = (Node<E>)node.getLeft();
            var right = (Node<E>)node.getRight();
            if(node.isBlack()) ++num;
            q.offerLast(new Elem(left, num));
            q.offerLast(new Elem(right, num));
        }
        return true;
    }
}
