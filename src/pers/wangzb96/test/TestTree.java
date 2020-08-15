package pers.wangzb96.test;

import java.util.Random;

import pers.wangzb96.dsalg.tree.*;
import static pers.wangzb96.util.Util.*;

/**
 * 测试树
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月8日 12:00:00
 */
@FunctionalInterface
public interface TestTree{
    void test(BinarySearchTree<Integer> bst, Integer elem);

    static long test(BinarySearchTree<Integer> bst, TestTree testTree,
                     Random rd, int max, int num,
                     boolean val, int batch, String name){

        long elap = 0;
        for(var i=0; i<num; ++i){
            var elem = rd.nextInt(max);

            var start = getTimeNS();
            testTree.test(bst, elem);
            elap += getTimeNS()-start;

            if(val && (i<10 || i%batch==0) && !bst.validate()){
                error("%s不合法！".formatted(name));
                exit();
            }
        }
        return elap;
    }

    static void main(String[] args){
        @SuppressWarnings("unchecked")
        var trees = (BinarySearchTree<Integer>[])new BinarySearchTree[]{
                new BinarySearchTree<Integer>(),
                new RedBlackTree<Integer>(),
                new AVLTree<Integer>(),
        };
        var rd = new Random(666);
        var max = 10000*100;
        var num = 10000*100;
        var val = false;
        var batch = 100;
        var names = new String[]{
                "二叉查找树",
                "红黑树",
                "AVL树",
        };

        for(var i=0; i<3; ++i){
            var bst = trees[i];
            var name = names[i];
            long elap;

            println("%s:".formatted(name));
            elap = test(bst, BinarySearchTree::insert, rd, max, num, val, batch, name);
            println("插入： %.3f (S)".formatted(elap/1e9));

            elap = test(bst, BinarySearchTree::get, rd, max, num, false, batch, name);
            println("获取： %.3f (S)".formatted(elap/1e9));

            elap = test(bst, BinarySearchTree::remove, rd, max, num, val, batch, name);
            println("删除： %.3f (S)".formatted(elap/1e9));
            printLine();
        }
    }
}
