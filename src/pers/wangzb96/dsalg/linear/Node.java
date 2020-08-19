package pers.wangzb96.dsalg.linear;

/**
 * Node
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 13:19:27
 */
public interface Node<T>{
    T getItem();
    void setItem(T item);

    Node<T> getLinked();
    void setLinked(Node<T> linked);
}
