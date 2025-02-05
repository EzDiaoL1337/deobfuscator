package com.diaoling.javavm.utils;

import com.diaoling.javavm.values.JavaValue;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;

import java.util.List;

public class ExecutionUtils {
    public static boolean areValuesUnknown(JavaValue instance, List<JavaValue> args) {
        if (instance.is(JavaValueType.UNKNOWN))
            return true;
        for (JavaValue value : args) {
            if (value.is(JavaValueType.UNKNOWN)) {
                return true;
            }
        }

        return false;
    }

    public static boolean areValuesUnknown(JavaValue... args) {
        for (JavaValue value : args) {
            if (value.is(JavaValueType.UNKNOWN)) {
                return true;
            }
        }

        return false;
    }

    public static boolean areValuesUnknown(JavaWrapper instance, JavaWrapper[] args) {
        if (instance != null && instance.get().is(JavaValueType.UNKNOWN))
            return true;
        if (args != null)
            for (JavaWrapper value : args) {
                if (value.get().is(JavaValueType.UNKNOWN)) {
                    return true;
                }
            }

        return false;
    }

    public static boolean areValuesUnknown(JavaWrapper instance, List<JavaWrapper> args) {
        if (instance != null && instance.get().is(JavaValueType.UNKNOWN))
            return true;
        if (args != null)
            for (JavaWrapper value : args) {
                if (value.get().is(JavaValueType.UNKNOWN)) {
                    return true;
                }
            }

        return false;
    }

    public static boolean areValuesUnknown(List<JavaWrapper> args) {
        for (JavaWrapper value : args) {
            if (value.get().is(JavaValueType.UNKNOWN)) {
                return true;
            }
        }

        return false;
    }

    public static boolean areValuesUnknown(JavaWrapper... args) {
        for (JavaWrapper value : args) {
            if (value != null && value.get().is(JavaValueType.UNKNOWN)) {
                return true;
            }
        }

        return false;
    }

//    public static String hash(String owner, String name, String desc) {
//        return owner + "." + name + "." + desc;
//    }
//
//    public static JavaValue[] convert(Object[] arr, JavaClass type, String orig) {
//        JavaValue[] converted = new JavaValue[arr.length];
//        for (int i = 0; i < arr.length; i++) {
//            converted[i] = new JavaObject(arr[i], type, orig);
//        }
//        return converted;
//    }
//
//    public static <T> T[] convert(JavaValue[] arr, Class<T> type) {
//        T[] t = (T[]) ArrayHelper.newInstance(type, arr.length);
//        for (int i = 0; i < arr.length; i++) {
//            t[i] = (T) arr[i].value();
//        }
//        return t;
//    }
}
