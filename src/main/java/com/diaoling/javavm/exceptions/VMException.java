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

package com.diaoling.javavm.exceptions;

import com.diaoling.javavm.values.JavaWrapper;

/**
 * This exception can be thrown when executing a method within the VM
 */
public class VMException extends RuntimeException {
    private JavaWrapper wrapped;

    public VMException(JavaWrapper inst) {
        super(new Throwable());
        this.wrapped = inst;
    }

    public JavaWrapper getWrapped() {
        return this.wrapped;
    }
}
