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

public class java_net_SocketInputStream {
    private final VirtualMachine _vm;

    public java_net_SocketInputStream(VirtualMachine vm) {
        _vm = vm;
        registerNatives();
    }

    private void registerNatives() {
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "java/net/SocketInputStream", "init", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/net/SocketInputStream", "socketRead0", "(Ljava/io/FileDescriptor;[BIII)I", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            byte[] bytes = ArrayConversionHelper.convertByteArray(args[1].asArray());
            int read;
            try {
                read = _vm.getNetwork().socketRead(java_io_FileDescriptor.getFd(args[0]), bytes, args[2].asInt(), args[3].asInt());
            } catch (IOException e) {
                throw _vm.newThrowable(VMSymbols.java_io_IOException);
            }
            for (int i = 0; i < bytes.length; i++) {
                args[1].asArray().set(i, _vm.newByte(bytes[i]));
            }
            return _vm.newInt(read);
        }));
    }
}
