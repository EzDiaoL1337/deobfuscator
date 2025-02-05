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

package com.diaoling.javavm.internals;

import com.diaoling.javavm.values.JavaWrapper;

import java.util.Objects;

public class Handle {
    private JavaWrapper handle;

    public Handle(JavaWrapper handle) {
        this.handle = handle;
    }

    public JavaWrapper get() {
        return this.handle;
    }

    public boolean notNull() {
        return handle != null;
    }

    public boolean isNull() {
        return handle == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Handle handle1 = (Handle) o;
        return Objects.equals(handle, handle1.handle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handle);
    }
}
