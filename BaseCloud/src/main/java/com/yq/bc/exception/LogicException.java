package com.yq.bc.exception;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description
 * @date 2020/1/19
 */
public class LogicException extends BaseRuntimeException {

    private static final long serialVersionUID = 8021955126746426850L;

    public LogicException(String message) {
        super(message);
    }

    public LogicException(Throwable cause) {
        super(cause);
    }

    public LogicException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
