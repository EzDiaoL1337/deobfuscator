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

package com.diaoling.deobfuscator.executor.providers;

import java.util.List;

import com.diaoling.deobfuscator.executor.Context;
import com.diaoling.deobfuscator.executor.values.JavaValue;
import org.objectweb.asm.Type;

public interface Provider {

    Object invokeMethod(String className, String methodName, String methodDesc, JavaValue targetObject, List<JavaValue> args, Context context);

    boolean instanceOf(JavaValue target, Type type, Context context);

    boolean checkcast(JavaValue target, Type type, Context context);

    boolean checkEquality(JavaValue first, JavaValue second, Context context);

    void setField(String className, String fieldName, String fieldDesc, JavaValue targetObject, Object value, Context context);

    Object getField(String className, String fieldName, String fieldDesc, JavaValue targetObject, Context context);

    boolean canInvokeMethod(String className, String methodName, String methodDesc, JavaValue targetObject, List<JavaValue> args, Context context);

    boolean canCheckInstanceOf(JavaValue target, Type type, Context context);

    boolean canCheckcast(JavaValue target, Type type, Context context);
    
    boolean canCheckEquality(JavaValue first, JavaValue second, Context context);

    boolean canGetField(String className, String fieldName, String fieldDesc, JavaValue targetObject, Context context);

    boolean canSetField(String className, String fieldName, String fieldDesc, JavaValue targetObject, Object value, Context context);
}