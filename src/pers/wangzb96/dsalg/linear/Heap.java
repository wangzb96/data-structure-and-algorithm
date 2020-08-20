package pers.wangzb96.dsalg.linear;

/**
 * Heap
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 13:12:16
 */
public interface Heap<T> extends SQH<T>{
    @Override
    default Heap<T> iterator(){
        return this;
    }

    @Override
    default Heap<T> put(T item){
        return push(item);
    }

    @Override
    Heap<T> push(T item);
}
