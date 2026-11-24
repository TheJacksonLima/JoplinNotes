package org.jfl.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.jfl.dto.ApiError;
import org.jfl.dto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ApiError> handlePaymentNotFound(PaymentNotFoundException ex, HttpServletRequest request){
        ApiError error = new ApiError(Instant.now(),404,"Not Found",ex.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        ApiError error = new ApiError(Instant.now(),400,"Not Found",ex.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleNotValidArgument(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError: ex.getBindingResult().getFieldErrors()) {

            errors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        ValidationErrorResponse response =
                new ValidationErrorResponse(
                        Instant.now(),
                        400,
                        "Validation Failed",
                        errors,
                        request.getRequestURI()
                );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiError> handleFileNotFound(FileNotFoundException ex, HttpServletRequest request) {
        ApiError error = new ApiError(Instant.now(),404,"File not Found",ex.getMessage(),request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
