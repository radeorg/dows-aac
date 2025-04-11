package org.dows.aac.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dows.aac.constant.AuthStatusCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AacException extends RuntimeException {

    private AuthStatusCode authStatusCode;


    public AacException(AuthStatusCode authStatusCode) {
        super(authStatusCode.getDescribe());
        this.authStatusCode = authStatusCode;
    }

    public AacException(String msg) {
        super(msg);
    }

    public AacException(String msg, Throwable e) {
        super(msg, e);
    }


}