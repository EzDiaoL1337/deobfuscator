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

public class sun_misc_Perf {
    private final VirtualMachine _vm;

    public sun_misc_Perf(VirtualMachine vm) {
        _vm = vm;
        registerNatives();
    }

    private void registerNatives() {
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "sun/misc/Perf", "registerNatives", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            registerNatives0();
        }));
    }

    private void registerNatives0() {
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "sun/misc/Perf", "createLong", "(Ljava/lang/String;IIJ)Ljava/nio/ByteBuffer;", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            long address = _vm.getMemory().allocateMemory(8);
            return _vm.newInstance(JavaClass.forName(_vm, "java/nio/DirectByteBuffer"), "(JI)V", _vm.newLong(address), _vm.newInt(8));
        }));
    }
}
