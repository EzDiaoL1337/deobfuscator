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
import com.diaoling.javavm.hooks.HookGenerator;
import com.diaoling.javavm.internals.VMSymbols;
import com.diaoling.javavm.oops.ThreadOop;
import com.diaoling.javavm.values.JavaWrapper;

public class java_lang_Thread {
    private static final String THIS = "java/lang/Thread";

    public static ThreadOop getThreadOop(JavaWrapper thread) {
        return thread.get().getMetadata("oop");
    }

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "currentThread", "()Ljava/lang/Thread;", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return ThreadOop.forThread(Thread.currentThread()).getThread();
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "setPriority0", "(I)V", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            getThreadOop(inst).setPriority(args[0].asInt());
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "isAlive", "()Z", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return vm.newBoolean(getThreadOop(inst).isAlive());
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "start0", "()V", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            getThreadOop(inst).start();
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "yield", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            ThreadOop.forThread(Thread.currentThread()).yield();
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "sleep", "(J)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            try {
                Thread.sleep(args[0].asLong());
            } catch (InterruptedException e) {
                throw vm.newThrowable(VMSymbols.java_lang_InterruptedExecption);
            }
        }));
    }
}
