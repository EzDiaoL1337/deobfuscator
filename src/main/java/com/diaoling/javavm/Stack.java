package com.diaoling.javavm;

import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.values.JavaTop;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;

public class Stack {
    private int _index = 0;
    private JavaWrapper[] _values = new JavaWrapper[16];

    public void push(JavaWrapper next) {
        if (next.is(JavaValueType.DOUBLE) || next.is(JavaValueType.LONG)) {
            push(JavaWrapper.createTop());
        }
        if (_index >= _values.length) {
            int newSize = (1 << (Integer.highestOneBit(_values.length) + 1));
            JavaWrapper[] copy = new JavaWrapper[newSize];
            System.arraycopy(_values, 0, copy, 0, _values.length);
            _values = copy;
        }
        _values[_index++] = next;
    }

    public void pushAll(JavaWrapper... next) {
        for (JavaWrapper w : next) {
            push(w);
        }
    }

    public JavaWrapper pop() {
        if (--_index < 0) {
            throw new ExecutionException("Unable to pop operand off empty stack");
        }
        JavaWrapper result = _values[_index];
        _values[_index] = null;
        if (result.is(JavaValueType.DOUBLE) || result.is(JavaValueType.LONG)) {
            JavaWrapper shouldBeTop = pop();
            if (!(shouldBeTop.get() instanceof JavaTop)) {
                throw new RuntimeException("Unexpected " + shouldBeTop);
            }
        }
        return result;
    }

    public JavaWrapper peek() {
        return _values[_index - 1];
    }

    public int size() {
        return _index;
    }

    JavaWrapper[] internalArray() {
        JavaWrapper[] copy = new JavaWrapper[_values.length];
        System.arraycopy(_values, 0, copy, 0, _values.length);
        return copy;
    }

    public Stack copy() {
        Stack newStack = new Stack();
        newStack._values = new JavaWrapper[_values.length];
        newStack._index = _index;
        for (int i = 0; i < _values.length; i++) {
            newStack._values[i] = _values[i];
        }
        return newStack;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = _values.length - 1; i >= 0; i--) {
            if (_values[i] != null) {
                builder.append(i).append(": ").append(_values[i].get()).append(" ");
            }
        }
        return builder.toString();
    }

    public void clear() {
        _values = new JavaWrapper[16];
        _index = 0;
    }
}
