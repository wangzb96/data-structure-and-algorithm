package pers.wangzb96.util;

import java.util.Arrays;

/**
 * 各种小工具
 * @author wangzb96
 * @version 1.0
 * @date 2020年8月11日 14:55:13
 */
public interface Util{
    static void exit(int stat){
        System.exit(stat);
    }
    static void exit(){
        exit(0);
    }

    static String toString(Object obj){
        String str;
        if(obj==null) str = "null";
        else if(obj instanceof String tmp) str = tmp;
        else if(obj.getClass().isArray()){
            if(obj instanceof int[] arr) str = Arrays.toString(arr);
            else if(obj instanceof double[] arr) str = Arrays.toString(arr);
            else if(obj instanceof boolean[] arr) str = Arrays.toString(arr);
            else if(obj instanceof char[] arr) str = Arrays.toString(arr);
            else if(obj instanceof long[] arr) str = Arrays.toString(arr);
            else if(obj instanceof short[] arr) str = Arrays.toString(arr);
            else if(obj instanceof byte[] arr) str = Arrays.toString(arr);
            else if(obj instanceof float[] arr) str = Arrays.toString(arr);
            else str = Arrays.toString((Object[])obj);
        }else str = obj.toString();
        return str;
    }
    static String toString(String sep, String end, Object... args){
        if(sep==null) sep = ", ";
        if(end==null) end = "\n";
        var sb = new StringBuilder();
        for(var i=0; i<args.length; ++i){
            if(i>0) sb.append(sep);
            sb.append(toString(args[i]));
        }
        sb.append(end);
        return sb.toString();
    }

    private static void _error(String str){
        System.err.print(str);
    }

    static void errorse(String sep, String end, Object... args){
        _error(toString(sep, end, args));
    }
    static void error(Object... args){
        errorse(null, null, args);
    }

    private static void _print(String str){
        System.out.print(str);
    }

    static void printse(String sep, String end, Object... args){
        _print(toString(sep, end, args));
    }
    static void prints(String sep, Object... args){
        printse(sep, "", args);
    }
    static void printsls(String sep, Object... args){
        printse(sep, " ", args);
    }
    static void printsln(String sep, Object... args){
        printse(sep, "\n", args);
    }
    static void printe(String end, Object... args){
        printse(null, end, args);
    }
    static void print(Object... args){
        printse(null, "", args);
    }
    static void printls(Object... args){
        printse(null, " ", args);
    }
    static void println(Object... args){
        printse(null, "\n", args);
    }

    static String line(String str, int num){
        if(str==null) str = "-";
        if(num<0) num = 64;
        return str.repeat(num);
    }

    static void printLine(String str, int num){
        printse(null, null, line(str, num));
    }
    static void printLine(String str){
        printLine(str, -1);
    }
    static void printLine(int num){
        printLine(null, num);
    }
    static void printLine(){
        printLine(null, -1);
    }

    static long getTimeMS(){
        return System.currentTimeMillis();
    }
    static long getTimeNS(){
        return System.nanoTime();
    }
}
