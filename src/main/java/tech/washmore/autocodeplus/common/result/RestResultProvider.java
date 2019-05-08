package tech.washmore.autocodeplus.common.result;


import tech.washmore.autocodeplus.common.result.support.BizCode;

public final class RestResultProvider {
    public static RestResult fromBizCode(Object source) {
        BizCode bizCode = BizCode.class.cast(source);
        return new DefaultRestResult(bizCode.getCode(), bizCode.getMessage());
    }
}
