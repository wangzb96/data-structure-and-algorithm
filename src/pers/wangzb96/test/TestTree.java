package pers.wangzb96.test;

import java.util.Random;
import java.util.TreeMap;

import pers.wangzb96.dsalg.tree.*;
import static pers.wangzb96.util.Util.*;

/**
 * 测试树
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月8日 12:00:00
 */
public interface TestTree{
    class TestBinarySearchTree{
        public static void test(){
            var bst = new BinarySearchTree<Integer>();
            bst.insert(100);
            bst.insert(50);
            bst.insert(30);
            bst.insert(20);
            bst.insert(200);
            bst.insert(300);
            bst.insert(120);
            bst.insert(150);
            println(bst);
            printLine();

            println(bst.get(20), bst.set(300, 333), bst.insert(999), bst.remove(120));
            printLine();

            println(bst);
            printLine();

            println(bst.preOrder());
            println(bst.inOrder());
            println(bst.postOrder());
            printLine();
        }
    }
    class TestRedBlackTree{
        public static long test(int max, int num, double prob, double prob1,
                                boolean val, int batch, boolean p){
            final long SEED = 666;
            var rd = new Random(SEED);
            var rbt = new RedBlackTree<Integer>();
            var start = getTimeMS();
            for(var i=0; i<num; ++i){
                var elem = rd.nextInt(max);
                var prb = rd.nextDouble();
                if(prb<prob) rbt.insert(elem);
                else if(prb<prob1) rbt.remove(elem);
                else rbt.get(elem);
                if(val && (i<10 || i%batch==0) && !rbt.validate()){
                    error("红黑树不合法！");
                    exit();
                }
            }
            var elap = getTimeMS()-start;
            if(p) println(rbt);
            return elap;
        }
    }
    class TestTreeMap{
        @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "UnnecessaryLocalVariable"})
        public static long test(int max, int num, double prob, double prob1){
            var map = new TreeMap<Integer, Object>();
            final long SEED = 666;
            var rd = new Random(SEED);
            var start = getTimeMS();
            for(var i=0; i<num; ++i){
                var elem = rd.nextInt(max);
                var prb = rd.nextDouble();
                if(prb<prob) map.put(elem, null);
                else if(prb<prob1) map.remove(elem);
                else map.get(elem);
            }
            var elap = getTimeMS()-start;
            return elap;
        }
    }

    static void main(String[] args){
        TestBinarySearchTree.test();
        TestRedBlackTree.test(1_0000, 1_0000, 0.33, 0.66,
                              true, 10, true);

//        var max = 10000*100;
//        var num = 10000*1;
//        var bi = num/100;
//        var bj = max/10;
//        var prob = 1;
//        var prob1 = 1;
//        for(var i=0; i<10; ++i){
//            var elap = new TestRedBlackTree().test(max, num, prob, prob1,
//                                                   false, 100, false);
//            var elap = 0.0;
//            var elap1 = new TestTreeMap().test(max, num, prob, prob1);
//            println("%d | %.3f | %.3f (S)".formatted(i, elap/1000.0, elap1/1000.0));
//        }
//        var l = new ArrayList<RedBlackTree.Node<Integer>>(max);
//        var c = new RedBlackTree.Node.Creator<Integer>();
//        for(var i=0; i<num; ++i){
//            if(i%bi==0) printe(": ", i);
//            for(var j=0; j<max; ++j){
//                if(i%bi==0 && j%bj==0) printls(j);
//                var node = c.newNode(j, null, -1);
//                if(j>=l.size()) l.add(node);
//                else l.set(j, node);
//            }
//            if(i%bi==0) println();
    }
}

