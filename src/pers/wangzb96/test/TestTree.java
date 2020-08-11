package pers.wangzb96.test;

import java.util.Random;
import java.util.TreeMap;

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
        public long test(int max, int num, double prob, double prob1,
                         boolean val, int batch, boolean p){
            final double PROB = 1.0/3;
            final double PROB1 = 2.0/3;
            final long SEED = 666;
            var rd = new Random(SEED);
            var start = getTimeMS();
            for(var i=0; i<num; ++i){
                var elem = rd.nextInt(max);
                var prb = rd.nextDouble();
                if(prb<prob) insert(elem);
                else if(prb<prob1) remove(elem);
                else get(elem);
                if(val && (i<10 || i%batch==0) && !validate()){
//                    error("红黑树不合法！");
                    error("*** RBTree ERROR! ***");
                    exit();
                }
            }
            var elap = getTimeMS()-start;
            if(p) println(this);
            return elap;
        }
    }
    class TestTreeMap{
        @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnnecessaryLocalVariable"})
        public long test(int max, int num, double prob, double prob1){
            var map = new TreeMap<Integer, Object>();
            final long SEED = 666;
            var rd = new Random(SEED);
            var start = getTimeMS();
            for(var i=0; i<num; ++i){
                var elem = rd.nextInt(max);
                var prb = rd.nextDouble();
                if(prb<prob) map.put(elem, elem);
                else if(prb<prob1) map.remove(elem);
                else map.get(elem);
            }
            var elap = getTimeMS()-start;
            return elap;
        }
    }

    static void main(String[] args){
//        new TestBinaryTree().test();
//        new TestBinarySearchTree().test();
        var max = 1_00_0000;
        var num = 1_00_0000;
        var prob = 0.5;
        var prob1 = 0.5;
        for(var i=0; i<10; ++i){
            var elap = new TestRedBlackTree().test(max, num, prob, prob1,
                                                   false, 100, false);
            var elap1 = new TestTreeMap().test(max, num, prob, prob1);
            println("%d | %.3f | %.3f (S)".formatted(i, elap/1000.0, elap1/1000.0));
        }
        printLine();
    }
}
