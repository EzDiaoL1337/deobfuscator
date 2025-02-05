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

import com.diaoling.javavm.values.JavaObject;
import com.diaoling.javavm.values.JavaWrapper;

public class java_lang_invoke_MemberName {
    public static JavaWrapper clazz(JavaWrapper memberName) {
        return memberName.asObject().getField("clazz", "Ljava/lang/Class;");
    }

    public static JavaWrapper name(JavaWrapper memberName) {
        return memberName.asObject().getField("name", "Ljava/lang/String;");
    }

    public static JavaWrapper type(JavaWrapper memberName) {
        return memberName.asObject().getField("type", "Ljava/lang/Object;");
    }

    public static int flags(JavaWrapper memberName) {
        return ((JavaObject) memberName.get()).getField("flags", "I").asInt();
    }

    public static void set_flags(JavaWrapper memberName, JavaWrapper flag) {
        memberName.asObject().setField("flags", "I", flag);
    }

    public static void set_vmtarget(JavaWrapper memberName, Object m) {
        memberName.asObject().setMetadata(JavaObject.VMTARGET, m);
    }

    public static void set_vmindex(JavaWrapper memberName, int vmindex) {
        memberName.asObject().setMetadata(JavaObject.VMINDEX, vmindex);
    }

    public static void set_clazz(JavaWrapper memberName, JavaWrapper oop) {
        memberName.asObject().setField("clazz", "Ljava/lang/Class;", oop);
    }

    public static void set_name(JavaWrapper memberName, JavaWrapper oop) {
        memberName.asObject().setField("name", "Ljava/lang/String;", oop);
    }

    public static void set_type(JavaWrapper memberName, JavaWrapper oop) {
        memberName.asObject().setField("type", "Ljava/lang/Object;", oop);
    }

    public static int vmindex(JavaWrapper memberName) {
        Integer x = (Integer) memberName.asObject().getMetadata(JavaObject.VMINDEX);
        return x == null ? 0 : x;
    }

    public static Object get_vmtarget(JavaWrapper vmentry) {
        return vmentry.asObject().getMetadata(JavaObject.VMTARGET);
    }
}
