package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.utils.ExecutionUtils;
import com.diaoling.javavm.values.JavaArray;
import com.diaoling.javavm.values.JavaUnknown;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

public class ArrayStoreInstruction extends Instruction {
    private final int _sort;

    public ArrayStoreInstruction(int sort) {
        if (sort == Type.VOID || sort == Type.ARRAY || sort == Type.METHOD) {
            throw new InternalError("Tried to register ArrayStoreInstruction with illegal sort: " + sort);
        }
        this._sort = sort;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        JavaWrapper storeValue = stack.pop();
        JavaWrapper arrayIndexValue = stack.pop();
        JavaWrapper arrayValue = stack.pop();

        if (!arrayValue.is(JavaValueType.ARRAY)) {
            throw new ExecutionException("Expecting to find Array on type, instead found " + arrayValue.getJavaClass() + " on " + arrayValue);
        }

        if (ExecutionUtils.areValuesUnknown(arrayValue, arrayIndexValue)) {
            if (arrayValue.is(JavaValueType.UNKNOWN)) {
                ((JavaUnknown) arrayValue.get()).merge("Array store operation on this at " + arrayIndexValue + " value " + storeValue);
            } else if (arrayIndexValue.is(JavaValueType.UNKNOWN)) {
                arrayValue.set(new JavaUnknown(execution.getVM(), arrayValue.getJavaClass(), "Array store operation"));
            } else {
                throw new ExecutionException("An internal error occurred: Unhandled condition");
            }
            return;
        }

        if (!arrayIndexValue.is(JavaValueType.INTEGER)) {
            throw new ExecutionException("Expecting to find Integer on stack");
        }
        if (!arrayValue.is(JavaValueType.ARRAY)) {
            throw new ExecutionException("Expecting to find Array on stack, found " + arrayValue);
        }

        int index = arrayIndexValue.asInt();
        JavaArray array = ((JavaArray) arrayValue.get());

        array.set(index, storeValue);
    }
}
