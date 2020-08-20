package pers.wangzb96.dsalg.linear;

import java.util.Iterator;

/**
 * Source
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月20日 17:37:05
 */
public interface Source<T> extends Iterable<T>, Iterator<T>{
    @Override
    default Source<T> iterator(){
        return this;
    }

    @Override
    default boolean hasNext(){
        return yet();
    }

    @Override
    default T next(){
        return pick();
    }

    boolean yet();

    T peek();

    T pick();
}
