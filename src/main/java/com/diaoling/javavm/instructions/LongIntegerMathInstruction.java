package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.utils.BiLongIntegerFunction;
import com.diaoling.javavm.utils.ExecutionUtils;
import com.diaoling.javavm.values.JavaUnknown;
import com.diaoling.javavm.values.JavaValue;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

public class LongIntegerMathInstruction extends Instruction {
    private final BiLongIntegerFunction _function;

    public LongIntegerMathInstruction(BiLongIntegerFunction function) {
        this._function = function;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        JavaValue b = stack.pop().get();
        JavaValue a = stack.pop().get();

        if (ExecutionUtils.areValuesUnknown(a, b)) {
            stack.push(JavaWrapper.wrap(new JavaUnknown(execution.getVM(), execution.getVM().LONG, JavaUnknown.UnknownCause.LONG_INTEGER_MATH, b, a)));
            return;
        }

        if (!b.is(JavaValueType.INTEGER)) {
            throw new ExecutionException("Expected to find integer on stack");
        }
        if (!a.is(JavaValueType.LONG)) {
            throw new ExecutionException("Expected to find long on stack");
        }

        stack.push(execution.getVM().newLong(_function.apply(a.asLong(), b.asInt())));
    }
}
