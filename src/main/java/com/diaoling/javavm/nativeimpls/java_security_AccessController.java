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
import com.diaoling.javavm.utils.ASMHelper;
import com.diaoling.javavm.values.JavaObject;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.MethodNode;

public class java_security_AccessController {
    private static final String THIS = "java/security/AccessController";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "doPrivileged", "(Ljava/security/PrivilegedExceptionAction;)Ljava/lang/Object;", true, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            JavaObject obj = ((JavaObject) args[0].get());
            MethodNode methodNode = ASMHelper.findMethod(obj.getJavaClass().getClassNode(), "run", "()Ljava/lang/Object;");
            if (methodNode == null) {
                throw new ExecutionException("unexpected null method " + obj + " " + obj.getJavaClass());
            }
            // todo oops
            return vm.internalExecute(obj.getJavaClass().getClassNode(), methodNode, args[0], new JavaWrapper[0], vm.currentInsn.get(Thread.currentThread()));
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "doPrivileged", "(Ljava/security/PrivilegedAction;)Ljava/lang/Object;", true, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            JavaObject obj = ((JavaObject) args[0].get());
            MethodNode methodNode = ASMHelper.findMethod(obj.getJavaClass().getClassNode(), "run", "()Ljava/lang/Object;");
            if (methodNode == null) {
                throw new ExecutionException("unexpected null method " + obj + " " + obj.getJavaClass());
            }
            // todo oops
            return vm.internalExecute(obj.getJavaClass().getClassNode(), methodNode, args[0], new JavaWrapper[0], vm.currentInsn.get(Thread.currentThread()));
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "doPrivileged", "(Ljava/security/PrivilegedAction;Ljava/security/AccessControlContext;)Ljava/lang/Object;", true, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            JavaObject obj = ((JavaObject) args[0].get());
            MethodNode methodNode = ASMHelper.findMethod(obj.getJavaClass().getClassNode(), "run", "()Ljava/lang/Object;");
            if (methodNode == null) {
                throw new ExecutionException("unexpected null method " + obj + " " + obj.getJavaClass());
            }
            // todo oops
            return vm.internalExecute(obj.getJavaClass().getClassNode(), methodNode, args[0], new JavaWrapper[0], vm.currentInsn.get(Thread.currentThread()));
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "doPrivileged", "(Ljava/security/PrivilegedExceptionAction;Ljava/security/AccessControlContext;)Ljava/lang/Object;", true, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            JavaObject obj = ((JavaObject) args[0].get());
            MethodNode methodNode = ASMHelper.findMethod(obj.getJavaClass().getClassNode(), "run", "()Ljava/lang/Object;");
            if (methodNode == null) {
                throw new ExecutionException("unexpected null method " + obj + " " + obj.getJavaClass());
            }
            // todo oops
            return vm.internalExecute(obj.getJavaClass().getClassNode(), methodNode, args[0], new JavaWrapper[0], vm.currentInsn.get(Thread.currentThread()));
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "getStackAccessControlContext", "()Ljava/security/AccessControlContext;", true, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            return vm.getNull();
        }));
    }
}
