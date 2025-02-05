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

package com.diaoling.javavm.ext.net;

import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaWrapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Network {
    private final VirtualMachine _vm;
    private final List<NetworkInterface> _interfaces;

    private final Map<Integer, JSocket> _sockets = new HashMap<>();
    private final List<DNSResolver> _resolvers = new ArrayList<>();
    private final List<SocketConnectionEstablisher> _connectionEstablishers = new ArrayList<>();

    public Network(VirtualMachine vm) {
        _vm = vm;
        _interfaces = new ArrayList<>();
    }

    public void addInterface(NetworkInterface netintf) {
        _interfaces.add(netintf);
    }

    public List<NetworkInterface> getInterfaces() {
        return Collections.unmodifiableList(_interfaces);
    }

    public void registerDNSResolver(DNSResolver resolver) {
        _resolvers.add(resolver);
    }

    public List<InetAddress> resolve(String host) {
        List<InetAddress> result = new ArrayList<>();
        for (DNSResolver resolver : _resolvers) {
            if (!resolver.resolve(host, result)) {
                break;
            }
        }
        return result;
    }

    public void registerConnectionEstablisher(SocketConnectionEstablisher establisher) {
        _connectionEstablishers.add(establisher);
    }

    public boolean connect(int fd, InetSocketAddress address) {
        JSocket socket = _sockets.get(fd);
        System.out.println("[Networking] Connecting socket " + fd + " to " + address);
        for (SocketConnectionEstablisher establisher : _connectionEstablishers) {
            establisher.connect(socket, address);
            if (socket.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public JSocket newSocket(boolean stream, boolean v6only /* unused */) {
        System.out.println("[Networking] Creating new socket with options: stream=" + stream + ", v6only=" + v6only);
        int fd = _vm.getFilesystem().nextFd();
        JSocket socket = new JSocket(fd);
        _sockets.put(fd, socket);
        return socket;
    }

    public int socketRead(int fd, byte[] bytes, int off, int len) throws IOException {
        System.out.println("[Networking] Reading from socket " + fd);
        JSocket socket = _sockets.get(fd);
        int read = socket.getInputStream().read(bytes, off, len);
        System.out.println("[Networking] Finished reading from socket " + fd + ": " + (read < 0 ? "[err]" : new String(bytes, off, read)));
        return read;
    }

    public void socketWrite(int fd, byte[] bytes, int off, int len) throws IOException {
        System.out.println("[Networking] Writing to socket " + fd + ": " + new String(bytes, off, len));
        JSocket socket = _sockets.get(fd);
        socket.getOutputStream().write(bytes, off, len);
    }
    public int socketAvailable(int fd) throws IOException {
        System.out.println("[Networking] Getting available bytes of socket " + fd);
        JSocket socket = _sockets.get(fd);
        return socket.getInputStream().available();
    }

    // vm helpers
    public JavaWrapper createInet4Address(Inet4Address addr) {
        try {
            Field holder = InetAddress.class.getDeclaredField("holder");
            holder.setAccessible(true);
            Object holderObj = holder.get(addr);
            Field address = holderObj.getClass().getDeclaredField("address");
            address.setAccessible(true);
            int addressVal = address.getInt(holderObj);

            JavaWrapper newInetAddr = _vm.newInstance(JavaClass.forName(_vm, "java/net/Inet4Address"), "()V");
            JavaWrapper newHolder = newInetAddr.asObject().getField("holder", "Ljava/net/InetAddress$InetAddressHolder;");
            newHolder.asObject().setField("address", "I", _vm.newInt(addressVal));

            return newInetAddr;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public InetAddress getInetAddress(JavaWrapper arg) {
        if (arg.getJavaClass().getClassNode().name.equals("java/net/Inet4Address")) {
            int address = arg.asObject().getField("holder", "Ljava/net/InetAddress$InetAddressHolder;").asObject().getField("address", "I").asInt();

            byte[] addr = new byte[4];
            addr[0] = (byte) ((address >>> 24) & 0xFF);
            addr[1] = (byte) ((address >>> 16) & 0xFF);
            addr[2] = (byte) ((address >>> 8) & 0xFF);
            addr[3] = (byte) (address & 0xFF);

            try {
                return InetAddress.getByAddress(addr);
            } catch (UnknownHostException e) {
                throw new ExecutionException("Unexpected error", e);
            }
        } else {
            throw new ExecutionException("IPv6 is not supported yet");
        }
    }

}
