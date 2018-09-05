package br.com.smnti.reddington.resource.common;

import br.com.smnti.reddington.common.util.AppUtil;
import br.com.smnti.reddington.exception.InvalidBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.*;

@CrossOrigin
public abstract class BaseResource {

    protected void validate(Errors bindingResult){
        List<FieldError> errors = bindingResult.getFieldErrors();

        errors.forEach((error) -> {
            String defaultMessage = error.getDefaultMessage();
            String field = error.getField();

            if (defaultMessage != null && !defaultMessage.contains("java.lang.Short") &&
                    !defaultMessage.contains("java.lang.Integer") && !defaultMessage.contains("java.lang.Long")) {
                if (!defaultMessage.contains("java.lang.Double") && !defaultMessage.contains("java.lang.Float")) {
                    if (defaultMessage.contains("java.lang.Boolean")) {
                        throw new InvalidBodyException(field + " is not valid boolean field");
                    } else if (defaultMessage.contains("java.time.LocalDateTime")) {
                        throw new InvalidBodyException(field + " is not valid datetime field");
                    } else if (defaultMessage.contains("java.time.LocalDate")) {
                        throw new InvalidBodyException(field + " is not valid date field");
                    } else if (Objects.requireNonNull(error.getCodes())[0].contains("NotNull")) {
                        throw new InvalidBodyException(field + " is required field");
                    } else if (error.getCodes()[0].contains("Min")) {
                        throw new InvalidBodyException(field + " is invalid field");
                    } else if (error.getCodes()[0].contains("Max")) {
                        throw new InvalidBodyException(field + " is invalid field");
                    } else {
                        throw new InvalidBodyException("invalid body");
                    }
                } else {
                    throw new InvalidBodyException(field + " is invalid field");
                }
            } else {
                throw new InvalidBodyException(field + " is invalid field");
            }
        });
    }

    protected ResponseEntity<?> buildResponse(HttpStatus status, Optional<?> body){
        if(body.isPresent()){
            return new ResponseEntity<>(createResponse(body), status);
        }

        return new ResponseEntity<>(createResponse(body), HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("unchecked")
    private Response createResponse( Optional<?> entity ){
        Response response = new Response();
        List<Object> records = new ArrayList<>();

        if (entity.isPresent()) {
            if (entity.get() instanceof Collection) {
                records.addAll((Collection)entity.get());
            } else {
                records.add(entity.get());
            }
        }


        response.setRecords(records);
        response.setHost(AppUtil.getServer());

        return response;
    }
}
