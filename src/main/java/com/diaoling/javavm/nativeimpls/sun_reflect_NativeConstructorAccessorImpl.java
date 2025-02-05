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
import com.diaoling.javavm.hooks.HookedMethod;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.utils.ExecutionUtils;
import com.diaoling.javavm.values.*;
import com.diaoling.javavm.values.*;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

public class sun_reflect_NativeConstructorAccessorImpl {
    private static final String THIS = "sun/reflect/NativeMethodAccessorImpl";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(new HookedMethod("sun/reflect/NativeConstructorAccessorImpl", "newInstance0", "(Ljava/lang/reflect/Constructor;[Ljava/lang/Object;)Ljava/lang/Object;", Cause.NONE, Effect.NONE).bind((ctx, inst, args) -> {
            JavaWrapper arg0 = args[0];
            JavaWrapper arg1 = args[1];
            if (ExecutionUtils.areValuesUnknown(arg0, arg1)) {
                return JavaWrapper.wrap(new JavaUnknown(vm, vm.getSystemDictionary().getJavaLangString(), "forName0 on " + arg0 + " " + arg1));
            }

            JavaObject constructor = ((JavaObject) arg0.get());
            JavaClass declaringClass = java_lang_Class.getJavaClass(constructor.getField("clazz", "Ljava/lang/Class;"));
            MethodNode constructorNode = declaringClass.getClassNode().methods.get(constructor.getField("slot", "I").asInt());

            Type[] types = Type.getArgumentTypes(constructorNode.desc);
            JavaWrapper[] ctorargs = new JavaWrapper[types.length];

            if (!arg1.is(JavaValueType.NULL)) {
                JavaArray providedArgs = ((JavaArray) arg1.get());
                for (int i = 0; i < types.length; i++) {
                    JavaWrapper wrapper = providedArgs.get(i);
                    if (!wrapper.is(JavaValueType.NULL) && wrapper.getJavaClass().isPrimitive()) {
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
                            case Type.OBJECT:
                            case Type.ARRAY:
                                ctorargs[i] = wrapper;
                                break;
                        }
                    }
                }
            }

            return vm.newInstance(declaringClass, constructorNode.desc, ctorargs);
        }));
    }
}
