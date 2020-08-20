package pers.wangzb96.dsalg.linear;

/**
 * Queue
 * front, back
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 13:04:48
 */
public interface Queue<T> extends SQH<T>{
    @Override
    default Queue<T> iterator(){
        return this;
    }

    @Override
    default Queue<T> put(T item){
        return push(item);
    }

    @Override
    Queue<T> push(T item);
}
