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
import com.diaoling.javavm.values.JavaWrapper;

public class java_lang_Double {
    private static final String THIS = "java/lang/Double";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "doubleToRawLongBits", "(D)J", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return vm.newLong(Double.doubleToRawLongBits(args[0].asDouble()));
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "longBitsToDouble", "(J)D", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return JavaWrapper.createDouble(vm, Double.longBitsToDouble(args[0].asLong()));
        }));
    }
}
