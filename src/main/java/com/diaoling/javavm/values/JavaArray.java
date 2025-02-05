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

public class JavaArray extends JavaValue {

    private JavaClass originalClazz;
    private JavaClass clazz;
    private JavaWrapper[] values;

    public JavaArray(JavaClass clazz, JavaWrapper[] values) {
        this.originalClazz = clazz;
        this.clazz = clazz;
        this.values = values;
    }

    @Override
    public boolean is(JavaValueType type) {
        return type == JavaValueType.ARRAY || type == JavaValueType.OBJECT;
    }

    @Override
    public JavaClass getJavaClass() {
        return this.clazz;
    }

    @Override
    public JavaClass getOriginalClass() {
        return this.originalClazz;
    }

    @Override
    public int getSize() {
        return 1;
    }

    public void set(int index, JavaWrapper copy) {
        values[index] = copy;
    }

    public JavaWrapper get(int index) {
        JavaWrapper val = values[index];
        return val == null ? JavaValue.forPrimitive(clazz.getVM(), clazz.internalGetType()) : val;
    }

    public int length() {
        return values.length;
    }

    public JavaWrapper[] rawArray() {
        return this.values;
    }

    public JavaValue checkcast(JavaClass other) {
        this.clazz = other;
        return this;
    }
}
