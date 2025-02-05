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

package com.diaoling.javavm.mirrors;

import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.internals.VMSymbols;

import static com.diaoling.javavm.internals.VMSymbols.*;

public class JavaMethodHandle {
    public static VMSymbols.VMIntrinsics signaturePolymorphicNameId(String name) {
        switch (name) {
            case VMSymbols.INVOKE_NAME:
                return VMSymbols.VMIntrinsics.INVOKE_GENERIC;
            case VMSymbols.INVOKE_BASIC_NAME:
                return VMSymbols.VMIntrinsics.INVOKE_BASIC;
            case VMSymbols.LINK_TO_VIRTUAL_NAME:
                return VMSymbols.VMIntrinsics.LINK_TO_VIRTUAL;
            case LINK_TO_STATIC_NAME:
                return VMSymbols.VMIntrinsics.LINK_TO_STATIC;
            case LINK_TO_SPECIAL_NAME:
                return VMSymbols.VMIntrinsics.LINK_TO_SPECIAL;
            case LINK_TO_INTERFACE_NAME:
                return VMSymbols.VMIntrinsics.LINK_TO_INTERFACE;
        }

        // todo this is not official
        if (name.startsWith("invoke")) {
            return VMSymbols.VMIntrinsics.INVOKE_GENERIC;
        }

        return VMSymbols.VMIntrinsics.NONE;
    }

    public static String signaturePolymorphicIntrinsicName(VMSymbols.VMIntrinsics id) {
        switch (id) {
            case INVOKE_BASIC:
                return VMSymbols.INVOKE_BASIC_NAME;
            case LINK_TO_VIRTUAL:
                return VMSymbols.LINK_TO_VIRTUAL_NAME;
            case LINK_TO_STATIC:
                return LINK_TO_STATIC_NAME;
            case LINK_TO_SPECIAL:
                return VMSymbols.LINK_TO_SPECIAL_NAME;
            case LINK_TO_INTERFACE:
                return VMSymbols.LINK_TO_INTERFACE_NAME;
        }
        throw new ExecutionException("Unsupported vm intrinsic " + id);
    }

    public static boolean isSignaturePolymorphicIntrinsic(VMSymbols.VMIntrinsics id) {
        return id != VMSymbols.VMIntrinsics.INVOKE_GENERIC;
    }

    public static boolean isSignaturePolymorphicStatic(VMSymbols.VMIntrinsics id) {
        return id != null && id.ordinal() >= VMSymbols.VMIntrinsics.FIRST_MH_STATIC.ordinal() && id.ordinal() <= VMSymbols.VMIntrinsics.LAST_MH_SIG_POLY.ordinal();
    }

    public static boolean isIntrinsicStatic(VMSymbols.VMIntrinsics id) {
        return id == VMSymbols.VMIntrinsics.LINK_TO_VIRTUAL
                || id == VMSymbols.VMIntrinsics.LINK_TO_INTERFACE
                || id == VMSymbols.VMIntrinsics.LINK_TO_SPECIAL
                || id == VMSymbols.VMIntrinsics.LINK_TO_STATIC;
    }
}