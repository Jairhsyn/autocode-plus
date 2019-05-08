package tech.washmore.autocodeplus.common.result.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.washmore.autocodeplus.common.result.DefaultRestResult;
import tech.washmore.autocodeplus.common.result.RestResult;

@ResponseBody
@ControllerAdvice
public final class ExceptionHandlerAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAware.class);

    @ExceptionHandler(value = AbstractRestException.class)
    public RestResult handleParamsErrorException(AbstractRestException e) {
        (((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse()).setStatus(e.getHttpStatus().value());
        return e.getResult();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult handleUncatchException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return new DefaultRestResult("系统异常:" + e.getMessage());
    }
}


