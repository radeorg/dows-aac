package org.dows.aac.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dows.aac.constant.AuthStatusCode;
import org.dows.rade.exception.RadeException;
import org.dows.rade.status.CommonStatusCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AacException extends RadeException {

    private AuthStatusCode authStatusCode;


    public AacException(AuthStatusCode authStatusCode) {
        super(authStatusCode.getDescribe());
        this.authStatusCode = authStatusCode;
    }

    public AacException(String msg) {
        super(Integer.valueOf(CommonStatusCode.FAILED.getCode()), msg);
    }


}