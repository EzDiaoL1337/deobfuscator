/*
 * Copyright 2018 Sam Sun <github-contact@samczsun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.diaoling.deobfuscator.rules.stringer;

import com.diaoling.deobfuscator.Deobfuscator;
import com.diaoling.deobfuscator.rules.Rule;
import com.diaoling.deobfuscator.transformers.Transformer;
import com.diaoling.deobfuscator.transformers.stringer.StringEncryptionTransformer;
import com.diaoling.deobfuscator.utils.TransformerHelper;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Modifier;
import java.util.*;

public class RuleStringDecryptor implements Rule, Opcodes {
    @Override
    public String getDescription() {
        return "This variant of Stringer's string decryptor can be identified by its use of Thread.currentThread().getStackTrace() and heavy math operations";
    }

    @Override
    public String test(Deobfuscator deobfuscator) {
    	for (ClassNode classNode : deobfuscator.getClasses().values()) {
    		for (MethodNode methodNode : classNode.methods) {
    			if (!methodNode.desc.equals("(Ljava/lang/String;)Ljava/lang/String;")
    				|| !Modifier.isStatic(methodNode.access) || methodNode.instructions == null)
    				continue;
    			
    			boolean isStringer = true;
    			
    			isStringer = isStringer && TransformerHelper.containsInvokeStatic(methodNode, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;");
    			isStringer = isStringer && TransformerHelper.containsInvokeVirtual(methodNode, "java/lang/Thread", "getStackTrace", "()[Ljava/lang/StackTraceElement;");
    			isStringer = isStringer && TransformerHelper.countOccurencesOf(methodNode, IAND) > 20;
    			isStringer = isStringer && TransformerHelper.countOccurencesOf(methodNode, IXOR) > 20;
    			isStringer = isStringer && TransformerHelper.countOccurencesOf(methodNode, IUSHR) > 10;
    			isStringer = isStringer && TransformerHelper.countOccurencesOf(methodNode, ISHL) > 10;
    			
    			if (!isStringer) {
    				continue;
    			}
    			
    			return "Found possible string decryption class " + classNode.name;
    		}
    	}
    	
    	return null;
    }

    @Override
    public Collection<Class<? extends Transformer<?>>> getRecommendTransformers() {
    	return Collections.singletonList(StringEncryptionTransformer.class);
    }
}
