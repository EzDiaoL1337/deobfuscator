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

import java.io.IOException;
import java.net.InetSocketAddress;

public class java_net_DualStackPlainSocketImpl {
    private final VirtualMachine _vm;

    public java_net_DualStackPlainSocketImpl(VirtualMachine vm) {
        _vm = vm;
        registerNatives();
    }

    private void registerNatives() {
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "java/net/DualStackPlainSocketImpl", "initIDs", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/net/DualStackPlainSocketImpl", "socket0", "(ZZ)I", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return _vm.newInt(_vm.getNetwork().newSocket(args[0].asBoolean(), args[1].asBoolean()).getFd());
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/net/DualStackPlainSocketImpl", "connect0", "(ILjava/net/InetAddress;I)I", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            int fd = args[0].asInt();
            boolean connected = _vm.getNetwork().connect(fd, new InetSocketAddress(_vm.getNetwork().getInetAddress(args[1]), args[2].asInt()));
            if (!connected) {
                throw _vm.newThrowable(VMSymbols.java_net_SocketException, "Could not connect");
            }
            return _vm.newInt(0);
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/net/DualStackPlainSocketImpl", "localPort0", "(I)I", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            System.out.println("[Networking] Getting local port of socket " + args[0].asInt());
            return _vm.newInt(12345);
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "java/net/DualStackPlainSocketImpl", "setIntOption", "(III)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            System.out.println("[Networking] Setting option of socket " + args[0].asInt() + ": " + args[1].asInt() + " = " + args[2].asInt());
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "java/net/DualStackPlainSocketImpl", "close0", "(I)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            System.out.println("[Networking] Closing socket " + args[0].asInt());
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/net/DualStackPlainSocketImpl", "available0", "(I)I", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            try {
                return _vm.newInt(_vm.getNetwork().socketAvailable(args[0].asInt()));
            } catch (IOException e) {
                throw _vm.newThrowable(VMSymbols.java_io_IOException);
            }
        }));
    }
}
