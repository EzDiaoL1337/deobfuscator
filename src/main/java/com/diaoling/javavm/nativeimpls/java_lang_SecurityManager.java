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
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaWrapper;

import java.util.List;

public class java_lang_SecurityManager {

    private final VirtualMachine _vm;

    public java_lang_SecurityManager(VirtualMachine vm) {
        _vm = vm;
        registerNatives();
    }

    private void registerNatives() {
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/lang/SecurityManager", "getClassContext", "()[Ljava/lang/Class;", false, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            List<StackTraceHolder> stacktrace = _vm.getStacktrace();
            stacktrace.remove(0);

            JavaWrapper array = JavaWrapper.createArray(JavaClass.forName(_vm, "[Ljava/lang/Class;"), new JavaWrapper[stacktrace.size()]);
            // todo remove hidden frames like NativeMethodAccessorImpl
            for (int i = 0; i < stacktrace.size(); i++) {
                StackTraceHolder holder = stacktrace.get(i);
                array.asArray().set(i, JavaClass.forName(_vm, holder.getClassNode().name).getOop());
            }

            return array;
        }));
    }
}
