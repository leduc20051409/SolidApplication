package com.leanhduc.solidapplication.exception;

import com.leanhduc.solidapplication.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(
            ResourceNotFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiResponse<Void> response =
                ApiResponse.failure(
                        status.value(), exception.getMessage(), "RESOURCE_NOT_FOUND", null);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(
            IllegalArgumentException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiResponse<Void> response =
                ApiResponse.failure(
                        status.value(), exception.getMessage(), "INVALID_ARGUMENT", null);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalState(IllegalStateException exception) {
        HttpStatus status = HttpStatus.CONFLICT;
        ApiResponse<Void> response =
                ApiResponse.failure(status.value(), exception.getMessage(), "INVALID_STATE", null);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception exception) {
        log.error("Unexpected error", exception);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResponse<Void> response =
                ApiResponse.failure(
                        status.value(),
                        "Đã xảy ra lỗi không mong muốn",
                        "INTERNAL_SERVER_ERROR",
                        null);
        return ResponseEntity.status(status).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {
        HttpStatus status = HttpStatus.resolve(statusCode.value());
        String message = status != null ? status.getReasonPhrase() : "Yêu cầu không hợp lệ";
        ApiResponse<Void> response =
                ApiResponse.failure(
                        statusCode.value(), message, "HTTP_" + statusCode.value(), null);
        return ResponseEntity.status(statusCode).headers(headers).body(response);
    }
}
