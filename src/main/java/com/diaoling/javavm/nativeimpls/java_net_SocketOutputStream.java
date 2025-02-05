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
import com.diaoling.javavm.utils.ArrayConversionHelper;

import java.io.IOException;

public class java_net_SocketOutputStream {
    private final VirtualMachine _vm;

    public java_net_SocketOutputStream(VirtualMachine vm) {
        _vm = vm;
        registerNatives();
    }

    private void registerNatives() {
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "java/net/SocketOutputStream", "init", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "java/net/SocketOutputStream", "socketWrite0", "(Ljava/io/FileDescriptor;[BII)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            byte[] bytes = ArrayConversionHelper.convertByteArray(args[1].asArray());
            try {
                _vm.getNetwork().socketWrite(java_io_FileDescriptor.getFd(args[0]), bytes, args[2].asInt(), args[3].asInt());
            } catch (IOException e) {
                throw _vm.newThrowable(VMSymbols.java_io_IOException);
            }
        }));
    }
}
