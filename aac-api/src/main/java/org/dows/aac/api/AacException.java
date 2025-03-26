package org.dows.aac.api;


public class AacException extends RuntimeException {
    public AacException(String msg) {
        super(msg);
    }

    public AacException(String msg, Throwable e) {
        super(msg, e);
    }


}