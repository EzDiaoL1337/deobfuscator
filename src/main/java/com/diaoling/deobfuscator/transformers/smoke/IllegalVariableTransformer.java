package com.diaoling.deobfuscator.transformers.smoke;

import com.diaoling.deobfuscator.config.TransformerConfig;
import com.diaoling.deobfuscator.transformers.Transformer;

import java.util.stream.Collectors;

public class IllegalVariableTransformer extends Transformer<TransformerConfig> {
    @Override
    public boolean transform() throws Throwable {
        classNodes().forEach(classNode -> {
            classNode.methods.stream().filter(methodNode -> methodNode.localVariables != null).forEach(methodNode -> {
                methodNode.localVariables = methodNode.localVariables.stream().filter(localVariableNode -> (int) localVariableNode.name.charAt(0) <= 128).collect(Collectors.toList());
            });
        });
        return true;
    }
} 
