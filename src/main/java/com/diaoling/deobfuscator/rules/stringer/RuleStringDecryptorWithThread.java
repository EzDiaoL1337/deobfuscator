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
import com.diaoling.deobfuscator.transformers.stringer.v3.StringEncryptionTransformer;
import com.diaoling.deobfuscator.utils.TransformerHelper;
import org.objectweb.asm.tree.*;

import java.util.*;

public class RuleStringDecryptorWithThread implements Rule {
    @Override
    public String getDescription() {
        return "Stringer's string encryption classes are very complex. One variety uses multithreading in order to make emulation difficult";
    }

    @Override
    public String test(Deobfuscator deobfuscator) {
        for (ClassNode classNode : deobfuscator.getClasses().values()) {
        	if (classNode.superName == null) {
                continue;
            }
            if (!classNode.superName.equals("java/lang/Thread")) {
                continue;
            }

            // Check for expected fields
            if (TransformerHelper.findFieldNode(classNode, null, "[Ljava/lang/Object;") == null) {
                continue;
            }
            if (TransformerHelper.findFieldNode(classNode, null, "I") == null) {
                continue;
            }
            if (TransformerHelper.findFieldNode(classNode, null, "[Ljava/math/BigInteger;") == null) {
                continue;
            }

            if (TransformerHelper.findMethodNode(classNode, "<init>", "(I)V") == null) {
                continue;
            }

            return "Found possible string decryption class " + classNode.name;
        }

        return null;
    }

    @Override
    public Collection<Class<? extends Transformer<?>>> getRecommendTransformers() {
        return Arrays.asList(com.diaoling.deobfuscator.transformers.stringer.StringEncryptionTransformer.class,
        	StringEncryptionTransformer.class);
    }
}
