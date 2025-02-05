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

import java.net.Inet4Address;
import java.net.InetAddress;

public class InterfaceAddress {
    private InetAddress address = null;
    private Inet4Address broadcast = null;
    private short maskLength = 0;

    public InterfaceAddress() {
    }

    public InterfaceAddress(InetAddress address, Inet4Address broadcast, short maskLength) {
        this.address = address;
        this.broadcast = broadcast;
        this.maskLength = maskLength;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public Inet4Address getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Inet4Address broadcast) {
        this.broadcast = broadcast;
    }

    public short getMaskLength() {
        return maskLength;
    }

    public void setMaskLength(short maskLength) {
        this.maskLength = maskLength;
    }
}
