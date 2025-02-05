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
import com.diaoling.javavm.utils.ASMHelper;
import com.diaoling.javavm.values.JavaArray;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;

public class java_lang_System {
    private static final String THIS = "java/lang/System";

    public static void putProp(VirtualMachine vm, JavaWrapper prop, String key, String val) {
        vm.internalExecute(vm.getSystemDictionary().getJavaUtilProperties().getClassNode(),
                ASMHelper.findMethod(vm.getSystemDictionary().getJavaUtilProperties().getClassNode(), "setProperty", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;"),
                prop,
                new JavaWrapper[]{vm.getStringInterned(key), vm.getStringInterned(val)},
                null
        );
    }

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "setIn0", "(Ljava/io/InputStream;)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            vm.getSystemDictionary().getJavaLangSystem().setStaticField("in", "Ljava/io/InputStream;", args[0]);
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "setOut0", "(Ljava/io/PrintStream;)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            vm.getSystemDictionary().getJavaLangSystem().setStaticField("out", "Ljava/io/PrintStream;", args[0]);
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "setErr0", "(Ljava/io/PrintStream;)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            vm.getSystemDictionary().getJavaLangSystem().setStaticField("err", "Ljava/io/PrintStream;", args[0]);
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "currentTimeMillis", "()J", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            // todo let vm decide
            return vm.newLong(System.currentTimeMillis());
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "nanoTime", "()J", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            // todo let vm decide
            return vm.newLong(System.nanoTime());
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, THIS, "arraycopy", "(Ljava/lang/Object;ILjava/lang/Object;II)V", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            JavaArray src = ((JavaArray) args[0].get());
            JavaArray dst = ((JavaArray) args[2].get());
            int srcStart = args[1].asInt();
            int dstStart = args[3].asInt();
            int amnt = args[4].asInt();
            JavaWrapper[] tmp = new JavaWrapper[amnt];
            for (int count = 0, i = srcStart; count < amnt; count++, i++) {
                tmp[count] = src.get(i);
            }
            for (int count = 0, j = dstStart; count < amnt; count++, j++) {
                dst.set(j, tmp[count]);
            }
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "identityHashCode", "(Ljava/lang/Object;)I", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return vm.newInt(args[0].is(JavaValueType.NULL) ? 0 : args[0].asObject().getHashCode());
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "initProperties", "(Ljava/util/Properties;)Ljava/util/Properties;", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            // todo so many more properties
            putProp(vm, args[0], "file.encoding", "UTF-8");
            putProp(vm, args[0], "file.separator", "\\");
            putProp(vm, args[0], "path.separator", ";");
            putProp(vm, args[0], "line.separator", "\r\n");
            putProp(vm, args[0], "java.home", "C:\\java_home_dir");
            putProp(vm, args[0], "user.dir", "C:\\user_dir");
            putProp(vm, args[0], "sun.reflect.inflationThreshold", String.valueOf(Integer.MAX_VALUE));
            putProp(vm, args[0], "os.name", "Windows 10");
            putProp(vm, args[0], "os.arch", "x86_64");
            putProp(vm, args[0], "os.version", "10.0");
            putProp(vm, args[0], "sun.boot.class.path", "C:\\java_home_dir\\lib\\jce.jar");
            putProp(vm, args[0], "java.io.tmpdir", "C:\\temp_dir\\");
            putProp(vm, args[0], "sun.jnu.encoding", "Cp1252");
            putProp(vm, args[0], "java.awt.headless", "true");
            putProp(vm, args[0], "java.class.version", "50.0");
            putProp(vm, args[0], "awt.toolkit", "sun.awt.windows.WToolkit");
            return args[0];
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "mapLibraryName", "(Ljava/lang/String;)Ljava/lang/String;", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            // todo let vm decide
            return vm.getString(vm.convertJavaObjectToString(args[0]) + ".dll");
        }));
    }
}
