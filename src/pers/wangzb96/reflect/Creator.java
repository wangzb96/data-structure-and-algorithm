package pers.wangzb96.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import pers.wangzb96.util.Util;

/**
 * Creator
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月19日 14:43:23
 */
public interface Creator<T>{
    static <T> Constructor<T> getConstructor(Class<T> cls, Class<?>... types){
        Constructor<T> constructor = null;
        try{
            constructor = cls.getConstructor(types);
        }catch(NoSuchMethodException e){
            Util.error("构造器不存在！");
        }catch(SecurityException e){
            Util.error("构造器安全异常！");
        }
        return constructor;
    }

    static <T> T create(Constructor<T> constructor, Object... args){
        T ret = null;
        try{
            ret = constructor.newInstance(args);
        }catch(IllegalAccessException e){
            Util.error("构造器不可访问！");
        }catch(InstantiationException e){
            Util.error("构造器不可创建对象！");
        }catch(IllegalArgumentException e){
            Util.error("构造器参数不合法！");
        }catch(InvocationTargetException e){
            Util.error("构造器异常！");
        }
        return ret;
    }

    T create(Object... args);
}
