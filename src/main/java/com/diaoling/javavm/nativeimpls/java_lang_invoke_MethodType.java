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

import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.values.JavaArray;
import com.diaoling.javavm.values.JavaObject;
import com.diaoling.javavm.values.JavaWrapper;

public class java_lang_invoke_MethodType {
    public static boolean isInstance(VirtualMachine vm, JavaWrapper oop) {
        return oop != null && oop.getJavaClass() == vm.getSystemDictionary().getJavaLangInvokeMethodType();
    }

    public static String asSignature(JavaWrapper oop, boolean internIfNotFound) {
        StringBuilder stringBuilder = new StringBuilder();
        printSignature(oop, stringBuilder);

        return stringBuilder.toString();
    }

    public static JavaWrapper ptypes(JavaWrapper oop) {
        return ((JavaObject) oop.get()).getField("ptypes", "[Ljava/lang/Class;");
    }

    public static JavaWrapper rtype(JavaWrapper oop) {
        return ((JavaObject) oop.get()).getField("rtype", "Ljava/lang/Class;");
    }

    public static void printSignature(JavaWrapper oop, StringBuilder stringBuilder) {
        stringBuilder.append("(");

        JavaArray ptypes = ptypes(oop).asArray();
        for (int i = 0, limit = ptypes.length(); i < limit; i++) {
            java_lang_Class.printSignature(ptypes.get(i), stringBuilder);
        }

        stringBuilder.append(")");
        java_lang_Class.printSignature(rtype(oop), stringBuilder);
    }
}
