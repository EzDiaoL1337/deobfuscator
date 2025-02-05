package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.internals.VMSymbols;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.mirrors.JavaMethodHandle;
import com.diaoling.javavm.utils.ASMHelper;
import com.diaoling.javavm.utils.TypeHelper;
import com.diaoling.javavm.values.JavaUnknown;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

public class InvocationInstruction extends Instruction {

    private final boolean _isStatic;

    public InvocationInstruction(boolean isStatic) {
        this._isStatic = isStatic;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        MethodInsnNode cast = (MethodInsnNode) currentInsn;

        Type returnType = Type.getReturnType(cast.desc);

        JavaWrapper[] args = new JavaWrapper[TypeHelper.getNumberOfArguments(cast.desc)];
        for (int i = args.length - 1; i >= 0; i--) {
            args[i] = stack.pop();
        }

        JavaWrapper instance = null;
        if (!_isStatic) {
            instance = stack.pop();
            if (instance.is(JavaValueType.NULL)) {
                throw execution.getVM().newThrowable(VMSymbols.java_lang_NullPointerException);
            }
        }

        JavaWrapper provided;

        ClassNode targetNode;
        if (instance != null) { //bleh
            if (instance.getJavaClass().isArray()) {
                targetNode = execution.getVM().getSystemDictionary().getJavaLangObject().getClassNode();
            } else {
                targetNode = instance.asObject().getOriginalClass().getClassNode();
            }
        } else {
            targetNode = execution.getVM().lookupClass(cast.owner);
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

            VMSymbols.VMIntrinsics id = JavaMethodHandle.signaturePolymorphicNameId(cast.name);
            if (id != VMSymbols.VMIntrinsics.NONE) {
                if (cast.owner.equals("java/lang/invoke/MethodHandle")) {
                    targetNode = execution.getVM().getSystemDictionary().getJavaLangInvokeMethodHandle().getClassNode();
                    target = ASMHelper.findMethod(execution.getVM().getSystemDictionary().getJavaLangInvokeMethodHandle().getClassNode(), cast.name, cast.desc);
                    if (target == null) {
                        target = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_NATIVE, cast.name, cast.desc, null, null);
                        if (JavaMethodHandle.isIntrinsicStatic(id)) {
                            target.access |= Opcodes.ACC_STATIC;
                        }
                        targetNode.methods.add(target);
                        execution.getVM().getJavaLangInvokeMethodHandle().hookPolymorphicSignature(target);
                    }
                }
            }

            if (target != null) {
                if (_isStatic) {
                    execution.getVM().initialize(JavaClass.forName(execution.getVM(), targetNode.name));
                }
                provided = execution.getVM().internalExecute(targetNode, target, instance, args, currentInsn);

                if (returnType.getSort() != Type.VOID && provided == null) {
                    throw new ExecutionException("Null result from " + targetNode.name + " " + target.name + target.desc + " " + Arrays.toString(args));
                }
            } else {
                throw new ExecutionException("IncompatibleClassChangeError: Method not found " + cast.owner + "." + cast.name + "." + cast.desc);
            }
        } else {
            provided = JavaWrapper.wrap(new JavaUnknown(execution.getVM(), JavaClass.forName(execution.getVM(), returnType), "Invocation on " + cast.owner + " " + cast.name + cast.desc));
        }

        if (returnType.getSort() != Type.VOID) {
            // todo verify
            stack.push(provided);
        }
    }
}