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

package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.utils.ASMHelper;
import com.diaoling.javavm.utils.TypeHelper;
import com.diaoling.javavm.values.JavaUnknown;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Modifier;
import java.util.List;

public class InvokeSpecialInstruction extends Instruction {
    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        MethodInsnNode cast = (MethodInsnNode) currentInsn;

        Type returnType = Type.getReturnType(cast.desc);

        JavaWrapper[] args = new JavaWrapper[TypeHelper.getNumberOfArguments(cast.desc)];
        for (int i = args.length - 1; i >= 0; i--) {
            args[i] = stack.pop();
        }

        JavaWrapper instance = stack.pop();

        JavaWrapper provided;
        ClassNode targetNode;

        ClassNode specifiedClass = execution.getVM().lookupClass(cast.owner);
        if (cast.name.equals("<init>")) {
            // sometimes we might not be able to resolve the class because the instance is Unsafe.defineAnonymousClass()'d
            // therefore, we load it from the instance itself when possible
            if (cast.owner.equals(instance.getJavaClass().getClassNode().name)) {
                targetNode = instance.getJavaClass().getClassNode();
            } else {
                targetNode = specifiedClass;
            }
        } else if (Modifier.isInterface(specifiedClass.access) || !execution.getClassNode().superName.equals(cast.owner)) {
            targetNode = specifiedClass;
        } else if ((execution.getClassNode().access & Opcodes.ACC_SUPER) != Opcodes.ACC_SUPER) {
            targetNode = specifiedClass;
        } else {
            targetNode = execution.getVM().lookupClass(execution.getClassNode().superName);
        }
        if (targetNode != null) {
            MethodNode target = ASMHelper.findMethod(targetNode, cast.name, cast.desc);
            while (target == null) {
                // todo permissionssssssss
                if (targetNode.name.equalsIgnoreCase("java/lang/Object"))
                    break;
                targetNode = execution.getVM().lookupClass(targetNode.superName);
                if (targetNode == null)
                    break;
                target = ASMHelper.findMethod(targetNode, cast.name, cast.desc);
            }

            if (target != null) {
                provided = execution.getVM().internalExecute(targetNode, target, instance, args, currentInsn);
            } else {
                throw new ExecutionException("IncompatibleClassChangeError: Method not found " + cast.owner + "." + cast.name + "." + cast.desc);
            }
        } else {
            if (cast.name.equals("<init>")) {
                throw new ExecutionException("Could not initialize class " + cast.owner + " " + cast.name + cast.desc);
            }
            provided = JavaWrapper.wrap(new JavaUnknown(execution.getVM(), JavaClass.forName(execution.getVM(), returnType), "Invocation on " + cast.owner + " " + cast.name + cast.desc));
        }


        if (returnType.getSort() != Type.VOID) {
            stack.push(provided);
        }
    }
}