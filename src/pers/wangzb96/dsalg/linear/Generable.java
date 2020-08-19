package pers.wangzb96.dsalg.linear;

/**
 * Generable
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 12:44:47
 */
public interface Generable<T> extends Iterable<T>{
    interface Generator<T> extends Generable<T>, Iterator<T>{
        @Override
        default Generator<T> iterator(){
            return generator();
        }

        @Override
        default Generator<T> generator(){
            return this;
        }
    }

    @Override
    default Generator<T> iterator(){
        return generator();
    }

    Generator<T> generator();
}
