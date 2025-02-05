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
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.ext.net.InterfaceAddress;
import com.diaoling.javavm.ext.net.NetworkInterface;
import com.diaoling.javavm.hooks.HookGenerator;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaWrapper;

import java.net.Inet4Address;
import java.util.List;

public class java_net_NetworkInterface {
    private final VirtualMachine _vm;

    public java_net_NetworkInterface(VirtualMachine vm) {
        _vm = vm;
        registerNatives();
    }

    private void registerNatives() {
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "java/net/NetworkInterface", "init", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {

        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/net/NetworkInterface", "getAll", "()[Ljava/net/NetworkInterface;", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            if (_vm.getNetwork().getInterfaces().isEmpty()) {
                return _vm.getNull();
            }

            List<NetworkInterface> interfaces = _vm.getNetwork().getInterfaces();

            int index = 1;
            JavaWrapper array = JavaWrapper.createArray(JavaClass.forName(_vm, "[Ljava/net/NetworkInterface;"), new JavaWrapper[interfaces.size()]);
            for (NetworkInterface networkInterface : interfaces) {
                JavaWrapper newInterface = _vm.newInstance(JavaClass.forName(_vm, "java/net/NetworkInterface"), "()V");
                newInterface.asObject().setField("name", "Ljava/lang/String;", _vm.getStringInterned(networkInterface.getName()));
                newInterface.asObject().setField("displayName", "Ljava/lang/String;", _vm.getStringInterned(networkInterface.getDisplayName()));
                newInterface.asObject().setField("index", "I", _vm.newInt(index));

                JavaWrapper addrArr = JavaWrapper.createArray(JavaClass.forName(_vm, "[Ljava/net/InetAddress;"), new JavaWrapper[networkInterface.getBindings().size()]);
                JavaWrapper bindingArr = JavaWrapper.createArray(JavaClass.forName(_vm, "[Ljava/net/InterfaceAddress;"), new JavaWrapper[networkInterface.getBindings().size()]);

                newInterface.asObject().setField("addrs", "[Ljava/net/InetAddress;", addrArr);
                newInterface.asObject().setField("bindings", "[Ljava/net/InterfaceAddress;", bindingArr);

                int interfaceAddressIndex = 0;
                for (InterfaceAddress addr : networkInterface.getBindings()) {
                    JavaWrapper newAddress;
                    JavaWrapper newBroadcast;
                    if (addr.getAddress() instanceof Inet4Address) {
                        newAddress = _vm.getNetwork().createInet4Address(((Inet4Address) addr.getAddress()));
                    } else {
                        throw new ExecutionException("IPv6 is not supported");
                    }
                    newBroadcast = _vm.getNetwork().createInet4Address(addr.getBroadcast());
                    addrArr.asArray().set(interfaceAddressIndex, newAddress);
                    if (addr.getMaskLength() != -1) {
                        JavaWrapper interfaceAddress = createInterfaceAddress(newAddress, newBroadcast, addr.getMaskLength());
                        bindingArr.asArray().set(interfaceAddressIndex, interfaceAddress);
                    } else {
                        bindingArr.asArray().set(interfaceAddressIndex, _vm.getNull());
                    }
                    interfaceAddressIndex++;
                }

                array.asArray().set(index - 1, newInterface);
                index++;
            }
            return array;
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, "java/net/NetworkInterface", "getMacAddr0", "([BLjava/lang/String;I)[B", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return _vm.getNull();
        }));
    }

    private JavaWrapper createInterfaceAddress(JavaWrapper address, JavaWrapper broadcast, short mask) {
        JavaWrapper interfaceAddress = _vm.newInstance(JavaClass.forName(_vm, "java/net/InterfaceAddress"), "()V");
        interfaceAddress.asObject().setField("address", "Ljava/net/InetAddress;", address);
        interfaceAddress.asObject().setField("broadcast", "Ljava/net/Inet4Address;", broadcast);
        interfaceAddress.asObject().setField("maskLength", "s", _vm.newShort(mask));
        return interfaceAddress;
    }
}
