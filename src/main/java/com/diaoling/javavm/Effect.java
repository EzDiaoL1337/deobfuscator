package com.diaoling.javavm;

/**
 * The effect that the particular method call will have on the rest of the program.
 * <p>
 * For example, nearly any method call (such as System.setProperty) will be ALL, because it will affect the outside program
 * However, a call like Integer.intValue() will be NONE
 */
public enum Effect {
    /**
     *
     */
    NONE,
    UNKNOWN,
    ALL;
}
