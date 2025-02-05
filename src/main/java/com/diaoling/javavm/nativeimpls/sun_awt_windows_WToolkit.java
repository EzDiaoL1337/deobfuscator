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

public class sun_awt_windows_WToolkit {
    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, "sun/awt/windows/WToolkit", "initIDs", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, "sun/awt/windows/WToolkit", "startToolkitThread", "(Ljava/lang/Runnable;Ljava/lang/ThreadGroup;)Z", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            // stub
            args[0].asObject().setField("inited", "Z", vm.newBoolean(true));
            return vm.newBoolean(true);
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, "sun/awt/windows/WToolkit", "setDynamicLayoutNative", "(Z)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            // stub
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, "sun/awt/windows/WToolkit", "setExtraMouseButtonsEnabledNative", "(Z)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            // stub
        }));
    }
}
