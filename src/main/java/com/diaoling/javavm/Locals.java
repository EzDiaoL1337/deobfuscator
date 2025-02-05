package com.diaoling.javavm;

import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.values.JavaWrapper;

public class Locals {
    public JavaWrapper[] _values = new JavaWrapper[16];

    public JavaWrapper get(int index) {
        if (index >= _values.length) {
            throw new ExecutionException("Illegal local variable number");
        }
        JavaWrapper value = _values[index];
        if (value == null) {
            throw new ExecutionException("Accessing value from uninitialized register " + index);
        }
        return value;
    }

    public JavaWrapper remove(int index) {
        if (index >= _values.length) {
            throw new ExecutionException("Illegal local variable number");
        }
        JavaWrapper value = _values[index];
        if (value == null) {
            throw new ExecutionException("Accessing value from uninitialized register " + index);
        }
        _values[index] = null;
        return value;
    }

    public void set(int index, JavaWrapper value) {
        if (index >= _values.length) {
            int newSize = (1 << (Integer.highestOneBit(_values.length) + 1));
            JavaWrapper[] copy = new JavaWrapper[newSize];
            System.arraycopy(_values, 0, copy, 0, _values.length);
            _values = copy;
        }

        _values[index] = value;
    }

    public int size() {
        return _values.length;
    }

    JavaWrapper[] internalArray() {
        JavaWrapper[] copy = new JavaWrapper[_values.length];
        System.arraycopy(_values, 0, copy, 0, _values.length);
        return copy;
    }

    public Locals copy() {
        Locals newLocals = new Locals();
        newLocals._values = new JavaWrapper[_values.length];
        for (int i = 0; i < _values.length; i++) {
            newLocals._values[i] = _values[i];
        }
        return newLocals;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < _values.length; i++) {
            if (_values[i] != null) {
                builder.append(i).append(": ").append(_values[i].get()).append(" ");
            }
        }
        return builder.toString();
    }
}
