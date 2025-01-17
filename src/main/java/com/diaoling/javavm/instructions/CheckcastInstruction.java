package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.utils.TypeHelper;
import com.diaoling.javavm.values.JavaValueType;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import java.util.List;

public class CheckcastInstruction extends Instruction {
    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        TypeInsnNode cast = (TypeInsnNode) currentInsn;

        Type desc = TypeHelper.parseType(execution.getVM(), cast.desc);
        JavaClass other = JavaClass.forName(execution.getVM(), desc);
        JavaWrapper wrappedValue = stack.pop();
        if (wrappedValue.is(JavaValueType.NULL)) {
            stack.push(wrappedValue);
            return;
        }

        stack.push(wrappedValue);
//        if (!wrappedValue.get().isInstanceOf(desc)) {
//            System.out.println(desc + " "+ other);
//            System.out.println(wrappedValue.getJavaClass().getName() + " cannot be cast to " + other.getName());
//            throw new ExecutionException("asdf"); //execution.getVM().newThrowable(VMSymbols.java_lang_ClassCastException, wrappedValue.getJavaClass().getName() + " cannot be cast to " + other.getName());
//        }
//
//        if (wrappedValue.get() instanceof JavaObject) {
//            stack.push(JavaWrapper.wrap(wrappedValue.asObject().checkcast(other)));
//        } else {
//            stack.push(JavaWrapper.wrap(wrappedValue.asArray().checkcast(other)));
//        }
    }
}
