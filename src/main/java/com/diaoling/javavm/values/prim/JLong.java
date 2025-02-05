/*
 * Copyright 2016 Sam Sun <me@samczsun.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.diaoling.javavm.values.prim;

import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaValueType;
import org.objectweb.asm.Type;

public class JLong extends JPrimitive {
    private final VirtualMachine _vm;

    private final long _value;

    public JLong(VirtualMachine vm, long value) {
        this._value = value;
        this._vm = vm;
    }

    @Override
    public final boolean asBoolean() {
        return this._value != 0;
    }

    @Override
    public final char asChar() {
        return (char) _value;
    }

    @Override
    public final byte asByte() {
        return (byte) _value;
    }

    @Override
    public final short asShort() {
        return (short) _value;
    }

    @Override
    public final int asInt() {
        return (int) _value;
    }

    @Override
    public final float asFloat() {
        return (float) _value;
    }

    @Override
    public final long asLong() {
        return (long) _value;
    }

    @Override
    public final double asDouble() {
        return (double) _value;
    }

    @Override
    public final int getSize() {
        return 2;
    }

    @Override
    public final JavaClass getJavaClass() {
        return _vm.INTEGER;
    }

    public final Type getType() {
        return Type.LONG_TYPE;
    }

    @Override
    public final Object rawValue() {
        return _value;
    }

    @Override
    public final String toString() {
        return "JavaLong(value=" + _value + ")";
    }

    @Override
    public boolean is(JavaValueType type) {
        return type == JavaValueType.PRIMITIVE || type == JavaValueType.LONG || type == JavaValueType.WIDE;
    }
}
