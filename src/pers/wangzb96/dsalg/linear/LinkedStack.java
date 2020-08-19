package pers.wangzb96.dsalg.linear;

import pers.wangzb96.reflect.Creator;
import pers.wangzb96.reflect.CreatorDft;

import java.util.ArrayList;

/**
 * LinkedStack
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 15:06:17
 */
public final class LinkedStack<T> implements Stack<T>{
    private final Creator<? extends Node<T>> creator;
    private Node<T> top = null;
    private Node<T> bottom = null;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public LinkedStack(Creator<? extends Node<T>> creator){
        if(creator==null) creator = (Creator<Node<T>>)new CreatorDft(NodeDft.class);
        this.creator = creator;
    }
    public LinkedStack(){
        this(null);
    }

    public Creator<? extends Node<T>> getCreator(){
        return creator;
    }

    private Node<T> getTop(){
        return top;
    }
    private void setTop(Node<T> top){
        this.top = top;
    }

    private Node<T> getBottom(){
        return bottom;
    }
    private void setBottom(Node<T> bottom){
        this.bottom = bottom;
    }

    @Override
    public boolean yet(){
        return getTop()!=null;
    }

    @Override
    public T peek(){
        var top = getTop();
        if(top==null) return null;
        return top.getItem();
    }

    @Override
    public T pop(){
        var top = getTop();
        if(top==null) return null;
        var item = top.getItem();
        top.setItem(null);
        setTop(top.getLinked());
        top.setLinked(getBottom());
        setBottom(top);
        return item;
    }

    @Override
    public Stack<T> push(T item){
        var bottom = getBottom();
        if(bottom==null) bottom = getCreator().create();
        bottom.setItem(item);
        setBottom(bottom.getLinked());
        bottom.setLinked(getTop());
        setTop(bottom);
        return this;
    }

    @Override
    public String toString(){
        var l = new ArrayList<T>();
        var n = 0;

        for(var i=getTop(); i!=null; i=i.getLinked()) l.add(i.getItem());
        for(var i=getBottom(); i!=null; i=i.getLinked()) ++n;

        return "(%d, %d): %s".formatted(l.size(), n, l.toString());
    }
}
