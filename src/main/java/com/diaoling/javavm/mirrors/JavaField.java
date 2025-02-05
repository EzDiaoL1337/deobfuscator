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

package com.diaoling.javavm.mirrors;

import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.utils.ArrayConversionHelper;
import com.diaoling.javavm.utils.TypeHelper;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.lang.reflect.Modifier;

public class JavaField {
    private final JavaClass clazz;
    private final FieldNode field;

    public JavaField(JavaClass klass, FieldNode methodNode) {
        this.clazz = klass;
        this.field = methodNode;
    }

    public int getModifiers() {
        return field.access & VirtualMachine.JVM_RECOGNIZED_FIELD_MODIFIERS;
    }

    public String getName() {
        return field.name;
    }

    public JavaClass getDeclaringClass() {
        return this.clazz;
    }

    public JavaClass getType() {
        Type type = Type.getType(field.desc);
        return JavaClass.forName(clazz.getVM(), type);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
        result = prime * result + ((field == null) ? 0 : field.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JavaField other = (JavaField) obj;
        if (clazz == null) {
            if (other.clazz != null)
                return false;
        } else if (!clazz.equals(other.clazz))
            return false;
        if (field == null) {
            if (other.field != null)
                return false;
        } else if (!field.equals(other.field))
            return false;
        return true;
    }

    public FieldNode getFieldNode() {
        return field;
    }

    @Override
    public String toString() {
        int mod = getModifiers();
        return (((mod == 0) ? "" : (Modifier.toString(mod) + " "))
                + getType().getTypeName() + " "
                + getDeclaringClass().getTypeName() + "."
                + getName());
    }

    public ClassNode getClassNode() {
        return clazz.getClassNode();
    }

    public JavaWrapper getOop() {
        JavaWrapper oopType = getType().getOop();
        JavaClass fieldClazz = JavaClass.forName(clazz.getVM(), TypeHelper.getTypeByDescriptor("Ljava/lang/reflect/Field;"));
        JavaWrapper oopField = clazz.getVM().newInstance(fieldClazz, "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/Class;IILjava/lang/String;[B)V",
                clazz.getOop(),
                clazz.getVM().getStringInterned(getName()),
                oopType,
                JavaWrapper.createInteger(clazz.getVM(), getModifiers()),
                JavaWrapper.createInteger(clazz.getVM(), getSlot()),
                clazz.getVM().getString(""),
                ArrayConversionHelper.convertByteArray(clazz.getVM(), new byte[0])
        );
        return oopField;
    }

    public int getSlot() {
        return clazz.getClassNode().fields.indexOf(field);
    }
}