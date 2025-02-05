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

public class java_lang_Float {
    private static final String THIS = "java/lang/Float";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "floatToRawIntBits", "(F)I", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return JavaWrapper.createInteger(vm, Float.floatToRawIntBits(args[0].asFloat()));
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "intBitsToFloat", "(F)I", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return JavaWrapper.createFloat(vm, Float.intBitsToFloat(args[0].asInt()));
        }));
    }
}
