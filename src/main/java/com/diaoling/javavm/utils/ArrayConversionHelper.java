/*
 * Copyright 2017 Sam Sun <github-contact@samczsun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diaoling.javavm.utils;

import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaArray;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;

public class ArrayConversionHelper {
    public static JavaWrapper convert(VirtualMachine vm, Object array) {
        if (array == null) {
            return vm.getNull();
        }
        if (array instanceof Object[])
            return convertObjectArray(vm, (Object[]) array);
        if (array instanceof boolean[])
            return convertBooleanArray(vm, (boolean[]) array);
        if (array instanceof byte[])
            return convertByteArray(vm, (byte[]) array);
        if (array instanceof char[])
            return convertCharArray(vm, (char[]) array);
        if (array instanceof short[])
            return convertShortArray(vm, (short[]) array);
        if (array instanceof int[])
            return convertIntArray(vm, (int[]) array);
        if (array instanceof long[])
            return convertLongArray(vm, (long[]) array);
        if (array instanceof float[])
            return convertFloatArray(vm, (float[]) array);
        if (array instanceof double[])
            return convertDoubleArray(vm, (double[]) array);
        if (array == null)
            throw new NullPointerException();
        throw new IllegalArgumentException();
    }

    public static JavaWrapper convertBooleanArray(VirtualMachine vm, boolean[] array) {
        if (array == null) {
            return vm.getNull();
        }
        JavaWrapper[] resultingArray = new JavaWrapper[array.length];
        for (int i = 0; i < array.length; i++) {
            resultingArray[i] = vm.newBoolean(array[i]);
        }
        return JavaWrapper.createArray(JavaClass.forName(vm, "[Z"), resultingArray);
    }

    public static JavaWrapper convertByteArray(VirtualMachine vm, byte[] array) {
        if (array == null) {
            return vm.getNull();
        }
        JavaWrapper[] resultingArray = new JavaWrapper[array.length];
        for (int i = 0; i < array.length; i++) {
            resultingArray[i] = vm.newByte(array[i]);
        }
        return JavaWrapper.createArray(JavaClass.forName(vm, "[B"), resultingArray);
    }

    public static JavaWrapper convertCharArray(VirtualMachine vm, char[] array) {
        if (array == null) {
            return vm.getNull();
        }
        JavaWrapper[] resultingArray = new JavaWrapper[array.length];
        for (int i = 0; i < array.length; i++) {
            resultingArray[i] = vm.newChar(array[i]);
        }
        return JavaWrapper.createArray(JavaClass.forName(vm, "[C"), resultingArray);
    }

    public static JavaWrapper convertShortArray(VirtualMachine vm, short[] array) {
        if (array == null) {
            return vm.getNull();
        }
        JavaWrapper[] resultingArray = new JavaWrapper[array.length];
        for (int i = 0; i < array.length; i++) {
            resultingArray[i] = vm.newShort(array[i]);
        }
        return JavaWrapper.createArray(JavaClass.forName(vm, "[S"), resultingArray);
    }

    public static JavaWrapper convertIntArray(VirtualMachine vm, int[] array) {
        if (array == null) {
            return vm.getNull();
        }
        JavaWrapper[] resultingArray = new JavaWrapper[array.length];
        for (int i = 0; i < array.length; i++) {
            resultingArray[i] = JavaWrapper.createInteger(vm, array[i]);
        }
        return JavaWrapper.createArray(JavaClass.forName(vm, "[I"), resultingArray);
    }

    public static JavaWrapper convertLongArray(VirtualMachine vm, long[] array) {
        if (array == null) {
            return vm.getNull();
        }
        JavaWrapper[] resultingArray = new JavaWrapper[array.length];
        for (int i = 0; i < array.length; i++) {
            resultingArray[i] = vm.newLong(array[i]);
        }
        return JavaWrapper.createArray(JavaClass.forName(vm, "[J"), resultingArray);
    }

    public static JavaWrapper convertFloatArray(VirtualMachine vm, float[] array) {
        if (array == null) {
            return vm.getNull();
        }
        JavaWrapper[] resultingArray = new JavaWrapper[array.length];
        for (int i = 0; i < array.length; i++) {
            resultingArray[i] = JavaWrapper.createFloat(vm, array[i]);
        }
        return JavaWrapper.createArray(JavaClass.forName(vm, "[F"), resultingArray);
    }

    public static JavaWrapper convertDoubleArray(VirtualMachine vm, double[] array) {
        if (array == null) {
            return vm.getNull();
        }
        JavaWrapper[] resultingArray = new JavaWrapper[array.length];
        for (int i = 0; i < array.length; i++) {
            resultingArray[i] = JavaWrapper.createDouble(vm, array[i]);
        }
        return JavaWrapper.createArray(JavaClass.forName(vm, "[D"), resultingArray);
    }

    public static JavaWrapper convertObjectArray(VirtualMachine vm, Object[] array) {
        if (array == null) {
            return vm.getNull();
        }
        JavaWrapper[] resultingArray = new JavaWrapper[array.length];
        for (int i = 0; i < array.length; i++) {
            resultingArray[i] = convert(vm, array[i]);
        }
        return JavaWrapper.createArray(JavaClass.forName(vm, Type.getType(array.getClass())), resultingArray);
    }

    public static byte[] convertByteArray(JavaArray array) {
        byte[] resultingArray = new byte[array.length()];
        for (int i = 0; i < array.length(); i++) {
            resultingArray[i] = array.get(i).asByte();
        }
        return resultingArray;
    }

    public static char[] convertCharArray(JavaArray array) {
        char[] resultingArray = new char[array.length()];
        for (int i = 0; i < array.length(); i++) {
            resultingArray[i] = array.get(i).asChar();
        }
        return resultingArray;
    }

    public static int[] convertIntArray(JavaArray array) {
        int[] resultingArray = new int[array.length()];
        for (int i = 0; i < array.length(); i++) {
            resultingArray[i] = array.get(i).asInt();
        }
        return resultingArray;
    }
}
