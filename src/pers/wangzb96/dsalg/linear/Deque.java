package pers.wangzb96.dsalg.linear;

/**
 * Deque
 * left, right
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 13:48:28
 */
public interface Deque<T>{
    boolean LEFT = false;
    boolean RIGHT = true;

    boolean yet();

    T peekLeft();
    T peekRight();

    T popLeft();
    T popRight();

    Deque<T> pushLeft(T item);
    Deque<T> pushRight(T item);

    default T peek(boolean which){
        if(which==LEFT) return peekLeft();
        return peekRight();
    }

    default T pop(boolean which){
        if(which==LEFT) return popLeft();
        return popRight();
    }

    default Deque<T> push(boolean which, T item){
        if(which==LEFT) return pushLeft(item);
        return pushRight(item);
    }
}
