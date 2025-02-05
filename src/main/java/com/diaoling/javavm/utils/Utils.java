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

package com.diaoling.javavm.utils;

import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.values.JavaArray;
import com.diaoling.javavm.values.JavaObject;
import com.diaoling.javavm.values.JavaValue;
import com.diaoling.javavm.values.prim.JDouble;
import com.diaoling.javavm.values.prim.JFloat;
import com.diaoling.javavm.values.prim.JInteger;
import com.diaoling.javavm.values.prim.JLong;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.*;

public class Utils {
    private static final ThreadLocal<Printer> printer = ThreadLocal.withInitial(() -> new Textifier());
    private static final ThreadLocal<TraceMethodVisitor> methodPrinter = ThreadLocal.withInitial(() -> new TraceMethodVisitor(printer.get()));

    public static long copy(InputStream from, OutputStream to) throws IOException {
        byte[] buf = new byte[4096];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    public static void sneakyThrow(Throwable t) {
        Utils.<Error>sneakyThrow0(t);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void sneakyThrow0(Throwable t) throws T {
        throw (T) t;
    }

    public static String prettyprint(AbstractInsnNode insnNode) {
        insnNode.accept(methodPrinter.get());
        StringWriter sw = new StringWriter();
        printer.get().print(new PrintWriter(sw));
        printer.get().getText().clear();
        return sw.toString().trim();
    }

    public static boolean isObjectOfType(Type type, JavaValue object) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
                if (!(object instanceof JInteger)) {
                    throw new ExecutionException("Expected JavaBoolean, got " + object);
                }
                break;
            case Type.CHAR:
                if (!(object instanceof JInteger)) {
                    throw new ExecutionException("Expected JavaCharacter, got " + object);
                }
                break;
            case Type.BYTE:
                if (!(object instanceof JInteger)) {
                    throw new ExecutionException("Expected JavaByte, got " + object);
                }
                break;
            case Type.SHORT:
                if (!(object instanceof JInteger)) {
                    throw new ExecutionException("Expected JavaShort, got " + object);
                }
                break;
            case Type.INT:
                if (!(object instanceof JInteger)) {
                    throw new ExecutionException("Expected JavaInteger, got " + object);
                }
                break;
            case Type.FLOAT:
                if (!(object instanceof JFloat)) {
                    throw new ExecutionException("Expected JavaFloat, got " + object);
                }
                break;
            case Type.LONG:
                if (!(object instanceof JLong)) {
                    throw new ExecutionException("Expected JavaLong, got " + object);
                }
                break;
            case Type.DOUBLE:
                if (!(object instanceof JDouble)) {
                    throw new ExecutionException("Expected JavaDouble, got " + object);
                }
                break;
            case Type.ARRAY:
            case Type.OBJECT:
                if (!(object instanceof JavaObject) && !(object instanceof JavaArray)) {
                    throw new ExecutionException("Expected JavaObject/JavaArray, got " + object);
                }
                break;
        }
        return true;
    }
}
