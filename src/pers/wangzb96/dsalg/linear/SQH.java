package pers.wangzb96.dsalg.linear;

/**
 * SQH
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 13:06:40
 */
public interface SQH<T> extends Source<T>, Target<T>{
    @Override
    default SQH<T> iterator(){
        return this;
    }

    @Override
    default T pick(){
        return pop();
    }

    @Override
    default SQH<T> put(T item){
        return push(item);
    }

    T pop();

    SQH<T> push(T item);
}
