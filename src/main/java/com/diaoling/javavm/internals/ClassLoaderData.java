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

import java.util.LinkedList;
import java.util.Objects;

public class ClassLoaderData {
    public static final ClassLoaderData NULL_LOADER_DATA = new ClassLoaderData(null, false, new Dependencies());
    private static final LinkedList<ClassLoaderData> classLoaderDatas = new LinkedList<>();
    private static final Object classLoaderDataLock = new Object();
    private final Handle loader;
    private final boolean isAnonymous;
    private final Dependencies dependencies;

    private ClassLoaderData(Handle loader, boolean isAnonymous, Dependencies dependencies) {
        this.loader = loader;
        this.isAnonymous = isAnonymous;
        this.dependencies = dependencies;
    }

    public static ClassLoaderData classLoaderDataOrNull(JavaWrapper loader) {
        return loader == null ? NULL_LOADER_DATA : loader.getVM().getJavaLangClassLoader().loaderData(loader);
    }

    public static ClassLoaderData findOrCreate(Handle loader) {
        ClassLoaderData cachedData = loader.get().getVM().getJavaLangClassLoader().loaderData(loader.get());
        if (cachedData != null) {
            return cachedData;
        }

        return add(loader, false);
    }

    private static ClassLoaderData add(Handle loader, boolean isAnonymous) {
        ClassLoaderData data = new ClassLoaderData(loader, isAnonymous, new Dependencies());

        if (!isAnonymous) {
            ClassLoaderData old = loader.get().compareAndSwapMetadata(VMSymbols.METADATA_LOADER_DATA, data, null);
            if (old != null) {
                return old;
            }
        }

        synchronized (classLoaderDataLock) {
            classLoaderDatas.addFirst(data);
        }

        return data;
    }

    public static ClassLoaderData classLoaderData(JavaWrapper oop) {
        return classLoaderDataOrNull(oop);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassLoaderData that = (ClassLoaderData) o;
        return Objects.equals(loader, that.loader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loader);
    }

    public static class Dependencies {

    }
}
