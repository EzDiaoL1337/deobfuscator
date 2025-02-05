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
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaWrapper;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.List;

public class java_net_Inet4AddressImpl {
    private final VirtualMachine _vm;

    public java_net_Inet4AddressImpl(VirtualMachine vm) {
        _vm = vm;
        registerNatives();
    }

    private void registerNatives() {
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/net/Inet4AddressImpl", "lookupAllHostAddr", "(Ljava/lang/String;)[Ljava/net/InetAddress;", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            String host = _vm.convertJavaObjectToString(args[0]);
            System.out.println("[Networking] Resolving host " + host);
            List<InetAddress> resolved = _vm.getNetwork().resolve(host);

            if (resolved.isEmpty()) {
                throw _vm.newThrowable(VMSymbols.java_net_UnknownHostException, host);
            }

            JavaWrapper array = JavaWrapper.createArray(JavaClass.forName(_vm, "[Ljava/net/InetAddress;"), new JavaWrapper[resolved.size()]);
            for (int i = 0; i < resolved.size(); i++) {
                InetAddress address = resolved.get(i);
                if (address instanceof Inet4Address) {
                    array.asArray().set(i, _vm.getNetwork().createInet4Address((Inet4Address) address));
                } else {
                    throw new UnsupportedOperationException("IPv6 addressees are not supported yet (" + address + ")");
                }
            }
            return array;
        }));
    }
}
