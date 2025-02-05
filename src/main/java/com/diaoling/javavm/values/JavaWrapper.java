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

import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.prim.JDouble;
import com.diaoling.javavm.values.prim.JFloat;
import com.diaoling.javavm.values.prim.JInteger;
import com.diaoling.javavm.values.prim.JPrimitive;

import java.util.*;

public class JavaWrapper extends JavaValue {

    private JavaValue _value;

    private JavaWrapper(JavaValue value) {
        this._value = value;
    }

    public static JavaWrapper wrap(JavaValue value) {
        return new JavaWrapper(value);
    }

    private static final JavaWrapper top = JavaWrapper.wrap(new JavaTop());

    public static JavaWrapper createTop() {
        return top;
    }

    public static JavaWrapper createUninitialized(JavaClass orig, String descType) {
        return JavaWrapper.wrap(new JavaUninitialized(orig, descType));
    }

    @Deprecated
    public static JavaWrapper createInteger(VirtualMachine vm, int value) {
        return JavaWrapper.wrap(new JInteger(vm, value));
    }

    @Deprecated
    public static JavaWrapper createFloat(VirtualMachine vm, float value) {
        return JavaWrapper.wrap(new JFloat(vm, value));
    }

    @Deprecated
    public static JavaWrapper createDouble(VirtualMachine vm, double value) {
        return JavaWrapper.wrap(new JDouble(vm, value));
    }

    public static JavaWrapper createArray(JavaClass type, JavaWrapper[] values) {
        return wrap(new JavaArray(type, values));
    }

    public boolean is(JavaValueType type) {
        return _value.is(type);
    }

    public byte asByte() {
        return _value.asByte();
    }

    public char asChar() {
        return _value.asChar();
    }

    public short asShort() {
        return _value.asShort();
    }

    public int asInt() {
        return _value.asInt();
    }

    public float asFloat() {
        return _value.asFloat();
    }

    public double asDouble() {
        return _value.asDouble();
    }

    public long asLong() {
        return _value.asLong();
    }

    public boolean asBoolean() {
        return _value.asBoolean();
    }

    public JavaValue get() {
        return _value;
    }

    public void set(JavaValue value) {
        this._value = value;
    }

    @Override
    public String type() {
        return _value.type();
    }

    @Override
    public JavaClass getJavaClass() {
        return _value.getJavaClass();
    }


    public String toString() {
        return _value.toString();
    }

    @Override
    public int getSize() {
        return _value.getSize();
    }

    @Override
    public <T> T getMetadata(String key) {
        return _value.getMetadata(key);
    }

    @Override
    public void setMetadata(String key, Object data) {
        _value.setMetadata(key, data);
    }

    @Override
    public Map<String, Object> getMetadata() {
        return _value.getMetadata();
    }

    @Override
    public void setMetadata(Map<String, Object> metadata0) {
        _value.setMetadata(metadata0);
    }

    @Override
    public <T> T compareAndSwapMetadata(String key, T data, T expected) {
        return _value.compareAndSwapMetadata(key, data, expected);
    }

    public JavaObject asObject() {
        if (!(_value instanceof JavaObject)) {
            throw new ExecutionException("Unexpected value: " + _value);
        }
        return (JavaObject) _value;
    }

    public JPrimitive asPrimitive() {
        return (JPrimitive) _value;
    }

    public JavaArray asArray() {
        return (JavaArray) _value;
    }
}
