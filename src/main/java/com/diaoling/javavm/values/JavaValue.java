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
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class JavaValue {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Object metadataLock = new Object();
    /*
     * @Deprecated until field slots work
     */
    @Deprecated
    private volatile Map<String, Object> metadata;

    public static JavaWrapper forPrimitive(VirtualMachine vm, Type type) {
        if (type.getSort() == Type.METHOD || type.getSort() == Type.VOID) {
            throw new ExecutionException("An internal error occurred: Cannot create primitive for " + type.getSort());
        }
        switch (type.getSort()) {
            case Type.BOOLEAN:
                return vm.newBoolean(false);
            case Type.BYTE:
                return vm.newByte((byte) 0);
            case Type.CHAR:
                return vm.newChar('\u0000');
            case Type.DOUBLE:
                return JavaWrapper.createDouble(vm, 0.0D);
            case Type.FLOAT:
                return JavaWrapper.createFloat(vm, 0.0F);
            case Type.INT:
                return JavaWrapper.createInteger(vm, 0);
            case Type.LONG:
                return vm.newLong(0L);
            case Type.SHORT:
                return vm.newShort((short) 0);
            case Type.OBJECT:
            case Type.ARRAY:
                return vm.getNull();
            default:
                throw new ExecutionException("Did not expect primitive type " + type.getSize());
        }
    }

    public abstract boolean is(JavaValueType type);

    public byte asByte() {
        throw new ExecutionException("Cannot call byteValue()");
    }

    public char asChar() {
        throw new ExecutionException("Cannot call charValue()");
    }

    public short asShort() {
        throw new ExecutionException("Cannot call shortValue()");
    }

    public int asInt() {
        throw new ExecutionException("Cannot call intValue()");
    }

    public float asFloat() {
        throw new ExecutionException("Cannot call floatValue()");
    }

    public double asDouble() {
        throw new ExecutionException("Cannot call doubleValue()");
    }

    public long asLong() {
        throw new ExecutionException("Cannot call longValue()");
    }

    public boolean asBoolean() {
        throw new ExecutionException("Cannot call booleanValue()");
    }

    public VirtualMachine getVM() {
        return null;
    }

    public String type() {
        throw new ExecutionException(new UnsupportedOperationException());
    }

    public abstract JavaClass getJavaClass();

    public JavaClass getOriginalClass() {
        return getJavaClass();
    }

    public boolean isInstanceOf(Type type) {
        JavaClass s = getOriginalClass();
        JavaClass t = JavaClass.forName(s.getVM(), type);
        return t.isAssignableFrom(s);
    }

    /**
     * Get the size of this object
     *
     * @return Either 1, or 2
     */
    public abstract int getSize();

    public Condition getCondition() {
        return condition;
    }

    public Lock getLock() {
        return lock;
    }

    @Deprecated
    public <T> T getMetadata(String key) {
        synchronized (metadataLock) {
            if (metadata == null) {
                return null;
            }
            return (T) metadata.get(key);
        }
    }

    @Deprecated
    public void setMetadata(String key, Object data) {
        synchronized (metadataLock) {
            if (metadata == null) {
                metadata = new HashMap<>(4);
            }
            metadata.put(key, data);
        }
    }

    @Deprecated
    public Map<String, Object> getMetadata() {
        synchronized (metadataLock) {
            if (metadata == null) {
                return null;
            }
            return new HashMap<>(metadata);
        }
    }

    @Deprecated
    public void setMetadata(Map<String, Object> metadata0) {
        if (metadata0 != null) {
            synchronized (metadataLock) {
                if (metadata == null) {
                    metadata = new HashMap<>(4);
                }
                metadata.clear();
                metadata.putAll(metadata0);
            }
        }
    }

    public <T> T compareAndSwapMetadata(String key, T data, T expected) {
        synchronized (metadataLock) {
            // metadata == null implies that expected needs to be null
            if (metadata == null) {
                if (expected == null) {
                    return null;
                }
                metadata = new HashMap<>();
            }
            T old = (T) metadata.get(key);
            if (old == expected) {
                metadata.put(key, data);
                return (T) data;
            }
            return (T) old;
        }
    }
}
