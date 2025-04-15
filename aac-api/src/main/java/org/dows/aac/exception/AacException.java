package org.dows.aac.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dows.aac.constant.AuthStatusCode;
import org.dows.rade.exception.RadeException;

@EqualsAndHashCode(callSuper = true)
@Data
public class AacException extends RadeException {

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