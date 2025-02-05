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

package com.diaoling.javavm.nativeimpls;

import com.diaoling.javavm.Cause;
import com.diaoling.javavm.Effect;
import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.hooks.HookGenerator;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.utils.ASMHelper;
import com.diaoling.javavm.utils.PrimitiveUtils;
import com.diaoling.javavm.values.JavaArray;
import com.diaoling.javavm.values.JavaObject;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class sun_reflect_NativeMethodAccessorImpl {
    private static final String THIS = "sun/reflect/NativeMethodAccessorImpl";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "invoke0", "(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", true, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            JavaObject method = ((JavaObject) args[0].get());
            JavaWrapper classObj = method.getField("clazz", "Ljava/lang/Class;");
            JavaClass clazz = java_lang_Class.getJavaClass(classObj);
            ClassNode classNode = clazz.getClassNode();
            MethodNode methodNode = clazz.getClassNode().methods.get(method.getField("slot", "I").asInt());

            Type[] types = Type.getArgumentTypes(methodNode.desc);
            JavaWrapper[] ctorargs = new JavaWrapper[types.length];

            if (!args[2].is(JavaValueType.NULL)) {
                JavaArray providedArgs = ((JavaArray) args[2].get());
                for (int i = 0; i < types.length; i++) {
                    JavaWrapper wrapper = providedArgs.get(i);
                    if (wrapper.is(JavaValueType.PRIMITIVE)) {
                        ctorargs[i] = wrapper;
                    } else {
                        switch (types[i].getSort()) {
                            case Type.BOOLEAN:
                                ctorargs[i] = ((JavaObject) wrapper.get()).getField("value", "Z");
                                break;
                            case Type.CHAR:
                                ctorargs[i] = ((JavaObject) wrapper.get()).getField("value", "C");
                                break;
                            case Type.BYTE:
                                ctorargs[i] = ((JavaObject) wrapper.get()).getField("value", "B");
                                break;
                            case Type.SHORT:
                                ctorargs[i] = ((JavaObject) wrapper.get()).getField("value", "S");
                                break;
                            case Type.INT:
                                ctorargs[i] = ((JavaObject) wrapper.get()).getField("value", "I");
                                break;
                            case Type.FLOAT:
                                ctorargs[i] = ((JavaObject) wrapper.get()).getField("value", "F");
                                break;
                            case Type.LONG:
                                ctorargs[i] = ((JavaObject) wrapper.get()).getField("value", "J");
                                break;
                            case Type.DOUBLE:
                                ctorargs[i] = ((JavaObject) wrapper.get()).getField("value", "D");
                                break;
                            case Type.ARRAY:
                            case Type.OBJECT:
                                ctorargs[i] = wrapper;
                                break;
                        }
                    }
                }
            }
            if (!args[1].is(JavaValueType.NULL)) {
                classNode = args[1].getJavaClass().getClassNode();
                ClassNode now = classNode;
                MethodNode newNode = ASMHelper.findMethod(classNode, methodNode.name, methodNode.desc);
                while (newNode == null) {
                    now = vm.lookupClass(now.superName);
                    if (now == null) {
                        break;
                    }
                    newNode = ASMHelper.findMethod(classNode, methodNode.name, methodNode.desc);
                }
                if (newNode != null) {
                    methodNode = newNode;
                }
            }
            Type expectedReturnType = Type.getReturnType(methodNode.desc);
            JavaWrapper result = vm.internalExecute(classNode, methodNode, args[1], ctorargs, vm.currentInsn.get(Thread.currentThread()));
            if (result == null) {
                if (expectedReturnType.getSort() == Type.VOID) {
                    return vm.getNull();
                } else {
                    throw new ExecutionException("No return value for non-void method");
                }
            } else {
                // we need to box the result if it's a primitive
                if (PrimitiveUtils.isPrimitive(expectedReturnType)) {
                    switch (expectedReturnType.getSort()) {
                        case Type.BOOLEAN:
                            result = vm.newBoxedBoolean(result.asPrimitive().asBoolean());
                            break;
                        case Type.BYTE:
                            result = vm.newBoxedByte(result.asByte());
                            break;
                        case Type.CHAR:
                            result = vm.newBoxedChar(result.asChar());
                            break;
                        case Type.SHORT:
                            result = vm.newBoxedShort(result.asShort());
                            break;
                        case Type.INT:
                            result = vm.newBoxedInt(result.asInt());
                            break;
                        case Type.FLOAT:
                            result = vm.newBoxedFloat(result.asFloat());
                            break;
                        case Type.LONG:
                            result = vm.newBoxedLong(result.asLong());
                            break;
                        case Type.DOUBLE:
                            result = vm.newBoxedDouble(result.asDouble());
                            break;
                    }
                }
            }
            return result;
        }));
    }
}
