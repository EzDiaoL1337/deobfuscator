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

import com.diaoling.javavm.MethodExecution;
import com.diaoling.javavm.values.JavaWrapper;

public class HookedFieldSetter {
    private String _owner;
    private String _name;
    private String _desc;

    private HookedFieldSetter.Hook _handler;

    public HookedFieldSetter(String owner, String name, String desc) {
        this._owner = owner;
        this._name = name;
        this._desc = desc;
    }

    public HookedFieldSetter bind(HookedFieldSetter.Hook handler) {
        _handler = handler;
        return this;
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

    public void set(MethodExecution context, JavaWrapper instance, JavaWrapper value) {
        _handler.set(context, instance, value);
    }

    public interface Hook {
        void set(MethodExecution context, JavaWrapper instance, JavaWrapper value);
    }
}
