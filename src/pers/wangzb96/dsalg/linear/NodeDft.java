package pers.wangzb96.dsalg.linear;

/**
 * NodeDft
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 17:23:23
 */
public final class NodeDft<T> implements Node<T>{
    private T item;
    private Node<T> linked;

    @Override
    public T getItem(){
        return item;
    }
    @Override
    public void setItem(T item){
        this.item = item;
    }

    @Override
    public Node<T> getLinked(){
        return linked;
    }
    @Override
    public void setLinked(Node<T> linked){
        this.linked = linked;
    }
}
