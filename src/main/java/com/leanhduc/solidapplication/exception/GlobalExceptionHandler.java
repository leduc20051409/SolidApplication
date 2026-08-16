package com.leanhduc.solidapplication.exception;

import com.leanhduc.solidapplication.dto.ApiResponse;
import com.leanhduc.solidapplication.enums.ErrorCode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception exception) {
        if (exception instanceof ErrorResponse errorResponse
                && errorResponse.getStatusCode().is4xxClientError()) {
            ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
            return ResponseEntity.status(errorResponse.getStatusCode())
                    .body(
                            ApiResponse.failure(
                                    errorCode, errorResponse.getStatusCode().value(), null));
        }

        log.error("Unexpected exception", exception);

        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.failure(errorCode, null));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.failure(errorCode, null));
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        HandlerMethodValidationException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleValidationException(Exception exception) {
        Map<String, String> details = collectValidationDetails(exception);

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.failure(errorCode, details));
    }

    private Map<String, String> collectValidationDetails(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException bodyValidationException) {
            return bodyValidationException.getBindingResult().getFieldErrors().stream()
                    .collect(
                            Collectors.toMap(
                                    FieldError::getField,
                                    this::resolveValidationMessage,
                                    (firstMessage, ignoredMessage) -> firstMessage,
                                    LinkedHashMap::new));
        }

        HandlerMethodValidationException methodValidationException =
                (HandlerMethodValidationException) exception;
        Map<String, String> details = new LinkedHashMap<>();
        methodValidationException
                .getParameterValidationResults()
                .forEach(
                        result -> {
                            String parameterName = result.getMethodParameter().getParameterName();
                            result.getResolvableErrors().stream()
                                    .findFirst()
                                    .ifPresent(
                                            error ->
                                                    details.put(
                                                            parameterName,
                                                            resolveValidationMessage(
                                                                    error.getDefaultMessage())));
                        });
        return details;
    }

    private String resolveValidationMessage(FieldError fieldError) {
        return resolveValidationMessage(fieldError.getDefaultMessage());
    }

    private String resolveValidationMessage(String errorCodeName) {
        if (errorCodeName == null) {
            return ErrorCode.VALIDATION_ERROR.getMessage();
        }

        try {
            return ErrorCode.valueOf(errorCodeName).getMessage();
        } catch (IllegalArgumentException exception) {
            return errorCodeName;
        }
    }
}
