package org.dows.aac.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dows.aac.constant.AuthStatusCode;
import org.dows.rade.exception.RadeException;
import org.dows.rade.status.CommonStatusCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AacException extends RadeException {


    public AacException(AuthStatusCode authStatusCode) {
        super(authStatusCode);
    }

    public AacException(String msg) {
        super(Integer.valueOf(CommonStatusCode.FAILED.getCode()), msg);
    }


}