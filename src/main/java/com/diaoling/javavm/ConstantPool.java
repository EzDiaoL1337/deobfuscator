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

package com.diaoling.javavm;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.diaoling.javavm.internals.VMSymbols;
import org.objectweb.asm.ClassReader;

public class ConstantPool {
    private final VirtualMachine _vm;
    private final ClassReader classReader;

    // todo load constants lazily (to prevent abuse with large constant pools)
    public ConstantPool(VirtualMachine virtualMachine, ClassReader classReader) {
        _vm = virtualMachine;
        this.classReader = classReader;
    }

    public int getSize() {
        return this.classReader.getItemCount();
    }

    public String getUTF8At(int i) {
        if (i < 0 || i > this.classReader.getItemCount()) {
            throw _vm.newThrowable(VMSymbols.java_lang_IllegalArgumentException, "Constant pool index out of bounds");
        }
        if (i == 0) {
            throw _vm.newThrowable(VMSymbols.java_lang_IllegalArgumentException, "Wrong type at constant pool index");
        }
        int index = classReader.getItem(i);
        if (index == 0) {
            throw _vm.newThrowable(VMSymbols.java_lang_IllegalArgumentException, "Wrong type at constant pool index"); // long/double take 2 slots, this item might not exist
        }
        if (classReader.readByte(index - 1) != 1) {
            throw _vm.newThrowable(VMSymbols.java_lang_IllegalArgumentException, "Wrong type at constant pool index");
        }

        ByteArrayDataInput din = ByteStreams.newDataInput(classReader.b);
        din.skipBytes(index);
        String result = din.readUTF();
        return result;
    }
}
