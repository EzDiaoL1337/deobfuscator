package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.utils.TypeHelper;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

import java.util.List;

public class LdcInstruction extends Instruction {
    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        LdcInsnNode cast = (LdcInsnNode) currentInsn;

        Object load = cast.cst;
        if (load instanceof Type) {
            Type type = (Type) load;
            if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
                String bytecodeName = type.getInternalName();
                Type actualType = TypeHelper.parseType(execution.getVM(), bytecodeName);
                if (actualType == null) {
                    throw new ExecutionException("An internal error occurred: Unexpected null Type while loading class");
                }
                stack.push(JavaClass.forName(execution.getVM(), actualType).getOop());
            } else {
                throw new ExecutionException("An internal error occurred: Unexpected sort on loaded type: " + load);
            }
        } else if (load instanceof Integer) {
            stack.push(JavaWrapper.createInteger(execution.getVM(), (Integer) load));
        } else if (load instanceof Float) {
            stack.push(JavaWrapper.createFloat(execution.getVM(), (Float) load));
        } else if (load instanceof Double) {
            stack.push(JavaWrapper.createDouble(execution.getVM(), (Double) load));
        } else if (load instanceof Long) {
            stack.push(execution.getVM().newLong((Long) load));
        } else if (load instanceof String) {
            stack.push(execution.getVM().getStringInterned((String) load));
        } else {
            throw new ExecutionException("An internal error occurred: Unexpected ldc type " + (load == null ? "null" : load.getClass()));
        }
    }
}
