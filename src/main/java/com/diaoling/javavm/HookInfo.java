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

import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class HookInfo {
    private ClassNode classNode;
    private MethodNode methodNode;
    private List<JavaWrapper> params;
    private JavaWrapper instance;
    private JavaWrapper returnValue;

    public HookInfo(ClassNode classNode, MethodNode methodNode) {
        this.classNode = classNode;
        this.methodNode = methodNode;
    }

    public HookInfo(ClassNode classNode, MethodNode methodNode, JavaWrapper instance, List<JavaWrapper> params) {
        this.classNode = classNode;
        this.methodNode = methodNode;
        this.instance = instance;
        this.params = params;
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    public MethodNode getMethodNode() {
        return methodNode;
    }

    public void setClassNode(ClassNode classNode) {
        this.classNode = classNode;
    }

    public void setMethodNode(MethodNode methodNode) {
        this.methodNode = methodNode;
    }

    public JavaWrapper getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(JavaWrapper returnValue) {
        this.returnValue = returnValue;
    }

    public boolean is(String owner, String name, String desc) {
        return classNode.name.equals(owner) && (methodNode != null) && methodNode.name.equals(name) && methodNode.desc.equals(desc);
    }

    public List<JavaWrapper> getParams() {
        return params;
    }

    public JavaWrapper getInstance() {
        return instance;
    }
}
