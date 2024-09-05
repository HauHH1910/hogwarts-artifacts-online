package com.hauhh.hogwartsartifactsonline.system.exception;

import com.hauhh.hogwartsartifactsonline.system.Result;
import com.hauhh.hogwartsartifactsonline.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(ArtifactNotFoundException.class)
    Result handleArtifactNotFoundException(Exception e) {
        return Result.builder()
                .flag(false)
                .code(StatusCode.NOT_FOUND)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(WizardNotFoundException.class)
    Result handleWizardNotFoundException(Exception e) {
        return Result.builder()
                .flag(false)
                .code(StatusCode.NOT_FOUND)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return new Result(false, StatusCode.INVALID_ARGUMENT, "Provided arguments are invalid, see data for details.", map);
    }

}
