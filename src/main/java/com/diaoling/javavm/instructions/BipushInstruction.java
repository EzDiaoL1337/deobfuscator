package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;

import java.util.List;

public class BipushInstruction extends Instruction {
    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        IntInsnNode cast = (IntInsnNode) currentInsn;
        stack.push(execution.getVM().newByte((byte) cast.operand));
    }
}
