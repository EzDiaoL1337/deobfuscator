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

public class java_lang_String {
    private static final String THIS = "java/lang/String";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "intern", "()Ljava/lang/String;", false, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            return vm.intern(inst);
        }));
    }

    public static boolean isInstance(VirtualMachine vm, JavaWrapper oop) {
        return oop != null && oop.getJavaClass() == vm.getSystemDictionary().getJavaLangString();
    }
}
