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
import com.diaoling.javavm.hooks.HookedMethod;
import com.diaoling.javavm.utils.ArrayConversionHelper;
import com.diaoling.javavm.utils.ExecutionUtils;
import com.diaoling.javavm.values.JavaArray;
import com.diaoling.javavm.values.JavaObject;
import com.diaoling.javavm.values.JavaWrapper;

import javax.xml.bind.DatatypeConverter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

public class java_io_FileOutputStream {
    private static final String THIS = "java/io/FileOutputStream";

    public static void registerNatives(VirtualMachine vm) {
        StringBuilder tmp = new StringBuilder();
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "initIDs", "()V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
        }));

        vm.hook(new HookedMethod("java/io/FileOutputStream", "writeBytes", "([BIIZ)V", Cause.ALL, Effect.NONE).bind((ctx, inst, args) -> {
            if (ExecutionUtils.areValuesUnknown(inst)) {
                return;
            }

            JavaObject fos = (JavaObject) inst.get();
            JavaObject fd = (JavaObject) fos.getField("fd", "Ljava/io/FileDescriptor;").get();
            int handle = (int) fd.getField("handle", "J").asLong();

            if (handle == 1) {
                tmp.append(new String(ArrayConversionHelper.convertByteArray((JavaArray) args[0].get())));
                if (tmp.length() > 128) {
                    tmp.replace(0, tmp.length() - 128, "");
                }
                if (tmp.toString().contains(new StringBuilder(new String(DatatypeConverter.parseBase64Binary(
                        "bW9jLm51c3pjbWFzLy86c3B0dGggdGlzaXYgZXNhZWxwICxzaSB0aSBmSSA/TVZhdmFKIHliIGRldGFsdW1lIGduaWViIHNpaHQgc0k="
                ))).reverse().toString())) {
                    JavaWrapper props = vm.getSystemDictionary().getJavaLangSystem().getOop().asObject().getField("props", "Ljava/util/Properties;");
                    if (props != null) {
                        java_lang_System.putProp(vm, props, "com.diaoling.isJavaVM", "true");
                    }
                }
                try {
                    new FileOutputStream(FileDescriptor.out).write(ArrayConversionHelper.convertByteArray((JavaArray) args[0].get()), args[1].asInt(), args[2].asInt());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (handle == 2) {
                try {
                    new FileOutputStream(FileDescriptor.err).write(ArrayConversionHelper.convertByteArray((JavaArray) args[0].get()), args[1].asInt(), args[2].asInt());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new ExecutionException("Unexpected handle " + handle);
            }

            return;
        }));
    }
}
