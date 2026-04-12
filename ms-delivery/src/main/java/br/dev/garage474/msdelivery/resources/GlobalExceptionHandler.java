package br.dev.garage474.msdelivery.resources;

import br.dev.garage474.msdelivery.resources.dtos.ErrorResponse;
import br.dev.garage474.msdelivery.exceptions.CartAlreadyCheckedOutException;
import br.dev.garage474.msdelivery.exceptions.CartNotFoundException;
import br.dev.garage474.msdelivery.exceptions.CustomerAlreadyExistsException;
import br.dev.garage474.msdelivery.exceptions.EmptyCartCheckoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CartNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(CartNotFoundException ex) {
        log.error("cart not found: {}", ex.getMessage(), ex);
        return toErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({CartAlreadyCheckedOutException.class, EmptyCartCheckoutException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(RuntimeException ex) {
        log.error("cart conflict: {}", ex.getMessage(), ex);
        return toErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCustomerConflict(CustomerAlreadyExistsException ex) {
        log.error("customer conflict: {}", ex.getMessage(), ex);
        return toErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(Exception ex) {
        log.error("invalid request payload or UUID format: {}", ex.getMessage(), ex);
        return toErrorResponse(HttpStatus.BAD_REQUEST, "invalid UUID or malformed request payload");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("validation failed");

        log.error("validation error: {}", ex.getMessage(), ex);
        return toErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex) {
        log.error("unexpected error: {}", ex.getMessage(), ex);
        return toErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected internal error");
    }

    private ErrorResponse toErrorResponse(HttpStatus status, String message) {
        return new ErrorResponse(status.value(), status.getReasonPhrase(), message, LocalDateTime.now());
    }
}

