package com.leanhduc.solidapplication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    @Builder.Default final boolean success = true;

    final int code;
    final String message;
    final T data;
    final ApiError error;

    @Builder.Default final Instant timestamp = Instant.now();

    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return ApiResponse.<T>builder().code(code).message(message).data(data).build();
    }

    public static ApiResponse<Void> failure(
            int code, String message, String errorCode, Object details) {
        return ApiResponse.<Void>builder()
                .success(false)
                .code(code)
                .message(message)
                .error(ApiError.builder().code(errorCode).details(details).build())
                .build();
    }

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class ApiError {
        String code;
        Object details;
    }
}
