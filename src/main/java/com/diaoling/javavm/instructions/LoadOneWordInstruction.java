package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;
import java.util.function.Function;

public class LoadOneWordInstruction extends Instruction {
    private final Function<VirtualMachine, JavaWrapper> _javaValueSupplier;

    public LoadOneWordInstruction(Function<VirtualMachine, JavaWrapper> supplier) {
        _javaValueSupplier = supplier;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        stack.push(_javaValueSupplier.apply(execution.getVM()));
    }
}
