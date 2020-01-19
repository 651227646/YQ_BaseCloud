package com.yq.bc.exception;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description
 * @date 2020/1/19
 */
public class ArgumentException extends BaseRuntimeException {

    private static final long serialVersionUID = -8479615591193529860L;
    private String errcode;

    public ArgumentException(String message) {
        super(message);
    }

    public ArgumentException(Throwable cause) {
        super(cause);
    }

    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentException(String errcode, String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
