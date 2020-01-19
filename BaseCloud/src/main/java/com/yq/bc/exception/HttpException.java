package com.yq.bc.exception;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description
 * @date 2020/1/19
 */
public class HttpException extends BaseRuntimeException {

    private static final long serialVersionUID = 8021955126746426850L;
    private int statusCode;

    public HttpException(String message) {
        this(0, (String) message);
    }

    public HttpException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpException(Throwable cause) {
        this(0, (Throwable) cause);
    }

    public HttpException(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public HttpException(String message, Throwable cause) {
        this(0, message, cause);
    }

    public HttpException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        this(0, message, cause, enableSuppression, writableStackTrace);
    }

    public HttpException(int statusCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.statusCode = statusCode;
    }

    public int getStatus() {
        return this.statusCode;
    }
}
