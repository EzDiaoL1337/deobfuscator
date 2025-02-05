package com.diaoling.javavm.utils;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMHelper {
    private ASMHelper() {

    }

    public static MethodNode findMethod(ClassNode owner, String name, String desc) {
        int size = owner.methods.size();
        for (int i = 0; i < size; i++) {
            MethodNode methodNode = owner.methods.get(i);
            if (methodNode.name.equals(name) && methodNode.desc.equals(desc)) {
                return methodNode;
            }
        }

        return null;
    }

    public static FieldNode findField(ClassNode owner, String name, String desc) {
        int size = owner.fields.size();
        for (int i = 0; i < size; i++) {
            FieldNode fieldNode = owner.fields.get(i);
            if (fieldNode.name.equals(name) && fieldNode.desc.equals(desc)) {
                return fieldNode;
            }
        }

        return null;
    }

    public static InnerClassNode findInnerClassNode(ClassNode classNode, String name) {
        if (classNode.innerClasses == null) {
            return null;
        }
        int size = classNode.innerClasses.size();
        for (int i = 0; i < size; i++) {
            InnerClassNode innerClassNode = classNode.innerClasses.get(i);
            if (innerClassNode.name.equals(name)) {
                return innerClassNode;
            }
        }

        return null;
    }

    public static Attribute findAttribute(ClassNode classNode, String name) {
        if (classNode.attrs == null) {
            return null;
        }
        int size = classNode.attrs.size();
        for (int i = 0; i < size; i++) {
            Attribute attribute = classNode.attrs.get(i);
            if (attribute.type.equals(name)) {
                return attribute;
            }
        }

        return null;
    }
}
