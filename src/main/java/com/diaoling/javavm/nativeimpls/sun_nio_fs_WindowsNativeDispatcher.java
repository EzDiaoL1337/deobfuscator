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
import com.diaoling.javavm.hooks.HookGenerator;
import com.diaoling.javavm.values.JavaWrapper;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class sun_nio_fs_WindowsNativeDispatcher {
    private final VirtualMachine _vm;

    public sun_nio_fs_WindowsNativeDispatcher(VirtualMachine vm) {
        _vm = vm;
        registerNatives();
    }

    private void registerNatives() {
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "sun/nio/fs/WindowsNativeDispatcher", "initIDs", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            // ignored
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, "sun/nio/fs/WindowsNativeDispatcher", "FindFirstFile0", "(JLsun/nio/fs/WindowsNativeDispatcher$FirstFile;)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            ByteBuffer buffer = _vm.getMemory().getBufferForAddress(args[0].asLong());
            JavaWrapper dst = args[1];

            buffer.position(0);
            CharBuffer charBuffer = buffer.asCharBuffer();
            char[] data = new char[charBuffer.remaining()];
            charBuffer.get(data);

            String fileName = new String(data);
            fileName = fileName.substring(0, fileName.indexOf("\u0000")); // it's null terminated

            throw new ExecutionException("unsupported");
        }));
    }
}
