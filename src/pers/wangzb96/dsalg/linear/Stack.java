package pers.wangzb96.dsalg.linear;

/**
 * Stack
 * top, bottom
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 12:52:16
 */
public interface Stack<T> extends SQH<T>{
    @Override
    default Stack<T> iterator(){
        return this;
    }

    @Override
    default Stack<T> put(T item){
        return push(item);
    }

    @Override
    Stack<T> push(T item);
}
