package org.lro.steven.aq.simpleminecraftlauncher.exception;

public abstract class Exception extends java.lang.RuntimeException {
    private String message = null;

    public void setMessage(String msg) {
        message = msg;
    }

    @Override
    public String toString() {
        return message;
    }
}
