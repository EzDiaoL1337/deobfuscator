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

package com.diaoling.javavm.ext.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JSocket {
    private final int _fd;

    private volatile InputStream _fromExternal;
    private volatile OutputStream _toExternal;

    private final ReadWriteLock _streamRedirectorLock = new ReentrantReadWriteLock();

    JSocket(int fd) {
        _fd = fd;
    }

    public int getFd() {
        return _fd;
    }

    /**
     * Set the source that this socket will read from. Can only be called once
     */
    public void setInput(InputStream inputStream) {
        _streamRedirectorLock.writeLock().lock();
        try {
            if (_fromExternal != null) {
                throw new IllegalArgumentException("input is already set");
            }
            _fromExternal = inputStream;
        } finally {
            _streamRedirectorLock.writeLock().unlock();
        }
    }

    /**
     * Set the destination that this socket will write to
     */
    public void setOutput(OutputStream outputStream) {
        _streamRedirectorLock.writeLock().lock();
        try {
            if (_toExternal != null) {
                throw new IllegalArgumentException("output is already set");
            }
            _toExternal = outputStream;
        } finally {
            _streamRedirectorLock.writeLock().unlock();
        }
    }

    public InputStream getInputStream() {
        _streamRedirectorLock.readLock().lock();
        try {
            return _fromExternal;
        } finally {
            _streamRedirectorLock.readLock().unlock();
        }
    }

    public OutputStream getOutputStream() {
        _streamRedirectorLock.readLock().lock();
        try {
            return _toExternal;
        } finally {
            _streamRedirectorLock.readLock().unlock();
        }
    }

    public boolean isConnected() {
        _streamRedirectorLock.readLock().lock();
        try {
            return _fromExternal != null && _toExternal != null;
        } finally {
            _streamRedirectorLock.readLock().unlock();
        }
    }
}
