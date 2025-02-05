package com.diaoling.javavm;

import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashMap;
import java.util.Map;

public class MethodExecution {
    private final VirtualMachine _virtualMachine;
    private final ClassNode _classNode;
    private final MethodNode _methodNode;
    private final ExecutionOptions _options;

    private final InstructionSnapshot[] _instructions;
    public Map<AbstractInsnNode, Integer> Visited = new HashMap<>();
    private JavaWrapper _returnValue;

    public MethodExecution(VirtualMachine vm, ClassNode classNode, MethodNode methodNode, ExecutionOptions options) {
        this._virtualMachine = vm;
        this._classNode = classNode;
        this._methodNode = methodNode;
        this._instructions = new InstructionSnapshot[methodNode.instructions.size()];
        this._options = options;
    }

    public InstructionSnapshot[] getInstructions() {
        return this._instructions;
    }

    public ClassNode getClassNode() {
        return this._classNode;
    }

    public MethodNode getMethodNode() {
        return this._methodNode;
    }

    public ExecutionOptions getOptions() {
        return this._options;
    }

    public JavaWrapper getReturnValue() {
        return this._returnValue;
    }

    void setReturnValue(JavaWrapper value) {
        this._returnValue = value;
    }

    public VirtualMachine getVM() {
        return _virtualMachine;
    }
}
