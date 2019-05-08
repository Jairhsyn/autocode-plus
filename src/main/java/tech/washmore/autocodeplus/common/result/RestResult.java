package tech.washmore.autocodeplus.common.result;

public interface RestResult {
    int getCode();

    String getMessage();

    public interface Codes {
        int UNKNOWN = -1;
    }
}
