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

package com.diaoling.deobfuscator.rules;

import com.diaoling.deobfuscator.Deobfuscator;
import com.diaoling.deobfuscator.transformers.Transformer;
import org.objectweb.asm.*;

import java.util.*;

public interface Rule extends Opcodes {
    String getDescription();

    String test(Deobfuscator deobfuscator);

    Collection<Class<? extends Transformer<?>>> getRecommendTransformers();
}
