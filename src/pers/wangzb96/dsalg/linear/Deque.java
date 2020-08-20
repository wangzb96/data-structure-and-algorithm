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

    default T peek(boolean which){
        if(which==LEFT) return peekLeft();
        return peekRight();
    }

    default T pickLeft(){
        return popLeft();
    }
    default T pickRight(){
        return popRight();
    }

    default T pick(boolean which){
        return pop(which);
    }

    T popLeft();
    T popRight();

    default T pop(boolean which){
        if(which==LEFT) return popLeft();
        return popRight();
    }

    default Deque<T> putLeft(T item){
        return pushLeft(item);
    }
    default Deque<T> putRight(T item){
        return pushRight(item);
    }

    default Deque<T> put(boolean which, T item){
        return push(which, item);
    }

    Deque<T> pushLeft(T item);
    Deque<T> pushRight(T item);

    default Deque<T> push(boolean which, T item){
        if(which==LEFT) return pushLeft(item);
        return pushRight(item);
    }
}
