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
import com.diaoling.javavm.utils.ArrayConversionHelper;
import com.diaoling.javavm.values.JavaArray;
import com.diaoling.javavm.values.JavaWrapper;
import com.jcraft.jzlib.CRC32;

public class java_util_zip_CRC32 {
    private static final String THIS = "java/util/zip/CRC32";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "update", "(II)I", true, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            CRC32 crc32 = new CRC32();
            crc32.reset(args[0].asInt());
            crc32.update(new byte[]{(byte) args[1].asInt()}, 0, 1);
            return JavaWrapper.createInteger(vm, (int) (crc32.getValue() & 0xffffffffL));
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "updateBytes", "(I[BII)I", true, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            CRC32 crc32 = new CRC32();
            crc32.reset(args[0].asInt());
            crc32.update(ArrayConversionHelper.convertByteArray((JavaArray) args[1].get()), args[2].asInt(), args[3].asInt());
            return JavaWrapper.createInteger(vm, (int) (crc32.getValue() & 0xffffffffL));
        }));
    }
}
