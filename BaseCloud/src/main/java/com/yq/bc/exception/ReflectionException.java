package com.yq.bc.exception;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description
 * @date 2020/1/19
 */
public class ReflectionException extends BaseException {

    private static final long serialVersionUID = 7096288929482651221L;

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
