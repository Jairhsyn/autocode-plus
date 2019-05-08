package tech.washmore.autocodeplus.common.result.exceptions;

import org.springframework.http.HttpStatus;
import tech.washmore.autocodeplus.common.result.DefaultRestResult;
import tech.washmore.autocodeplus.common.result.RestResult;

/**
 * @author Washmore
 * @version V1.0
 * @summary TODO
 * @Copyright (c) 2019, Washmore All Rights Reserved.
 * @since 2019/4/14
 */
public class AbstractRestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private HttpStatus httpStatus;
    private RestResult result;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public RestResult getResult() {
        return result;
    }

    public AbstractRestException(String message, HttpStatus httpStatus) {
        this(RestResult.Codes.UNKNOWN, message, httpStatus);
    }

    public AbstractRestException(int code, String message, HttpStatus httpStatus) {
        this(code, message, httpStatus, null);
    }

    public AbstractRestException(int code, String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.result = new DefaultRestResult(code, message);
        this.httpStatus = httpStatus;
    }
}
