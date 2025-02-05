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

public class VMSymbols {
    public static final int MAX_SYMBOL_LENGTH = (1 << 16) - 1;
    public static final String java_lang_IncompatibleClassChangeError = "java/lang/IncompatibleClassChangeError";
    public static final String java_lang_NoSuchMethodError = "java/lang/NoSuchMethodError";
    public static final String java_lang_NoSuchFieldError = "java/lang/NoSuchFieldError";
    public static final String java_lang_InternalError = "java/lang/InternalError";
    public static final String java_lang_NullPointerException = "java/lang/NullPointerException";
    public static final String java_lang_ClassCastException = "java/lang/ClassCastException";
    public static final String INVOKE_NAME = "invoke";
    public static final String INVOKE_BASIC_NAME = "invokeBasic";
    public static final String LINK_TO_VIRTUAL_NAME = "linkToVirtual";
    public static final String LINK_TO_STATIC_NAME = "linkToStatic";
    public static final String LINK_TO_SPECIAL_NAME = "linkToSpecial";
    public static final String LINK_TO_INTERFACE_NAME = "linkToInterface";
    public static final String java_lang_ArithmeticException = "java/lang/ArithmeticException";
    public static final String java_lang_ArrayIndexOutOfBoundsException = "java/lang/ArrayIndexOutOfBoundsException";
    public static final String java_lang_ArrayStoreException = "java/lang/ArrayStoreException";
    public static final String java_lang_ClassNotFoundException = "java/lang/ClassNotFoundException";
    public static final String java_lang_NoClassDefFoundError = "java/lang/NoClassDefFoundError";
    public static final String java_lang_IllegalArgumentException = "java/lang/IllegalArgumentException";
    public static final String java_lang_OutOfMemoryError = "java/lang/OutOfMemoryError";
    public static final String java_util_concurrent_ConcurrentHashMap = "java/util/concurrent/ConcurrentHashMap";
    public static final String java_util_zip_DataFormatException = "java/util/zip/DataFormatException";
    public static final String java_util_concurrent_ConcurrentHashMap_sig = "L" + java_util_concurrent_ConcurrentHashMap + ";";
    public static final String java_lang_CloneNotSupportedException = "java/lang/CloneNotSupportedException";
    public static final String java_io_FileNotFoundException = "java/io/FileNotFoundException";
    public static final String java_lang_InterruptedExecption = "java/lang/InterruptedException";
    public static final String java_net_UnknownHostException = "java/net/UnknownHostException";
    public static final String java_net_SocketException = "java/net/SocketException";
    public static final String java_io_IOException = "java/io/IOException";
    public static final String java_lang_Class = "java/lang/Class";
    public static final String java_lang_Class_forName0_name = "forName0";
    public static final String java_lang_Class_forName0_sig = "(Ljava/lang/String;ZLjava/lang/ClassLoader;Ljava/lang/Class;)Ljava/lang/Class;";
    public static final String java_lang_ClassLoader = "java/lang/ClassLoader";
    public static final String java_lang_ClassLoader_parallelLockMap_name = "parallelLockMap";
    public static final String java_lang_ClassLoader_parallelLockMap_sig = java_util_concurrent_ConcurrentHashMap_sig;
    @Deprecated
    public static final String METADATA_KLASS = "metadata/klass";
    @Deprecated
    public static final String METADATA_FIELD = "metadata/field";
    @Deprecated
    public static final String METADATA_LOADER_DATA = "metadata/loaderdata";
    public static final String java_lang_Throwable = "java/lang/Throwable";

    public enum VMIntrinsics {
        NONE,
        INVOKE,
        INVOKE_GENERIC,
        INVOKE_BASIC,
        LINK_TO_VIRTUAL,
        LINK_TO_STATIC,
        LINK_TO_SPECIAL,
        LINK_TO_INTERFACE;

        public static final VMIntrinsics FIRST_MH_SIG_POLY = INVOKE_GENERIC;
        public static final VMIntrinsics FIRST_MH_STATIC = LINK_TO_VIRTUAL;
        public static final VMIntrinsics LAST_MH_SIG_POLY = LINK_TO_INTERFACE;
    }
}
