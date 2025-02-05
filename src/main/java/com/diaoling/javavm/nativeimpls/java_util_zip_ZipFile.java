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

public class java_util_zip_ZipFile {
    private static final String THIS = "java/util/zip/ZipFile";

    private final VirtualMachine _vm;

    public java_util_zip_ZipFile(VirtualMachine vm) {
        _vm = vm;
        registerNatives();
    }

    private void registerNatives() {
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, THIS, "initIDs", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            // ignored
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "open", "(Ljava/lang/String;IJZ)J", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            String requestedFile = _vm.convertJavaObjectToString(args[0]);
            int mode = args[1].asPrimitive().asInt();
            long lastModifiedTime = args[2].asPrimitive().asLong();
            boolean usemmap = args[3].asPrimitive().asBoolean();

            long opened = _vm.getFilesystem().openZipfile(requestedFile, mode, lastModifiedTime, usemmap);
            if (opened == -1) {
                throw _vm.newThrowable(VMSymbols.java_io_FileNotFoundException, requestedFile);
            } else {
                return _vm.newLong(opened);
            }
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getTotal", "(J)I", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            return _vm.newInt(_vm.getFilesystem().getZipTotal(address));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "startsWithLOC", "(J)Z", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            return _vm.newBoolean(_vm.getFilesystem().getZipStartsWithLOC(address));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getNextEntry", "(JI)J", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            int index = args[1].asInt();
            return _vm.newLong(_vm.getFilesystem().getZipNextEntry(address, index));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getEntryTime", "(J)J", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            return _vm.newLong(_vm.getFilesystem().getZipEntryTime(address));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getEntryCrc", "(J)J", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            return _vm.newLong(_vm.getFilesystem().getZipEntryCrc(address));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getEntryCSize", "(J)J", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            return _vm.newLong(_vm.getFilesystem().getZipEntryCSize(address));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getEntrySize", "(J)J", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            return _vm.newLong(_vm.getFilesystem().getZipEntrySize(address));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getEntryMethod", "(J)I", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            return _vm.newInt(_vm.getFilesystem().getZipEntryMethod(address));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getEntryFlag", "(J)I", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            return _vm.newInt(_vm.getFilesystem().getZipEntryFlag(address));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getCommentBytes", "(J)[B", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            return ArrayConversionHelper.convertByteArray(_vm, _vm.getFilesystem().getZipCommentBytes(address));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getEntry", "(J[BZ)J", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            byte[] name = ArrayConversionHelper.convertByteArray(args[1].asArray());
            boolean addSlash = args[2].asBoolean();
            return _vm.newLong(_vm.getFilesystem().getZipGetEntry(address, name, addSlash));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "getEntryBytes", "(JI)[B", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            int type = args[1].asInt();
            return ArrayConversionHelper.convertByteArray(_vm, _vm.getFilesystem().getZipEntryBytes(address, type));
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingHook(_vm, THIS, "read", "(JJJ[BII)I", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            long entry = args[1].asLong();
            long pos = args[2].asLong();
            byte[] target = ArrayConversionHelper.convertByteArray(args[3].asArray());
            int off = args[4].asInt();
            int len = args[5].asInt();
            int read = _vm.getFilesystem().zipRead(address, entry, pos, target, off, len);
            for (int i = 0; i < target.length; i++) {
                args[3].asArray().set(i, _vm.newByte(target[i]));
            }
            return _vm.newInt(read);
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, THIS, "freeEntry", "(JJ)V", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            // not needed here
        }));
        _vm.hook(HookGenerator.generateUnknownHandlingVoidHook(_vm, THIS, "close", "(J)V", true, Cause.ALL, Effect.ALL, (ctx, inst, args) -> {
            long address = args[0].asLong();
            _vm.getFilesystem().closeZip(address);
        }));

    }
}
