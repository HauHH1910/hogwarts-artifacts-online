package com.hauhh.exception;

import com.hauhh.common.Result;
import com.hauhh.common.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
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

    @ExceptionHandler(ObjectNotFoundException.class)
    Result handleObjectNotFoundException(ObjectNotFoundException e) {
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
        return Result.builder()
                .flag(false)
                .code(StatusCode.INVALID_ARGUMENT)
                .message("Provided arguments are invalid, see data for details.")
                .data(map)
                .build();
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAuthenticationException(Exception e) {
        return Result.builder()
                .flag(false)
                .code(StatusCode.UNAUTHORIZED)
                .message("Username or password is incorrect")
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAccountStatusException(AccountStatusException e) {
        return Result.builder()
                .flag(false)
                .code(StatusCode.UNAUTHORIZED)
                .message("User account is abnormal")
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleInvalidBearerTokenException(InvalidBearerTokenException e) {
        return Result.builder()
                .flag(false)
                .code(StatusCode.UNAUTHORIZED)
                .message("The access token provided is expired, revoked, malformed token")
                .data(e.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleAccessDeniedException(AccessDeniedException e) {
        return Result.builder()
                .flag(false)
                .code(StatusCode.FORBIDDEN)
                .message("No permissions.")
                .data(e.getMessage())
                .build();
    }

    /**
    * Fallback handles any unhandled exception
    * */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleException(Exception e) {
        return Result.builder()
                .flag(false)
                .code(StatusCode.INTERNAL_SERVER_ERROR)
                .message("A server error occurred.")
                .data(e.getMessage())
                .build();
    }

}
