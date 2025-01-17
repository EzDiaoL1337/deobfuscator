package com.diaoling.javavm.instructions;

import com.diaoling.javavm.Locals;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.Stack;
import com.diaoling.javavm.mirrors.JavaClass;
import com.diaoling.javavm.utils.TypeHelper;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import java.util.List;

public class NewInstruction extends Instruction {
    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        TypeInsnNode cast = (TypeInsnNode) currentInsn;

        Type descType = TypeHelper.parseType(execution.getVM(), cast.desc);
        JavaClass clazz = JavaClass.forName(execution.getVM(), descType);

        execution.getVM().initialize(clazz);

        stack.push(JavaWrapper.createUninitialized(clazz, cast.desc));
    }
}
