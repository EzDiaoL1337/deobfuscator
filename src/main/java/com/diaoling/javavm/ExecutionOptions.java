package com.diaoling.javavm;

import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ExecutionOptions {

    private Map<AbstractInsnNode, Consumer<BreakpointInfo>> _watchlist = new HashMap<>();
    private List<StackTraceHolder> _stacktrace = new ArrayList<>();

    public ExecutionOptions watch(AbstractInsnNode insn, Consumer<BreakpointInfo> consumer) {
        _watchlist.put(insn, consumer);
        return this;
    }

    public boolean shouldRecord(AbstractInsnNode target) {
        return !_watchlist.isEmpty() && _watchlist.containsKey(target);
    }

    public void notify(AbstractInsnNode node, BreakpointInfo info) {
        _watchlist.get(node).accept(info);
    }

    public static class BreakpointInfo {
        private AbstractInsnNode now;
        private Stack stack;
        private Locals locals;

        public BreakpointInfo(AbstractInsnNode now, Stack stack, Locals locals) {
            this.now = now;
            this.stack = stack;
            this.locals = locals;
        }

        public Stack getStack() {
            return stack;
        }

        public Locals getLocals() {
            return locals;
        }

        public AbstractInsnNode getNow() {
            return now;
        }
    }
}
