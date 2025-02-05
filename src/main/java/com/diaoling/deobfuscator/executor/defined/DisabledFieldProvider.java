/*
 * Copyright 2016 Sam Sun <me@samczsun.com>
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.diaoling.deobfuscator.executor.defined;

import com.diaoling.deobfuscator.executor.Context;
import com.diaoling.deobfuscator.executor.providers.FieldProvider;
import com.diaoling.deobfuscator.executor.values.JavaValue;

public class DisabledFieldProvider extends FieldProvider {
    public void setField(String className, String fieldName, String fieldDesc, JavaValue targetObject, Object value, Context context) {
        throw new IllegalArgumentException("Field get: " + className + " " + fieldName + fieldDesc);
    }

    public Object getField(String className, String fieldName, String fieldDesc, JavaValue targetObject, Context context) {
        throw new IllegalArgumentException("Field set: " + className + " " + fieldName + fieldDesc);
    }

    public boolean canGetField(String className, String fieldName, String fieldDesc, JavaValue targetObject, Context context) {
        return true;
    }

    public boolean canSetField(String className, String fieldName, String fieldDesc, JavaValue targetObject, Object value, Context context) {
        return true;
    }
}
