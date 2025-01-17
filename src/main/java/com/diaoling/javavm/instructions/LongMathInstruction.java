package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.utils.BiLongFunction;
import com.diaoling.javavm.utils.ExecutionUtils;
import com.diaoling.javavm.values.JavaUnknown;
import com.diaoling.javavm.values.JavaValue;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

public class LongMathInstruction extends Instruction {
    private final BiLongFunction _function;
    private final boolean _returnInt;

    public LongMathInstruction(BiLongFunction function, boolean returnInt) {
        this._function = function;
        this._returnInt = returnInt;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        JavaValue b = stack.pop().get();
        JavaValue a = stack.pop().get();

        if (ExecutionUtils.areValuesUnknown(a, b)) {
            stack.push(JavaWrapper.wrap(new JavaUnknown(execution.getVM(), execution.getVM().LONG, JavaUnknown.UnknownCause.LONG_MATH, b, a)));
            return;
        }

        if (!a.is(JavaValueType.LONG) || !b.is(JavaValueType.LONG)) {
            throw new ExecutionException("Expected to find long on stack");
        }

        long result = _function.apply(a.asLong(), b.asLong());
        if (!_returnInt) {
            stack.push(execution.getVM().newLong(result));
        } else {
            stack.push(JavaWrapper.createInteger(execution.getVM(), (int) result));
        }
    }
}
