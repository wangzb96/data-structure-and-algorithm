package pers.wangzb96.test;

import java.util.Random;
import pers.wangzb96.dsalg.tree.*;
import static pers.wangzb96.util.Util.*;

/**
 * 测试树
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月11日 14:57:23
 */
public interface TestTree{
    class TestBinaryTree extends BinaryTree<Integer>{
        public void test(){
            var nodeCreator = getNodeCreator();
            var node = nodeCreator.newNode(123, null, -1);
            println(this);
            println(nodeCreator);
            println(node);
            printLine();

            println(isNotEmpty());
            printLine();

            println(node.getElem());
            println(node.getParent(), node.getWhich());
            println((Object)node.getChildren());
            printLine();

            println(node.numChildren());
            println(node.firstChildNotNull());
            printLine();
        }
    }
    class TestBinarySearchTree extends BinarySearchTree<Integer>{
        public TestBinarySearchTree(){
            super(Integer::compare);
        }
        public void test(){
            insert(100);
            insert(200);
            insert(50);
            insert(80);
            insert(120);
            insert(180);
            insert(30);
            insert(60);
            println(this);
            printLine();

            println(preOrder());
            println(inOrder());
            println(postOrder());
            printLine();
        }
    }
    class TestRedBlackTree extends RedBlackTree<Integer>{
        public TestRedBlackTree(){
            super(Integer::compare);
        }
        public void test(int max, int num, int batch){
            final double PROB = 1.0/3;
            final double PROB1 = 2.0/3;
            final long SEED = 666;
            var rd = new Random(SEED);
            for(var i=0; i<num; ++i){
                var elem = rd.nextInt(max);
                var prob = rd.nextDouble();
                if(prob<PROB) insert(elem);
                else if(prob<PROB1) remove(elem);
                else get(elem);
                if((i<10 || i%batch==0) && !validate()){
                    error("红黑树不合法！");
                    exit();
                }
            }
            println(this);
            printLine();
        }
    }

    static void main(String[] args){
        new TestBinaryTree().test();
        new TestBinarySearchTree().test();
        new TestRedBlackTree().test(10000, 100_0000, 100);
    }
}
