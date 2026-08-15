package com.leanhduc.solidapplication.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.leanhduc.solidapplication.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    @Test
    void exposesUnsupportedOperationMessageForTheIspViolationDemo() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<ApiResponse<Void>> response =
                handler.handleUnsupportedOperation(
                        new UnsupportedOperationException("Khách hàng không được tạo voucher"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Khách hàng không được tạo voucher", response.getBody().getMessage());
        assertEquals("UNSUPPORTED_OPERATION", response.getBody().getError().getCode());
    }
}
