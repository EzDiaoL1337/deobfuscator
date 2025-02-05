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

package com.diaoling.deobfuscator.rules.zelix;

import com.diaoling.deobfuscator.Deobfuscator;
import com.diaoling.deobfuscator.rules.Rule;
import com.diaoling.deobfuscator.transformers.Transformer;
import com.diaoling.deobfuscator.transformers.zelix.string.EnhancedStringEncryptionTransformer;
import com.diaoling.deobfuscator.utils.TransformerHelper;
import com.diaoling.deobfuscator.transformers.zelix.StringEncryptionTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.lang.reflect.*;
import java.util.*;

public class RuleEnhancedStringEncryption implements Rule, Opcodes {
    @Override
    public String getDescription() {
        return "Zelix Klassmaster has several modes of string encryption. " +
                "This mode is similar to the simple mode, but adds an additional layer of decryption by " +
                "calling a method with signature (II)Ljava/lang/String;";
    }

    @Override
    public String test(Deobfuscator deobfuscator) {
        if (new RuleSuspiciousClinit().test(deobfuscator) == null) {
            return null;
        }
        if (new RuleMethodParameterChangeStringEncryption().test(deobfuscator) != null) {
            return null;
        }

        for (ClassNode classNode : deobfuscator.getClasses().values()) {
            MethodNode enhanced = TransformerHelper.findMethodNode(classNode, null, "(II)Ljava/lang/String;");
            if (enhanced == null || enhanced.instructions == null) {
                continue;
            }
            if (!Modifier.isStatic(enhanced.access)) {
                continue;
            }

            boolean isEnhanced = true;

            isEnhanced = isEnhanced && TransformerHelper.containsInvokeVirtual(enhanced, "java/lang/String", "intern", "()Ljava/lang/String;");
            isEnhanced = isEnhanced && TransformerHelper.containsInvokeVirtual(enhanced, "java/lang/String", "toCharArray", "()[C");
            isEnhanced = isEnhanced && TransformerHelper.countOccurencesOf(enhanced, AALOAD) > 0;
            isEnhanced = isEnhanced && TransformerHelper.countOccurencesOf(enhanced, AASTORE) > 0;
            isEnhanced = isEnhanced && TransformerHelper.countOccurencesOf(enhanced, TABLESWITCH) > 0;
            isEnhanced = isEnhanced && TransformerHelper.countOccurencesOf(enhanced, IXOR) > 0;
            isEnhanced = isEnhanced && TransformerHelper.countOccurencesOf(enhanced, IREM) > 0;

            if (isEnhanced) {
                return "Found potential enhanced string encrypted class " + classNode.name;
            }
        }

        return null;
    }

    @Override
    public Collection<Class<? extends Transformer<?>>> getRecommendTransformers() {
        return Arrays.asList(StringEncryptionTransformer.class, EnhancedStringEncryptionTransformer.class);
    }
}
