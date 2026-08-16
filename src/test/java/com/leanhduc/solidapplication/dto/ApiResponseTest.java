package com.leanhduc.solidapplication.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leanhduc.solidapplication.enums.ErrorCode;
import com.leanhduc.solidapplication.enums.ResponseCode;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    void shouldCreateSuccessResponse() {
        ApiResponse<String> response = ApiResponse.success(ResponseCode.VOUCHER_CREATED, "data");

        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("Tạo voucher thành công", response.getMessage());
        assertEquals("data", response.getData());
        assertNull(response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void shouldCreateFailureResponse() {
        ApiResponse<Void> response = ApiResponse.failure(ErrorCode.VALIDATION_ERROR, "orderId");

        assertFalse(response.isSuccess());
        assertEquals(400, response.getCode());
        assertEquals("Dữ liệu không hợp lệ", response.getMessage());
        assertNull(response.getData());
        assertEquals("VALIDATION_ERROR", response.getError().getCode());
        assertEquals("orderId", response.getError().getDetails());
        assertNotNull(response.getTimestamp());
    }
}
