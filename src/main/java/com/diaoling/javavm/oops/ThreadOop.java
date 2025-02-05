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

package com.diaoling.javavm.oops;

import com.diaoling.javavm.ExecutionOptions;
import com.diaoling.javavm.VirtualMachine;
import com.diaoling.javavm.exceptions.ExecutionException;
import com.diaoling.javavm.utils.ASMHelper;
import com.diaoling.javavm.values.JavaWrapper;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThreadOop extends Oop {
    private static final Map<Thread, ThreadOop> THREAD_TO_OOP_MAP = new HashMap<>();

    private final boolean isMainThread;
    private final Thread backingThread;
    private JavaWrapper thread;

    public ThreadOop(JavaWrapper thread) {
        this(thread, false);
    }

    private ThreadOop(JavaWrapper thread, boolean mainThread) {
        super(null); // todo oops
        this.thread = thread;
        this.isMainThread = mainThread;
        if (isMainThread) {
            backingThread = Thread.currentThread();
        } else {
            backingThread = new NativeThread();
        }
        THREAD_TO_OOP_MAP.put(backingThread, this);
    }

    public static ThreadOop forThread(Thread thread) {
        return THREAD_TO_OOP_MAP.get(thread);
    }

    public static ThreadOop forCurrentThread() {
        return forThread(Thread.currentThread()) == null ? new ThreadOop(null, true) : forThread(Thread.currentThread());
    }

    public JavaWrapper getThread() {
        return this.thread;
    }

    public void setThread(JavaWrapper wrapper) {
        if (this.thread != null) {
            throw new ExecutionException("Cannot set already non-null thread");
        }
        this.thread = wrapper;
    }

    public static void shutdown() {
        for (ThreadOop oop : THREAD_TO_OOP_MAP.values()) {
            if (oop.isMainThread) {
                oop.thread = null;
            } else {
                oop.backingThread.stop();
            }
        }
        THREAD_TO_OOP_MAP.clear();
    }

    public void start() {
        if (this.isMainThread) {
            throw new ExecutionException("Cannot re-start main thread");
        } else {
            try {
                Method m = Thread.class.getDeclaredMethod("start0");
                m.setAccessible(true);
                m.invoke(this.backingThread);
            } catch (ReflectiveOperationException e) {
                throw new ExecutionException(e);
            }
        }
    }

    public void setPriority(int priority) {
        try {
            Method m = Thread.class.getDeclaredMethod("setPriority0", int.class);
            m.setAccessible(true);
            m.invoke(this.backingThread, priority);
        } catch (ReflectiveOperationException e) {
            throw new ExecutionException(e);
        }
    }

    public boolean isAlive() {
        return this.backingThread.isAlive();
    }

    public void setInterrupted() {

    }

    public void yield() {
        if (Thread.currentThread() != backingThread) {
            throw new ExecutionException("Yield called on wrong thread: " + Thread.currentThread() + " vs " + backingThread);
        }
        Thread.yield();
    }

    private class NativeThread extends Thread {
        NativeThread() {
            super("JavaVM Native Thread: " + thread.getJavaClass().getName());
        }

        public void run() {
            VirtualMachine vm = thread.getJavaClass().getVM();
            try {
                vm.execute(thread.getJavaClass().getClassNode(), ASMHelper.findMethod(thread.getJavaClass().getClassNode(), "run", "()V"), thread, Collections.emptyList(), new ExecutionOptions());
            } catch (Throwable t) {
                if (!(t instanceof ThreadDeath)) {
                    t.printStackTrace(System.out);
                }
            } finally {
                /*
                    From Thread#join()

                     * <p> This implementation uses a loop of {@code this.wait} calls
                     * conditioned on {@code this.isAlive}. As a thread terminates the
                     * {@code this.notifyAll} method is invoked. It is recommended that
                     * applications not use {@code wait}, {@code notify}, or
                     * {@code notifyAll} on {@code Thread} instances.
                 */
                try {
                    thread.get().getLock().lock();
                    vm.execute(vm.getSystemDictionary().getJavaLangObject().getClassNode(), ASMHelper.findMethod(vm.getSystemDictionary().getJavaLangObject().getClassNode(), "notifyAll", "()V"), thread, Collections.emptyList(), new ExecutionOptions());
                } finally {
                    thread.get().getLock().unlock();
                }
            }
        }
    }
}
