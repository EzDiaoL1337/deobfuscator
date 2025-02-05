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

import java.io.File;

public class java_io_WinNTFileSystem {
    private static final String THIS = "java/io/WinNTFileSystem";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "initIDs", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, "java/io/WinNTFileSystem", "getBooleanAttributes", "(Ljava/io/File;)I", false, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            String path = vm.convertJavaObjectToString(args[0].asObject().getField("path", "Ljava/lang/String;"));
            File file = vm.getFilesystem().map(new File(path));
            if (file != null && file.exists()) {
                System.out.println("[Filesystem] Getting boolean attributes of " + file + " (" + path + ")");
                int result = 0x01; // BA_EXISTS;
                if (file.isDirectory()) {
                    result |= 0x04;
                }
                if (file.isFile()) {
                    result |= 0x02;
                }
                if (file.isHidden()) {
                    result |= 0x08;
                }
                return vm.newInt(result);
            }
            System.out.println("[Filesystem] Could not get boolean attributes of file " + path);
            return vm.newInt(0);
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, "java/io/WinNTFileSystem", "canonicalize0", "(Ljava/lang/String;)Ljava/lang/String;", false, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            return args[0];
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, "java/io/WinNTFileSystem", "canonicalizeWithPrefix0", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", false, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            return args[1];
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, "java/io/WinNTFileSystem", "getLastModifiedTime", "(Ljava/io/File;)J", false, Cause.ALL, Effect.NONE, (ctx, inst, args) -> {
            return vm.newLong(new File(vm.convertJavaObjectToString(args[0].asObject().getField("path", "Ljava/lang/String;"))).lastModified());
        }));
    }
}
