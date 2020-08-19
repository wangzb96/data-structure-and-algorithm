package pers.wangzb96.dsalg.linear;

/**
 * Iterable
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 12:30:45
 */
public interface Iterable<T> extends java.lang.Iterable<T>{
    interface Iterator<T> extends Iterable<T>, java.util.Iterator<T>{
        @Override
        default Iterator<T> iterator(){
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

    @Override
    Iterator<T> iterator();
}
