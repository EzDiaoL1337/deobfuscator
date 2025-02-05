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

package com.diaoling.javavm.nativeimpls;

import com.diaoling.javavm.Cause;
import com.diaoling.javavm.Effect;
import com.diaoling.javavm.StackTraceHolder;
import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.hooks.HookGenerator;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.values.JavaWrapper;
import com.diaoling.javavm.*;
import com.diaoling.javavm.hooks.*;
import com.diaoling.javavm.mirrors.*;
import com.diaoling.javavm.values.*;
import org.objectweb.asm.tree.*;

import java.lang.reflect.*;
import java.util.*;

/*
 * Throwables are interesting. Calling fillInStackTrace() will cause the VM to generate the backtrace,
 * but not actually modify the Throwable's accessible fields (the backtrace field will be modified though)
 *
 * Only when getStackTrace() or printStackTrace() is called, is getStackTraceElement() called to lazily fetch the stacktrace
 */
public class java_lang_Throwable {
    private static final String THIS = "java/lang/Throwable";
    public static final String METADATA_BACKTRACE = "backtrace";

    public static void registerNatives(VirtualMachine vm) {
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "fillInStackTrace", "(I)Ljava/lang/Throwable;", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            List<StackTraceHolder> stacktrace = vm.getStacktrace();

            while (!stacktrace.isEmpty()) {
                StackTraceHolder holder = stacktrace.get(0);
                if (holder.getMethod().name.equalsIgnoreCase("fillInStackTrace0") ||
                        holder.getMethod().name.equalsIgnoreCase("fillInStackTrace")) {
                    stacktrace.remove(0);
                    continue;
                }
                if (holder.getMethod().name.equals("<init>") && vm.getSystemDictionary().getJavaLangThrowable().isAssignableFrom(JavaClass.forName(vm, holder.getClassNode().name))) {
                    stacktrace.remove(0);
                    continue;
                }
                break;
            }

            inst.get().setMetadata(METADATA_BACKTRACE, stacktrace);

            return inst;
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "getStackTraceDepth", "()I", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            return JavaWrapper.createInteger(vm, ((List) inst.get().getMetadata(METADATA_BACKTRACE)).size());
        }));
        vm.hook(HookGenerator.generateUnknownHandlingHook(vm, THIS, "getStackTraceElement", "(I)Ljava/lang/StackTraceElement;", false, Cause.NONE, Effect.NONE, (ctx, inst, args) -> {
            JavaClass stackTraceElementClazz = JavaClass.forName(vm, "java/lang/StackTraceElement");

            List<StackTraceHolder> holders = inst.get().getMetadata(METADATA_BACKTRACE);
            List<StackTraceElement> elements = convert(holders);

            StackTraceElement elem = elements.get(args[0].asInt());

            return vm.newInstance(stackTraceElementClazz, "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V",
                    vm.getString(elem.getClassName()),
                    vm.getString(elem.getMethodName()),
                    vm.getString(elem.getFileName()),
                    vm.newInt(elem.getLineNumber())
            );
        }));
    }

    public static List<StackTraceElement> convert(List<StackTraceHolder> stackTraceHolder) {
        if (stackTraceHolder == null) {
            return new ArrayList<>();
        }

        List<StackTraceElement> result = new ArrayList<>();

        for (StackTraceHolder elem : stackTraceHolder) {
            int lineNumber = -1;
            if (Modifier.isNative(elem.getMethod().access)) {
                lineNumber = -2;
            } else {
                if (elem.getInstruction() != null) {
                    AbstractInsnNode target = elem.getInstruction();
                    List<LineNumberNode> lines = new ArrayList<>();
                    for (MethodNode m : elem.getClassNode().methods) {
                        if (m.instructions != null && m.instructions.getFirst() != null) {
                            for (AbstractInsnNode i = m.instructions.getFirst(); i.getNext() != null; i = i.getNext()) {
                                if (i instanceof LineNumberNode) {
                                    lines.add((LineNumberNode) i);
                                }
                            }
                        }
                    }
                    outer:
                    for (AbstractInsnNode i = target.getPrevious(); i != null; i = i.getPrevious()) {
                        if (i instanceof LabelNode) {
                            for (LineNumberNode ln : lines) {
                                if (ln.start.getLabel().equals(((LabelNode) i).getLabel())) {
                                    lineNumber = ln.line;
                                    break outer;
                                }
                            }
                        }
                    }
                }
            }
            result.add(new StackTraceElement(
                    elem.getClassNode().name.replace('/', '.'),
                    elem.getMethod().name,
                    elem.getClassNode().sourceFile,
                    lineNumber
            ));
        }

        return result;
    }
}
