package tech.washmore.autocodeplus.common.result.support;

import java.util.Objects;

public final class BizCode {
    private final int code;
    private String message;

    public BizCode(int code, String message) {
        this.code = code;
        this.message = Objects.toString(message, "");
    }

    public BizCode withMessage(String message) {
        return new BizCode(this.code, message);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
