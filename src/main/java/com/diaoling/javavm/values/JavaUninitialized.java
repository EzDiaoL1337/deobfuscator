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

package com.diaoling.javavm.values;

import com.diaoling.javavm.mirrors.JavaClass;

import java.util.EnumSet;

public class JavaUninitialized extends JavaValue {
    private static final EnumSet<JavaValueType> TYPES = EnumSet.of(JavaValueType.UNINITIALIZED);
    private final JavaClass _class;
    private final String _bytecodeType;
    private JavaObject value;
    public JavaUninitialized(JavaClass clazz, String originalType) {
        this._class = clazz;
        this._bytecodeType = originalType;
        this.value = new JavaObject(clazz, originalType);
    }

    @Override
    public boolean is(JavaValueType type) {
        return TYPES.contains(type);
    }

    public JavaObject initializedValue() {
        return this.value;
    }

    @Override
    public JavaClass getJavaClass() {
        return this._class;
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public String type() {
        return this._bytecodeType;
    }

    @Override
    public String toString() {
        return "JavaUninitialized(type=" + _bytecodeType + ", val=" + value + ")";
    }
}
