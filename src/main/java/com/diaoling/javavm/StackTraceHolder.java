package com.diaoling.javavm;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class StackTraceHolder {
    private ClassNode _class;
    private MethodNode _method;
    private AbstractInsnNode _instruction;

    public StackTraceHolder(ClassNode clazz, MethodNode method, AbstractInsnNode instruction) {
        this._class = clazz;
        this._method = method;
        this._instruction = instruction;
    }

    public ClassNode getClassNode() {
        return _class;
    }

    public MethodNode getMethod() {
        return _method;
    }

    public AbstractInsnNode getInstruction() {
        return _instruction;
    }

    public void setInstruction(AbstractInsnNode instruction) {
        this._instruction = instruction;
    }

    @Override
    public String toString() {
        return "at " + _class.name + "." + _method.name + _method.desc;
    }
}
