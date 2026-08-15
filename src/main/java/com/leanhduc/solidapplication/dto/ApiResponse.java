package com.leanhduc.solidapplication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.leanhduc.solidapplication.enums.ErrorCode;
import com.leanhduc.solidapplication.enums.ResponseCode;
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

    public static <T> ApiResponse<T> success(ResponseCode responseCode, T data) {
        return ApiResponse.<T>builder()
                .code(responseCode.getCode())
                .message(responseCode.getMessage())
                .data(data)
                .build();
    }

    public static ApiResponse<Void> failure(ErrorCode errorCode, Object details) {
        return failure(errorCode, errorCode.getCode(), details);
    }

    public static ApiResponse<Void> failure(ErrorCode errorCode, int responseCode, Object details) {
        return ApiResponse.<Void>builder()
                .success(false)
                .code(responseCode)
                .message(errorCode.getMessage())
                .error(ApiError.builder().code(errorCode.name()).details(details).build())
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
