package br.dev.garage474.mspedido.adapters.in.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Tratador global de exceções para a API REST.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Trata exceções genéricas de RuntimeException.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(
        RuntimeException e,
        WebRequest request
    ) {
        log.error("Erro de runtime: {}", e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .message(e.getMessage())
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Trata exceções genéricas.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
        Exception e,
        WebRequest request
    ) {
        log.error("Erro interno: {}", e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .message("Erro interno do servidor")
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(LocalDateTime.now())
            .path(request.getDescription(false).replace("uri=", ""))
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

