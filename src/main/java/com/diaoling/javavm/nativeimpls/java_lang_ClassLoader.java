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
import com.diaoling.javavm.internals.ClassLoaderData;
import com.diaoling.javavm.internals.VMSymbols;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.utils.TypeHelper;
import com.diaoling.javavm.values.JavaWrapper;

public class java_lang_ClassLoader {
    private static final String THIS = "java/lang/ClassLoader";

    private final VirtualMachine _vm;

    public java_lang_ClassLoader(VirtualMachine virtualMachine) {
        _vm = virtualMachine;
    }

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "findBuiltinLib", "(Ljava/lang/String;)Ljava/lang/String;", true, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            JavaWrapper arg0 = args[0];
            return arg0;
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "findLoadedClass0", "(Ljava/lang/String;)Ljava/lang/Class;", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            JavaClass clazz = JavaClass.forName(vm, TypeHelper.getTypeByFQN(vm, vm.convertJavaObjectToString(args[0])));
            if (clazz == null) {
                System.out.println("[ClassLoader] Could not find class " + vm.convertJavaObjectToString(args[0]));
                return vm.getNull();
            }
            return clazz.getOop();
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "findBootstrapClass", "(Ljava/lang/String;)Ljava/lang/Class;", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            // todo eventually classloaders
            JavaClass clazz = JavaClass.forName(vm, TypeHelper.getTypeByFQN(vm, vm.convertJavaObjectToString(args[0])));
            if (clazz == null) {
                System.out.println("[ClassLoader] Could not find class " + vm.convertJavaObjectToString(args[0]));
                return vm.getNull();
            }
            return clazz.getOop();
        }));
        vm.hook(HookGenerator.generateUnknownHandlingVoidHook(vm, "java/lang/ClassLoader$NativeLibrary", "load", "(Ljava/lang/String;Z)V", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            System.out.println("[ClassLoader] An attempt was made to load the library " + vm.convertJavaObjectToString(args[0]));
            inst.asObject().setField("loaded", "Z", vm.newBoolean(true));
            return;
        }));
    }


    public JavaWrapper nonReflectionClassLoader(JavaWrapper classLoader) {
        if (classLoader != null) {
            if (classLoader.getJavaClass().isAssignableFrom(_vm.getSystemDictionary().getSunReflectDelegatingClassLoader())) {

            }
        }
        return null;
    }

    public ClassLoaderData loaderData(JavaWrapper oop) {
        return oop.asObject().getMetadata(VMSymbols.METADATA_LOADER_DATA);
    }

    public boolean parallelCapable(JavaWrapper oop) {
        return oop.asObject().getField(VMSymbols.java_lang_ClassLoader_parallelLockMap_name, VMSymbols.java_lang_ClassLoader_parallelLockMap_sig) != null;
    }
}