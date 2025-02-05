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

package com.diaoling.javavm.values.prim;

import com.diaoling.javavm.values.JavaValue;

public abstract class JPrimitive extends JavaValue {

    public abstract boolean asBoolean();

    public abstract char asChar();

    public abstract byte asByte();

    public abstract short asShort();

    public abstract int asInt();

    public abstract float asFloat();

    public abstract long asLong();

    public abstract double asDouble();

    public abstract Object rawValue();
}
