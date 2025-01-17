package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.utils.MaybeBoolean;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;

import java.util.List;
import java.util.function.Function;

public class SinglePredicateInstruction extends Instruction {

    private final Function<JavaWrapper, MaybeBoolean> _handler;

    public SinglePredicateInstruction(Function<JavaWrapper, MaybeBoolean> handler) {
        this._handler = handler;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        JumpInsnNode jumpInsnNode = (JumpInsnNode) currentInsn;

        JavaWrapper top = stack.pop();
        MaybeBoolean result = _handler.apply(top);
        switch (result) {
            case YES:
                branchTo.add(jumpInsnNode.label);
                break;
            case MAYBE:
                branchTo.add(jumpInsnNode.label);
                branchTo.add(jumpInsnNode.getNext());
                break;
            case NO:
                break;
        }
    }
}
