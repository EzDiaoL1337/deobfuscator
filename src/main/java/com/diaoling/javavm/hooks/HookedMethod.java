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

package com.diaoling.javavm.hooks;

import com.diaoling.javavm.Cause;
import com.diaoling.javavm.Effect;
import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.AbstractInsnNode;

public class HookedMethod {
    private String _owner;
    private String _name;
    private String _desc;
    private Cause _cause;
    private Effect _effect;

    private Hook _handler;

    public HookedMethod(String owner, String name, String desc) {
        this(owner, name, desc, Cause.ALL, Effect.ALL);
    }

    public HookedMethod(String owner, String name, String desc, Cause cause, Effect effect) {
        this._owner = owner;
        this._name = name;
        this._desc = desc;
        this._cause = cause;
        this._effect = effect;
    }

    public HookedMethod bind(Hook handler) {
        _handler = handler;
        return this;
    }

    public HookedMethod bind(VoidHook handler) {
        if (!this._desc.endsWith("V"))
            throw new IllegalArgumentException("Cannot bind non-returning handler with non-void function");
        return bind((a, b, c) ->
        {
            handler.execute(a, b, c);
            return null;
        });
    }

    public String getOwner() {
        return _owner;
    }

    public String getName() {
        return _name;
    }

    public String getDesc() {
        return _desc;
    }

    public Cause getCause() {
        return _cause;
    }

    public Effect getEffect() {
        return _effect;
    }

    public JavaWrapper execute(MethodExecution context, JavaWrapper instance, JavaWrapper[] args, AbstractInsnNode cur) {
        try {
            context.getVM().pushStacktrace(context.getClassNode(), context.getMethodNode(), cur);
            return _handler.execute(context, instance, args);
        } finally {
            context.getVM().popStacktrace();
        }
    }

    public interface Hook {
        JavaWrapper execute(MethodExecution context, JavaWrapper instance, JavaWrapper[] args);
    }

    public interface VoidHook {
        void execute(MethodExecution context, JavaWrapper instance, JavaWrapper[] args);
    }
}
