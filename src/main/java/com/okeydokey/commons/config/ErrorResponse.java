package com.okeydokey.commons.config;

import lombok.Data;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author okeydokey
 */
@Data
public class ErrorResponse {
    private String message;
    private String code;
    private List<FieldError> errors;

    //TODO
    public static class FieldError{
        private String field;
        private String value;
        private String reason;
    }
}
