package tech.washmore.autocodeplus.common.result;


import tech.washmore.autocodeplus.common.result.exceptions.*;

public abstract class JAssert {

    private JAssert() {
        super();
    }

    //404 begin
    public static void found(boolean expression, Object bizCode) {
        found(expression, bizCode, null);
    }

    public static void found(boolean expression, Object bizCode, OnAssertFailed onAssertFailed) {
        if (!expression) {
            if (onAssertFailed != null) {
                onAssertFailed.callback();
            }
            justNotFound(bizCode);
        }
    }

    public static void found(boolean expression, String message) {
        if (!expression) {
            justNotFound(message);
        }
    }

    public static void found(boolean expression, int errorCode, String message) {
        found(expression, errorCode, message, null);
    }

    public static void found(boolean expression, int errorCode, String message, OnAssertFailed onAssertFailed) {
        if (!expression) {
            if (onAssertFailed != null) {
                onAssertFailed.callback();
            }
            justNotFound(errorCode, message);
        }
    }

    public static void justNotFound(Object bizCode) {
        RestResult result = RestResultProvider.fromBizCode(bizCode);
        justNotFound(result.getCode(), result.getMessage());
    }

    public static void justNotFound(String message) {
        throw new NotFoundException(message);
    }

    public static void justNotFound(int errorCode, String message) {
        throw new NotFoundException(errorCode, message);
    }

    //400 begin
    public static void validParam(boolean expression, String message) {
        if (!expression) {
            justInvalidParam(message);
        }
    }

    public static void validParam(boolean expression, int errorCode, String message) {
        validParam(expression, errorCode, message, null);
    }

    public static void validParam(boolean expression, int errorCode,
                                  String message, OnAssertFailed onAssertFailed) {
        if (!expression) {
            if (onAssertFailed != null) {
                onAssertFailed.callback();
            }
            justInvalidParam(errorCode, message);
        }
    }


    public static void validParam(boolean expression, Object bizCode) {
        validParam(expression, bizCode, null);

    }

    public static void validParam(boolean expression, Object bizCode, OnAssertFailed onAssertFailed) {
        if (!expression) {
            if (onAssertFailed != null) {
                onAssertFailed.callback();
            }
            justInvalidParam(bizCode);
        }
    }

    public static void justInvalidParam(String message) {
        throw new InvalidParamException(message);
    }

    public static void justInvalidParam(int errorCode, String message) {
        throw new InvalidParamException(errorCode, message);
    }

    public static void justInvalidParam(Object bizCode) {
        RestResult result = RestResultProvider.fromBizCode(bizCode);
        justInvalidParam(result.getCode(), result.getMessage());
    }

    //403 begin
    public static void access(boolean expression, String message) {
        if (!expression) {
            justDenied(message);
        }
    }

    public static void access(boolean expression, int errorCode, String message) {
        access(expression, errorCode, message, null);
    }

    public static void access(boolean expression, int errorCode,
                              String message, OnAssertFailed onAssertFailed) {
        if (!expression) {
            if (onAssertFailed != null) {
                onAssertFailed.callback();
            }
            justDenied(errorCode, message);
        }
    }

    public static void access(boolean expression, Object bizcode) {
        access(expression, bizcode, null);
    }

    public static void access(boolean expression, Object bizcode, OnAssertFailed onAssertFailed) {
        if (!expression) {
            if (onAssertFailed != null) {
                onAssertFailed.callback();
            }
            justDenied(bizcode);
        }
    }

    public static void justDenied(String message) {
        throw new AccessDeniedException(message);
    }

    public static void justDenied(int errorCode, String message) {
        throw new AccessDeniedException(errorCode, message);
    }

    public static void justDenied(Object bizCode) {
        RestResult result = RestResultProvider.fromBizCode(bizCode);
        justDenied(result.getCode(), result.getMessage());
    }

    //401 begin
    public static void authorized(boolean expression, String message) {
        if (!expression) {
            justUnauthorized(message);
        }
    }

    public static void authorized(boolean expression, int errorCode, String message) {
        authorized(expression, errorCode, message, null);
    }

    public static void authorized(boolean expression, int errorCode, String message, OnAssertFailed onAssertFailed) {
        if (!expression) {
            if (onAssertFailed != null) {
                onAssertFailed.callback();
            }
            justUnauthorized(errorCode, message);
        }
    }

    public static void authorized(boolean expression, Object bizCode) {
        authorized(expression, bizCode, null);
    }

    public static void authorized(boolean expression, Object bizCode, OnAssertFailed onAssertFailed) {
        if (!expression) {
            if (onAssertFailed != null) {
                onAssertFailed.callback();
            }
            justUnauthorized(bizCode);
        }
    }

    public static void justUnauthorized(String message) {
        throw new UnauthorizedException(message);
    }

    public static void justUnauthorized(int errorCode, String message) {
        throw new UnauthorizedException(errorCode, message);
    }

    public static void justUnauthorized(Object bizCode) {
        RestResult result = RestResultProvider.fromBizCode(bizCode);
        justUnauthorized(result.getCode(), result.getMessage());
    }
}
