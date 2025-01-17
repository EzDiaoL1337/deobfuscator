package com.diaoling.deobfuscator.rules.antireleak;

import java.util.Collection;
import java.util.Collections;

import com.diaoling.deobfuscator.Deobfuscator;
import com.diaoling.deobfuscator.rules.Rule;
import com.diaoling.deobfuscator.transformers.Transformer;
import com.diaoling.deobfuscator.transformers.antireleak.InvokedynamicTransformer;

public class RuleInvokeDynamic implements Rule {

    @Override
    public String getDescription() {
        return "AntiReleak uses Invokedynamic instructions with specific or with many arguments to obfuscate method calls";
    }

    @Override
    public String test(Deobfuscator deobfuscator) {
        int invokeDynamicCount = InvokedynamicTransformer.findInvokeDynamic(deobfuscator.getClasses().values());
        if (invokeDynamicCount > 0) {
            return "Found " + invokeDynamicCount + " potential AntiReleak Invokedynamic instructions";
        }
        return null;
    }

    @Override
    public Collection<Class<? extends Transformer<?>>> getRecommendTransformers() {
        return Collections.singleton(InvokedynamicTransformer.class);
    }
}
