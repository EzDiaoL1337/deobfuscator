package com.diaoling.javavm;

/**
 * How dependent this method call will be on the outside context
 * <p>
 * i.e. System.currentTimeMillis() is ALL because it depends on the current time
 * String.valueOf() is NONE because it depends only on the local context
 */
public enum Cause {
    NONE,
    UNKNOWN,
    ALL
}
