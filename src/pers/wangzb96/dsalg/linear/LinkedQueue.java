package pers.wangzb96.dsalg.linear;

import pers.wangzb96.reflect.Creator;
import pers.wangzb96.reflect.CreatorDft;

import java.util.ArrayList;

/**
 * LinkedQueue
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 15:28:39
 */
public final class LinkedQueue<T> implements Queue<T>{
    private final Creator<? extends Node<T>> creator;
    private Node<T> front;
    private Node<T> back;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public LinkedQueue(Creator<? extends Node<T>> creator){
        if(creator==null) creator = (Creator<Node<T>>)new CreatorDft(NodeDft.class);
        this.creator = creator;
        var node = creator.create();
        node.setLinked(node);
        front = back = node;
    }
    public LinkedQueue(){
        this(null);
    }

    public Creator<? extends Node<T>> getCreator(){
        return creator;
    }

    private Node<T> getFront(){
        return front;
    }
    private void setFront(Node<T> front){
        this.front = front;
    }

    private Node<T> getBack(){
        return back;
    }
    private void setBack(Node<T> back){
        this.back = back;
    }

    @Override
    public boolean yet(){
        return getFront()!=getBack();
    }

    @Override
    public T peek(){
        var front = getFront();
        if(front==getBack()) return null;
        return front.getItem();
    }

    @Override
    public T pop(){
        var front = getFront();
        if(front==getBack()) return null;
        var item = front.getItem();
        front.setItem(null);
        setFront(front.getLinked());
        return item;
    }

    @Override
    public Queue<T> push(T item){
        var back = getBack();
        back.setItem(item);
        var node = back.getLinked();
        if(node==getFront()){
            var tmp = getCreator().create();
            back.setLinked(tmp);
            tmp.setLinked(node);
            node = tmp;
        }
        setBack(node);
        return this;
    }

    @Override
    public String toString(){
        var l = new ArrayList<T>();
        var n = 0;

        for(var i=getFront(); i!=getBack(); i=i.getLinked()) l.add(i.getItem());
        var i=getBack();
        do{
            ++n;
            i=i.getLinked();
        }while(i!=getFront());

        return "(%d, %d): %s".formatted(l.size(), n, l.toString());
    }
}
