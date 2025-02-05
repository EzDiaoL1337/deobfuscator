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
import com.diaoling.javavm.StackTraceHolder;
import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.hooks.HookGenerator;
import com.diaoling.javavm.internals.VMSymbols;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaWrapper;

import java.util.List;

public class sun_reflect_Reflection {
    private static final String THIS = "sun/reflect/Reflection";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "getCallerClass", "()Ljava/lang/Class;", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            List<StackTraceHolder> stacktrace = vm.getStacktrace();
            if (stacktrace.size() < 2) {
                return vm.getNull();
            }

            StackTraceHolder shouldBeReflection = stacktrace.get(0);
            if (!shouldBeReflection.getClassNode().name.equals(THIS) || !shouldBeReflection.getMethod().name.equals("getCallerClass") || !shouldBeReflection.getMethod().desc.equals("()Ljava/lang/Class;")) {
                throw vm.newThrowable(VMSymbols.java_lang_InternalError, "JVM_GetCallerClass must only be called from Reflection.getCallerClass");
            }
//            if (!shouldBeReflection.getMethod().isCallerSensitive()) {
//                throw new WrappedException(vm.newInternalError("CallerSensitive annotation expected at frame 0"));
//            }
            StackTraceHolder shouldBeCallerSensitive = stacktrace.get(1);
//            if (!shouldBeCallerSensitive.getMethod().isCallerSensitive()) {
//                throw new WrappedException(vm.newInternalError("CallerSensitive annotation expected at frame 0"));
//            }
            // todo is_ignored_by_security_walk
            return JavaClass.forName(vm, stacktrace.get(2).getClassNode().name).getOop();
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "getCallerClass", "(I)Ljava/lang/Class;", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            List<StackTraceHolder> stacktrace = vm.getStacktrace();
            if (stacktrace.size() <= args[0].asInt()) {
                return vm.getNull();
            }
            return JavaClass.forName(vm, stacktrace.get(args[0].asInt()).getClassNode().name).getOop();
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "getClassAccessFlags", "(Ljava/lang/Class;)I", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            return JavaWrapper.createInteger(vm, java_lang_Class.getJavaClass(args[0]).getModifiers());
        }));
    }
}
