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
import com.diaoling.javavm.hooks.HookedMethod;
import com.diaoling.javavm.mirrors.JavaMethod;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Modifier;

public class java_lang_invoke_MethodHandle {

    private final VirtualMachine _vm;

    public java_lang_invoke_MethodHandle(VirtualMachine vm) {
        _vm = vm;
        registerNatives();
    }

    public static JavaWrapper getLambdaForm(JavaWrapper methodHandle) {
        return methodHandle.asObject().getField("form", "Ljava/lang/invoke/LambdaForm;");
    }

    public static JavaWrapper getVMEntry(JavaWrapper lambdaForm) {
        return lambdaForm.asObject().getField("vmentry", "Ljava/lang/invoke/MemberName;");
    }

    private void registerNatives() {
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/lang/invoke/MethodHandle", "invoke", "([Ljava/lang/Object;)Ljava/lang/Object;", false, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            JavaWrapper type = inst.asObject().getField("type", "Ljava/lang/invoke/MethodType;");
            JavaWrapper form = inst.asObject().getField("form", "Ljava/lang/invoke/LambdaForm;");
            JavaWrapper vmentry = form.asObject().getField("vmentry", "Ljava/lang/invoke/MemberName;");
            JavaMethod target = (JavaMethod) java_lang_invoke_MemberName.get_vmtarget(vmentry);

            JavaWrapper instance = args.length > 0 ? args[0] : null;
            JavaWrapper[] params = new JavaWrapper[0];
            if (args.length > 1) {
                params = new JavaWrapper[args.length - 1];
                System.arraycopy(args, 1, params, 0, params.length);
            }

            throw new ExecutionException("unsupported");
        }));
    }

    public void hookPolymorphicSignature(MethodNode methodNode) {
        if (methodNode.name.equals("invoke") || methodNode.name.equals("invokeBasic") || methodNode.name.equals("invokeExact")) {
            Type methodType = Type.getMethodType(methodNode.desc);
            Type[] mArgs = methodType.getArgumentTypes();
            Type ret = methodType.getReturnType();

            HookedMethod.Hook hook = (ctx, inst, args) -> {
                JavaWrapper type = inst.asObject().getField("type", "Ljava/lang/invoke/MethodType;");
                JavaWrapper form = inst.asObject().getField("form", "Ljava/lang/invoke/LambdaForm;");
                JavaWrapper vmentry = form.asObject().getField("vmentry", "Ljava/lang/invoke/MemberName;");
                JavaMethod target = (JavaMethod) java_lang_invoke_MemberName.get_vmtarget(vmentry);

                JavaWrapper instance = args.length > 0 ? args[0] : null;
                JavaWrapper[] params = new JavaWrapper[0];
                if (args.length > 1) {
                    params = new JavaWrapper[args.length - 1];
                    System.arraycopy(args, 1, params, 0, params.length);
                }

                if (target.getClassNode().name.startsWith("java/lang/invoke/LambdaForm$")) {
                    // ???
                    instance = inst;
                    params = new JavaWrapper[args.length + 1];
                    System.arraycopy(args, 0, params, 1, args.length);
                    params[0] = inst;
                }

//                System.out.println("vmentry: " + vmentry);
//                System.out.println("instance: " + instance);
//                System.out.println("params: " + params.length);
//                for (JavaWrapper param : params) {
//                    System.out.println(param);
//                }

                JavaWrapper result = _vm.internalExecute(target.getClassNode(), target.getMethodNode(), instance, params, _vm.currentInsn.get(Thread.currentThread()));
                return result;
            };

            if (ret.getSort() == Type.VOID) {
                _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "java/lang/invoke/MethodHandle", methodNode.name, methodNode.desc, Modifier.isStatic(methodNode.access), Cause.ALL, Effect.ALL, hook::execute));
            } else {
                _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/lang/invoke/MethodHandle", methodNode.name, methodNode.desc, Modifier.isStatic(methodNode.access), Cause.ALL, Effect.ALL, hook));
            }
        } else if (methodNode.name.equals("linkToStatic")) {
            Type methodType = Type.getMethodType(methodNode.desc);
            Type[] mArgs = methodType.getArgumentTypes();
            Type ret = methodType.getReturnType();

            HookedMethod.Hook hook = (ctx, inst, args) -> {
                JavaMethod target = (JavaMethod) java_lang_invoke_MemberName.get_vmtarget(args[args.length - 1]);

                JavaWrapper[] params = new JavaWrapper[0];
                if (args.length > 1) {
                    params = new JavaWrapper[args.length - 1];
                    System.arraycopy(args, 0, params, 0, params.length);
                }
//                System.out.println("linkTostatic: " + target);
//                System.out.println("params: " + params.length);
//                for (JavaWrapper param : params) {
//                    System.out.println(param);
//                }

                return _vm.internalExecute(target.getClassNode(), target.getMethodNode(), null, params, _vm.currentInsn.get(Thread.currentThread()));
            };

            if (ret.getSort() == Type.VOID) {
                _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "java/lang/invoke/MethodHandle", methodNode.name, methodNode.desc, Modifier.isStatic(methodNode.access), Cause.ALL, Effect.ALL, hook::execute));
            } else {
                _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/lang/invoke/MethodHandle", methodNode.name, methodNode.desc, Modifier.isStatic(methodNode.access), Cause.ALL, Effect.ALL, hook));
            }
        } else if (methodNode.name.equals("linkToVirtual")) {
            Type methodType = Type.getMethodType(methodNode.desc);
            Type[] mArgs = methodType.getArgumentTypes();
            Type ret = methodType.getReturnType();

            HookedMethod.Hook hook = (ctx, inst, args) -> {
                JavaMethod target = (JavaMethod) java_lang_invoke_MemberName.get_vmtarget(args[args.length - 1]);

                JavaWrapper[] params = new JavaWrapper[0];
                if (args.length > 1) {
                    params = new JavaWrapper[args.length - 2];
                    System.arraycopy(args, 1, params, 0, params.length);
                }
//                System.out.println("linkToVirtual: " + target);
//                System.out.println("instance: " + args[0]);
//                System.out.println("params: " + params.length);
//                for (JavaWrapper param : params) {
//                    System.out.println(param);
//                }

                return _vm.internalExecute(target.getClassNode(), target.getMethodNode(), args[0], params, _vm.currentInsn.get(Thread.currentThread()));
            };

            if (ret.getSort() == Type.VOID) {
                _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "java/lang/invoke/MethodHandle", methodNode.name, methodNode.desc, Modifier.isStatic(methodNode.access), Cause.ALL, Effect.ALL, hook::execute));
            } else {
                _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/lang/invoke/MethodHandle", methodNode.name, methodNode.desc, Modifier.isStatic(methodNode.access), Cause.ALL, Effect.ALL, hook));
            }
        }
    }
}
