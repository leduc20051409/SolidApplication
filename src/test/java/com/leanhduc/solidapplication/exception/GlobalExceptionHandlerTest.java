package com.leanhduc.solidapplication.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.leanhduc.solidapplication.dto.ApiResponse;
import com.leanhduc.solidapplication.enums.ErrorCode;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

class GlobalExceptionHandlerTest {

    @Test
    void handlesEveryBusinessErrorThroughAppException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiResponse<Void>> response =
                handler.handleAppException(
                        new AppException(ErrorCode.CUSTOMER_CANNOT_CREATE_VOUCHER));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Khách hàng không được tạo voucher", response.getBody().getMessage());
        assertEquals("CUSTOMER_CANNOT_CREATE_VOUCHER", response.getBody().getError().getCode());
    }

    @Test
    void returnsValidationDetailsByField() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors())
                .thenReturn(
                        List.of(
                                new FieldError(
                                        "request",
                                        "voucherCodes",
                                        "Danh sách mã voucher không được để trống")));

        ResponseEntity<ApiResponse<Void>> response = handler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VALIDATION_ERROR", response.getBody().getError().getCode());
        assertEquals(
                "Danh sách mã voucher không được để trống",
                ((java.util.Map<?, ?>) response.getBody().getError().getDetails())
                        .get("voucherCodes"));
    }

    @Test
    void preservesClientErrorStatusForStandardSpringRequestErrors() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiResponse<Void>> response =
                handler.handleUnexpectedException(
                        new MissingServletRequestParameterException("customerName", "String"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INVALID_REQUEST", response.getBody().getError().getCode());
    }
}
