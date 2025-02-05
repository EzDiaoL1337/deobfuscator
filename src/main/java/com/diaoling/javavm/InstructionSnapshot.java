package com.diaoling.javavm;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;

public class InstructionSnapshot {
    public Set<Pair<Stack, Locals>> _pairs = new HashSet<>();

    public void merge(Stack stack, Locals locals) {
        _pairs.add(Pair.of(stack, locals));
    }
}
