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

package com.diaoling.javavm.internals;

import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.mirrors.JavaField;

import java.lang.reflect.Modifier;

public class FieldDescriptor {
    private int flags; // todo make accessflags
    private int index;
    private Object constantPool;
    // javavm
    private JavaField field;

    public FieldDescriptor() {

    }

    public void reinitialize(JavaClass klass, int index) {
        flags = klass.getFieldById(index).getFieldNode().access;
        field = klass.getFieldById(index);
        this.index = index;
    }

    public int getFlags() {
        return flags;
    }

    public int getIndex() {
        return index;
    }

    public boolean is_static() {
        return Modifier.isStatic(flags);
    }

    public JavaClass fieldHolder() {
        return field.getDeclaringClass();
    }

    public String signature() {
        return field.getFieldNode().desc;
    }

    public String name() {
        return field.getFieldNode().name;
    }

    public boolean hasField() {
        return field != null;
    }
}
