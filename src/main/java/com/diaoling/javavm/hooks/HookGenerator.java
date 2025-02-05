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

package com.diaoling.javavm.hooks;

import com.diaoling.javavm.Cause;
import com.diaoling.javavm.Effect;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.internals.VMSymbols;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.utils.ExecutionUtils;
import com.diaoling.javavm.values.JavaUnknown;
import com.diaoling.javavm.values.JavaValue;
import com.diaoling.javavm.values.JavaWrapper;
import com.diaoling.javavm.values.prim.JDouble;
import com.diaoling.javavm.values.prim.JFloat;
import com.diaoling.javavm.values.prim.JInteger;
import com.diaoling.javavm.values.prim.JLong;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Supplier;

public class HookGenerator {
    public static HookedMethod generateNopHook(String owner, String name, String desc) {
        Type type = Type.getReturnType(desc);
        if (type.getSort() != Type.VOID)
            throw new UnsupportedOperationException("Cannot nop hook a method which doesn't return void");

        return new HookedMethod(owner, name, desc, Cause.NONE, Effect.NONE).bind((context, instance, args) -> {

        });
    }

    public static HookedMethod generateUnknownHandlingVoidHook(VirtualMachine vm, String owner, String name, String desc, boolean isStatic, Cause cause, Effect effect, HookedMethod.VoidHook hook) {
        Type returnType = Type.getReturnType(desc);
        if (returnType.getSort() != Type.VOID)
            throw new UnsupportedOperationException("Cannot nop hook a method which doesn't return void");

        return new HookedMethod(owner, name, desc, cause, effect).bind((ctx, inst, args) -> {
            if (!isStatic && inst == null) {
                throw vm.newThrowable(VMSymbols.java_lang_NullPointerException);
            }
            if (ExecutionUtils.areValuesUnknown(inst, args)) {
                //todo log
                return;
            }
            hook.execute(ctx, inst, args);
        });
    }

    public static HookedMethod generateUnknownHandlingHook(VirtualMachine vm, String owner, String name, String desc, boolean isStatic, Cause cause, Effect effect, HookedMethod.Hook hook) {
        Type returnType = Type.getReturnType(desc);
        if (returnType.getSort() == Type.VOID)
            throw new UnsupportedOperationException("Cannot nop hook a method which doesn't return void");

        JavaClass returnClass = JavaClass.forName(vm, returnType);
        Type[] argumentTypes = Type.getArgumentTypes(desc);

        return new HookedMethod(owner, name, desc, cause, effect).bind((ctx, inst, args) -> {
            if (!isStatic && inst == null) {
                throw vm.newThrowable(VMSymbols.java_lang_NullPointerException);
            }
            if (ExecutionUtils.areValuesUnknown(inst, args)) {
                return JavaWrapper.wrap(new JavaUnknown(vm, returnClass, "Calling " + name + " on " + inst + " " + Arrays.toString(args)));
            }

            return hook.execute(ctx, inst, args);
        });
    }

    public static HookedMethod generateDefaultingHook(Method method, Cause cause, Effect effect) {
        return generateDefaultingHook(method, cause, effect, null);
    }

    public static HookedMethod generateDefaultingHook(Method method, Cause cause, Effect effect, Supplier<JavaWrapper> value) {
        Class<?> srcClass = method.getDeclaringClass();
        String internalName = Type.getInternalName(srcClass);

        HookedMethod hookedMethod = new HookedMethod(internalName, method.getName(), Type.getMethodDescriptor(method), cause, effect);

        if (method.getReturnType() == void.class) {
            hookedMethod.bind((context, instance, args) -> {

            });
        } else {
            hookedMethod.bind((context, instance, args) -> {
                return value.get();
            });
        }

        return hookedMethod;
    }

    //    private static JavaValue convertArray(MethodExecution context, Class<?> retType, Object retVal) {
//        JavaClass mirror = JavaClass.forName(context.getVM(), Type.getType(retType));
//
//        if (mirror.internalGetType().getDimensions() != 1) {
//            throw new ExecutionException("");
//        }
//
//        if (retType == byte[].class) {
//            return new JavaObject(ExecutionUtils.convert((byte[]) retVal), mirror, mirror.internalGetType().getDescriptor());
//        } else if (retType == char[].class) {
//            return new JavaObject(ExecutionUtils.convert((char[]) retVal), mirror, mirror.internalGetType().getDescriptor());
//        }
//
//        return new JavaObject(ExecutionUtils.convert((Object[]) retVal, mirror.getComponentType(), mirror.getComponentType().internalGetType().getInternalName()), mirror, mirror.internalGetType().getDescriptor());
//    }
//
    private static JavaValue convertObject(MethodExecution execution, Class<?> retType, Object retVal) {
        JavaClass mirror = JavaClass.forName(execution.getVM(), Type.getType(retType));
        if (retType == int.class) {
            return new JInteger(execution.getVM(), (int) retVal);
        } else if (retType == char.class) {
            return execution.getVM().newChar((char) retVal).get();
        } else if (retType == byte.class) {
            return execution.getVM().newByte((byte) retVal).get();
        } else if (retType == short.class) {
            return execution.getVM().newShort((short) retVal).get();
        } else if (retType == boolean.class) {
            return ((boolean) retVal) ? execution.getVM().TRUE.get() : execution.getVM().FALSE.get();
        } else if (retType == float.class) {
            return new JFloat(execution.getVM(), (float) retVal);
        } else if (retType == long.class) {
            return new JLong(execution.getVM(), (long) retVal);
        } else if (retType == double.class) {
            return new JDouble(execution.getVM(), (double) retVal);
        } else if (retVal == null) {
            return execution.getVM().getNull().get();
        } else if (retType.isArray()) {
//            return convertArray(context, retType, retVal);
        } else {
//            return new JavaObject(retVal, mirror, mirror.internalGetType().getInternalName());
        }

        throw new ExecutionException("Unsupported " + retType + " " + retVal);
    }
}
