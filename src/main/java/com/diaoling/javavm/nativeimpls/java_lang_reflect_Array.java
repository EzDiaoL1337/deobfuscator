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
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;

public class java_lang_reflect_Array {
    private static final String THIS = "java/lang/reflect/Array";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "newArray", "(Ljava/lang/Class;I)Ljava/lang/Object;", true, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            JavaClass type = java_lang_Class.getJavaClass(args[0]);
            return JavaWrapper.createArray(JavaClass.forName(vm, Type.getType("[" + type.internalGetType().getDescriptor())), new JavaWrapper[args[1].asInt()]);
        }));
    }
}
