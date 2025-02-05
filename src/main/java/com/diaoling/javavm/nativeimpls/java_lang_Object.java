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
import com.diaoling.javavm.internals.VMSymbols;
import com.diaoling.javavm.oops.ThreadOop;
import com.diaoling.javavm.utils.ExecutionUtils;
import com.diaoling.javavm.values.*;
import com.diaoling.javavm.values.*;

import java.util.concurrent.TimeUnit;

public class java_lang_Object {
    private static final String THIS = "java/lang/Object";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "getClass", "()Ljava/lang/Class;", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return inst.getJavaClass().getOop();
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "hashCode", "()I", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return vm.newInt(inst.asObject().getHashCode());
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "clone", "()Ljava/lang/Object;", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            if (!inst.getJavaClass().isArray()) {
                if (!vm.getSystemDictionary().getJavalangCloneable().isAssignableFrom(inst.getJavaClass())) {
                    throw vm.newThrowable(VMSymbols.java_lang_CloneNotSupportedException, inst.getJavaClass().getCanonicalName());
                }
                // todo special handling for membernames?
                // todo register finalizer
                return JavaWrapper.wrap(inst.asObject().vmClone());
            }

            if (inst.is(JavaValueType.ARRAY)) {
                JavaArray old = inst.asArray();
                JavaArray array = new JavaArray(inst.getJavaClass(), new JavaWrapper[old.length()]);
                for (int i = 0; i < old.length(); i++) {
                    array.set(i, old.get(i));
                }

                return JavaWrapper.wrap(array);
            } else {
                throw new ExecutionException("Unexpected");
            }
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "notify", "()V", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            inst.get().getCondition().signal();
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "notifyAll", "()V", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            inst.get().getCondition().signalAll();
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "wait", "(J)V", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            long wait = args[0].asLong();
            try {
                if (wait == 0) {
                    inst.get().getCondition().await();
                } else {
                    inst.get().getCondition().await(wait, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                ThreadOop.forThread(Thread.currentThread()).setInterrupted();
            }
        }));
        vm.hook(new HookedMethod("java/lang/Object", "<init>", "()V", Cause.NONE, Effect.NONE).bind((ctx, inst, args) -> {
            JavaValue arg0 = inst.get();
            if (ExecutionUtils.areValuesUnknown(arg0)) {
                throw new ExecutionException("Unknown value while initializing?!?!");
            }
            if (!(arg0 instanceof JavaUninitialized)) {
                throw new ExecutionException("Cannot call <init> on initialized object: " + arg0);
            }
            JavaUninitialized uninitialized = (JavaUninitialized) arg0;
            if (vm.getSystemDictionary().getJavaLangThread().isAssignableFrom(uninitialized.getJavaClass())) {
                ThreadOop threadOop = new ThreadOop(inst);
                uninitialized.initializedValue().setMetadata("oop", threadOop);
            }
            inst.set(uninitialized.initializedValue());
        }));
    }
}
