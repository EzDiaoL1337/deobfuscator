/* java.lang.reflect.Array - manipulate arrays by reflection
   Copyright (C) 1998, 1999, 2001, 2003, 2005  Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package com.diaoling.javavm.utils;

import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;

/**
 * Array holds static helper functions that allow you to create and
 * manipulate arrays by reflection. Operations know how to perform widening
 * conversions, but throw {@link IllegalArgumentException} if you attempt
 * a narrowing conversion. Also, when accessing primitive arrays, this
 * class performs object wrapping and unwrapping as necessary.<p>
 * <p>
 * <B>Note:</B> This class returns and accepts types as Classes, even
 * primitive types; there are Class types defined that represent each
 * different primitive type.  They are <code>java.lang.Boolean.TYPE,
 * java.lang.Byte.TYPE,</code>, also available as <code>boolean.class,
 * byte.class</code>, etc.  These are not to be confused with the
 * classes <code>java.lang.Boolean, java.lang.Byte</code>, etc., which are
 * real classes. Note also that the shorthand <code>Object[].class</code>
 * is a convenient way to get array Classes.<p>
 * <p>
 * <B>Performance note:</B> This class performs best when it does not have
 * to convert primitive types.  The further along the chain it has to convert,
 * the worse performance will be.  You're best off using the array as whatever
 * type it already is, and then converting the result.  You will do even
 * worse if you do this and use the generic set() function.
 *
 * @author John Keiser
 * @author Eric Blake (ebb9@email.byu.edu)
 * @author Per Bothner (bothner@cygnus.com)
 * @status updated to 1.4
 * @see java.lang.Boolean#TYPE
 * @see java.lang.Byte#TYPE
 * @see java.lang.Short#TYPE
 * @see java.lang.Character#TYPE
 * @see java.lang.Integer#TYPE
 * @see java.lang.Long#TYPE
 * @see java.lang.Float#TYPE
 * @see java.lang.Double#TYPE
 * @since 1.1
 */
public final class ArrayHelper {

    // todo just create it
    public static JavaWrapper newInstance(VirtualMachine vm, Type type, int length) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
                return ArrayConversionHelper.convertBooleanArray(vm, new boolean[length]);
            case Type.BYTE:
                return ArrayConversionHelper.convertByteArray(vm, new byte[length]);
            case Type.CHAR:
                return ArrayConversionHelper.convertCharArray(vm, new char[length]);
            case Type.SHORT:
                return ArrayConversionHelper.convertShortArray(vm, new short[length]);
            case Type.INT:
                return ArrayConversionHelper.convertIntArray(vm, new int[length]);
            case Type.LONG:
                return ArrayConversionHelper.convertLongArray(vm, new long[length]);
            case Type.FLOAT:
                return ArrayConversionHelper.convertFloatArray(vm, new float[length]);
            case Type.DOUBLE:
                return ArrayConversionHelper.convertDoubleArray(vm, new double[length]);
            case Type.OBJECT:
            case Type.ARRAY:
                return JavaWrapper.createArray(JavaClass.forName(vm, "[" + type.getDescriptor()), new JavaWrapper[length]);
        }
        // assert componentType == void.class
        throw new ExecutionException("Unexpected " + type);
    }

    private static JavaWrapper createMultiArray(VirtualMachine vm, Type type, int[] dimensions, int index) {
        JavaWrapper retval = newInstance(vm, Type.getType(type.getDescriptor().substring(1)), dimensions[index]);
        if (index == dimensions.length - 1)
            return retval;
        for (int i = 0; i < dimensions[index]; i++) {
            retval.asArray().set(i, createMultiArray(vm, Type.getType(type.getDescriptor().substring(1)), dimensions, index + 1));
        }
        return retval;
    }

    public static JavaWrapper newInstance(VirtualMachine vm, Type componentType, int[] dimensions) {
        if (dimensions.length <= 0)
            throw new IllegalArgumentException("Empty dimensions array.");
        return createMultiArray(vm, componentType, dimensions, 0);
    }
}