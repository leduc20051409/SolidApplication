package com.leanhduc.solidapplication.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    void shouldCreateSuccessResponse() {
        ApiResponse<String> response = ApiResponse.success(200, "Thành công", "data");

        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("Thành công", response.getMessage());
        assertEquals("data", response.getData());
        assertNull(response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void shouldCreateFailureResponse() {
        ApiResponse<Void> response =
                ApiResponse.failure(400, "Dữ liệu không hợp lệ", "INVALID_ARGUMENT", "orderId");

        assertFalse(response.isSuccess());
        assertEquals(400, response.getCode());
        assertEquals("Dữ liệu không hợp lệ", response.getMessage());
        assertNull(response.getData());
        assertEquals("INVALID_ARGUMENT", response.getError().getCode());
        assertEquals("orderId", response.getError().getDetails());
        assertNotNull(response.getTimestamp());
    }
}
