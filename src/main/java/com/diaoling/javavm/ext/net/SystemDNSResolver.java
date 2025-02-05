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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * Resolves hosts using the system's DNS resolver
 */
public class SystemDNSResolver implements DNSResolver {
    @Override
    public boolean resolve(String host, List<InetAddress> result) {
        try {
            InetAddress[] resolved = InetAddress.getAllByName(host);
            result.addAll(Arrays.asList(resolved));
        } catch (UnknownHostException e) {
        }
        return true;
    }
}
