/*
 * Copyright 2017 Sam Sun <github-contact@samczsun.com>
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

package com.diaoling.deobfuscator.matcher;

import org.objectweb.asm.tree.AbstractInsnNode;

public class OrStep implements Step {
    private final Step[] steps;

    public OrStep(Step... steps) {
        this.steps = steps;
    }

    @Override
    public AbstractInsnNode tryMatch(InstructionMatcher matcher, AbstractInsnNode now) {
        for (Step step : steps) {
            AbstractInsnNode next = step.tryMatch(matcher, now);
            if (next != null) {
                return next;
            }
        }
        return null;
    }
}
