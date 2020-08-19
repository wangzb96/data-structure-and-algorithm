package pers.wangzb96.reflect;

import java.lang.reflect.Constructor;

/**
 * CreatorDft
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 17:58:26
 */
public final class CreatorDft<T> implements Creator<T>{
    private final Constructor<T> constructor;

    public CreatorDft(Class<T> cls, Class<?>... types){
        constructor = Creator.getConstructor(cls, types);
    }

    public Constructor<T> getConstructor(){
        return constructor;
    }

    @Override
    public T create(Object... args){
        return Creator.create(getConstructor(), args);
    }
}
