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
import com.diaoling.javavm.ConstantPool;
import com.diaoling.javavm.Effect;
import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.hooks.HookGenerator;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaWrapper;

public class sun_reflect_ConstantPool {
    private static final String THIS = "sun/reflect/ConstantPool";

    public static JavaClass getJavaClass(JavaWrapper constantPool) {
        return constantPool.getMetadata("oop");
    }

    public static void setJavaClass(JavaWrapper constantPool, JavaClass javaClass) {
        constantPool.setMetadata("oop", javaClass);
    }

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "getSize0", "(Ljava/lang/Object;)I", false, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            JavaClass javaClass = getJavaClass(inst);
            ConstantPool constantPool = vm.getConstantPool(javaClass.getClassNode());
            if (constantPool == null) {
                throw new ExecutionException("No constantpool found for " + javaClass.getClassNode().name + " " + Integer.toHexString(System.identityHashCode(javaClass.getClassNode())));
            }
            return vm.newInt(constantPool.getSize());
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "getUTF8At0", "(Ljava/lang/Object;I)Ljava/lang/String;", false, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            JavaClass javaClass = getJavaClass(inst);
            ConstantPool constantPool = vm.getConstantPool(javaClass.getClassNode());
            if (constantPool == null) {
                throw new ExecutionException("No constantpool found for " + javaClass.getClassNode().name + " " + Integer.toHexString(System.identityHashCode(javaClass.getClassNode())));
            }
            return vm.getStringInterned(constantPool.getUTF8At(args[1].asInt()));
        }));
    }
}
