package br.com.smnti.reddington.handler;

import br.com.smnti.reddington.common.log.LoggerSentry;
import br.com.smnti.reddington.common.util.AppUtil;
import br.com.smnti.reddington.exception.InvalidBodyException;
import br.com.smnti.reddington.exception.ZipCodeAlreadyExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler {

    @Autowired
    private LoggerSentry loggerSentry;

    @ResponseBody
    @ExceptionHandler({InvalidBodyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage exceptionHandler(InvalidBodyException ex){
        return this.createErrorMessage(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({ZipCodeAlreadyExists.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage exceptionHandler(ZipCodeAlreadyExists ex){

        this.loggerSentry.capture(ex);

        return this.createErrorMessage(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage exceptionHandler(Exception ex){
        return this.createErrorMessage(ex.getMessage());
    }

    private ErrorMessage createErrorMessage( String message ){
        return ErrorMessage.builder()
                .host(AppUtil.getServer())
                .message(message)
                .build();
    }

}
