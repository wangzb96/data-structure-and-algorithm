package pers.wangzb96.test;

import pers.wangzb96.dsalg.linear.*;
import pers.wangzb96.reflect.CreatorDft;

import static pers.wangzb96.util.Util.*;

/**
 * TestNode
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 13:40:03
 */
public interface TestNode{
    final class NodeImp implements Node<Integer>{
        int item = -1;
        Node<Integer> linked = null;

        @Override
        public Integer getItem(){
            return item;
        }
        @Override
        public void setItem(Integer item){
            int tmp = -1;
            if(item!=null) tmp = item;
            this.item = tmp;
        }

        @Override
        public Node<Integer> getLinked(){
            return linked;
        }
        @Override
        public void setLinked(Node<Integer> linked){
            this.linked = linked;
        }
    }

    static void testSQH(SQH<Integer> sqh, Integer item){
        sqh.push(item);
        println(sqh);
        for(var i: sqh){
            if(i>0){
                sqh.push(i/2);
                sqh.push(i/3);
            }
            println(sqh);
        }
        printLine();
    }

    static void main(String[] args){
        var item = 7;
        testSQH(new LinkedStack<>(), item);
        testSQH(new LinkedStack<>(new CreatorDft<>(NodeImp.class)), item);
        testSQH(new LinkedQueue<>(), item);
        testSQH(new LinkedQueue<>(new CreatorDft<>(NodeImp.class)), item);
    }
}
