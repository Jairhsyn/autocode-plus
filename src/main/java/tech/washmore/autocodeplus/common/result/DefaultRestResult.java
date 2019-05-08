package tech.washmore.autocodeplus.common.result;

public class DefaultRestResult implements RestResult {

    private final int code;
    private final String message;

    public DefaultRestResult(String message) {
        super();
        this.message = message;
        this.code = Codes.UNKNOWN;
    }

    public DefaultRestResult(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
