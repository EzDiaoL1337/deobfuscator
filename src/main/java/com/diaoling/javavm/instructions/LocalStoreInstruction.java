package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.List;

public class LocalStoreInstruction extends Instruction {

    private final int _sort;

    public LocalStoreInstruction(int sort) {
        if (sort == Type.VOID || sort == Type.ARRAY || sort == Type.METHOD || sort == Type.BYTE || sort == Type.BOOLEAN || sort == Type.SHORT || sort == Type.CHAR) {
            throw new InternalError("Tried to register LocalStoreInstruction with illegal sort: " + sort);
        }
        this._sort = sort;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        VarInsnNode varInsnNode = (VarInsnNode) currentInsn;
        JavaWrapper top = stack.pop();

        switch (_sort) {
            case Type.INT:
                if (!top.is(JavaValueType.INTEGER)) {
                    throw new ExecutionException("Register " + varInsnNode.var + " contains wrong type. Expected INTEGER, got (" + top.get().getClass() + ")");
                }
                locals.set(varInsnNode.var, top);
                break;
            case Type.FLOAT:
                if (!top.is(JavaValueType.FLOAT)) {
                    throw new ExecutionException("Register " + varInsnNode.var + " contains wrong type. Expected FLOAT, got (" + top.get().getClass() + ")");
                }
                locals.set(varInsnNode.var, top);
                break;
            case Type.OBJECT:
                if (!top.is(JavaValueType.OBJECT) && !top.is(JavaValueType.UNINITIALIZED)) {
                    throw new ExecutionException("Register " + varInsnNode.var + " contains wrong type. Expected OBJECT, got (" + top.get().getClass() + ")");
                }
                locals.set(varInsnNode.var, top);
                break;
            case Type.DOUBLE:
                if (!top.is(JavaValueType.DOUBLE)) {
                    throw new ExecutionException("Register " + varInsnNode.var + " contains wrong type. Expected DOUBLE, got (" + top.get().getClass() + ")");
                }
                locals.set(varInsnNode.var, top);
                break;
            case Type.LONG:
                if (!top.is(JavaValueType.LONG)) {
                    throw new ExecutionException("Register " + varInsnNode.var + " contains wrong type. Expected LONG, got (" + top.get().getClass() + ")");
                }
                locals.set(varInsnNode.var, top);
                break;
            default:
                throw new ExecutionException("An internal error occurred: Unhandled sort " + _sort);
        }
    }
}
