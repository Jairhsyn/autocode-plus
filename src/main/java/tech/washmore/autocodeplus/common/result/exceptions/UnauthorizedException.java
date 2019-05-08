package tech.washmore.autocodeplus.common.result.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Washmore
 * @version V1.0
 * @summary TODO
 * @Copyright (c) 2019, Washmore All Rights Reserved.
 * @since 2019/4/14
 */
public class UnauthorizedException extends AbstractRestException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(int code, String message) {
        super(code, message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(int code, String message, Throwable cause) {
        super(code, message, HttpStatus.UNAUTHORIZED, cause);
    }
}
