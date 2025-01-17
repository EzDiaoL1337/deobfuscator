package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.internals.VMSymbols;
import com.diaoling.javavm.utils.BiIntegerFunction;
import com.diaoling.javavm.utils.ExecutionUtils;
import com.diaoling.javavm.values.JavaUnknown;
import com.diaoling.javavm.values.JavaValue;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

public class IntegerMathInstruction extends Instruction {
    private final BiIntegerFunction _function;
    private final boolean division;

    public IntegerMathInstruction(BiIntegerFunction function) {
        this._function = function;
        this.division = false;
    }

    public IntegerMathInstruction(BiIntegerFunction function, boolean division) {
        this._function = function;
        this.division = division;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        JavaValue b = stack.pop().get();
        JavaValue a = stack.pop().get();

        if (ExecutionUtils.areValuesUnknown(a, b)) {
            stack.push(JavaWrapper.wrap(new JavaUnknown(execution.getVM(), execution.getVM().INTEGER, JavaUnknown.UnknownCause.INTEGER_MATH, b, a)));
            return;
        }

        if (!a.is(JavaValueType.INTEGER) || !b.is(JavaValueType.INTEGER)) {
            throw new ExecutionException("Expected to find integer on stack");
        }

        if (division && b.asInt() == 0) {
            throw execution.getVM().newThrowable(VMSymbols.java_lang_ArithmeticException, "/ by zero");
        }

        stack.push(JavaWrapper.createInteger(execution.getVM(), _function.apply(a.asInt(), b.asInt())));
    }
}
