package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.utils.ArrayHelper;
import com.diaoling.javavm.values.JavaUnknown;
import com.diaoling.javavm.values.JavaValue;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class NewArrayInstruction extends Instruction {
    private static Type[] MAPPING = new Type[12];

    static {
        MAPPING[T_BOOLEAN] = Type.BOOLEAN_TYPE;
        MAPPING[T_CHAR] = Type.CHAR_TYPE;
        MAPPING[T_FLOAT] = Type.FLOAT_TYPE;
        MAPPING[T_DOUBLE] = Type.DOUBLE_TYPE;
        MAPPING[T_BYTE] = Type.BYTE_TYPE;
        MAPPING[T_SHORT] = Type.SHORT_TYPE;
        MAPPING[T_INT] = Type.INT_TYPE;
        MAPPING[T_LONG] = Type.LONG_TYPE;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        IntInsnNode cast = (IntInsnNode) currentInsn;

        JavaValue lengthValue = stack.pop().get();

        if (!lengthValue.is(JavaValueType.UNKNOWN)) {
            stack.push(ArrayHelper.newInstance(execution.getVM(), MAPPING[cast.operand], lengthValue.asInt()));
        } else {
            stack.push(JavaWrapper.wrap(new JavaUnknown(execution.getVM(), JavaClass.forName(execution.getVM(), MAPPING[cast.operand]), JavaUnknown.UnknownCause.ANEWARRAY, lengthValue)));
        }
    }
}
