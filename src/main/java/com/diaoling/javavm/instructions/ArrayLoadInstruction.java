package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.internals.VMSymbols;
import com.diaoling.javavm.utils.ExecutionUtils;
import com.diaoling.javavm.values.JavaArray;
import com.diaoling.javavm.values.JavaUnknown;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

public class ArrayLoadInstruction extends Instruction {
    private final int _sort;

    public ArrayLoadInstruction(int sort) {
        if (sort == Type.VOID || sort == Type.ARRAY || sort == Type.METHOD) {
            throw new InternalError("Tried to register ArrayLoadInstruction with illegal sort: " + sort);
        }
        this._sort = sort;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        JavaWrapper arrayIndex = stack.pop();
        JavaWrapper arrayInstance = stack.pop();

        if (!arrayInstance.is(JavaValueType.ARRAY)) {
            throw new ExecutionException("Expecting to find Array on type, instead found " + arrayInstance.getJavaClass() + " on " + arrayInstance);
        }

        if (arrayInstance.is(JavaValueType.NULL)) {
            throw execution.getVM().newThrowable(VMSymbols.java_lang_NullPointerException);
        }

        Type originalType = arrayInstance.getJavaClass().internalGetType();
        Type resultingType = /*originalType.getElementType(1)*/Type.getType(originalType.getDescriptor().substring(1));

        if (resultingType.getSort() != _sort) {
            if ((resultingType.getSort() == Type.ARRAY && _sort == Type.OBJECT)) {

            } else {
                if (_sort == Type.BYTE && (resultingType.getSort() == Type.BOOLEAN)) {
                    // this is ok
                } else {
                    throw new ExecutionException("Expected " + _sort + ", but found " + resultingType.getSort());
                }
            }
        }

        if (!arrayIndex.is(JavaValueType.INTEGER)) {
            throw new ExecutionException("Expecting to find Integer on stack");
        }

        if (ExecutionUtils.areValuesUnknown(arrayInstance, arrayIndex)) {
            stack.push(JavaWrapper.wrap(new JavaUnknown(execution.getVM(), arrayInstance.getJavaClass().getComponentType(), "Array load operation on (" + arrayInstance + ") index (" + arrayIndex + ")")));
            return;
        }

        int index = arrayIndex.asInt();
        JavaArray array = ((JavaArray) arrayInstance.get());

        if (index >= array.length()) {
            throw execution.getVM().newThrowable(VMSymbols.java_lang_ArrayIndexOutOfBoundsException, String.valueOf(index));
        }

        stack.push(array.get(index));
    }
}
