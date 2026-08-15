package com.leanhduc.solidapplication.controller.dip;

import com.leanhduc.solidapplication.dto.ApiResponse;
import com.leanhduc.solidapplication.model.Order;
import com.leanhduc.solidapplication.service.dip.solution.OrderServiceDipSolution;
import com.leanhduc.solidapplication.service.dip.violation.OrderServiceDipViolation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dip")
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DipController {

    OrderServiceDipViolation violationService;

    OrderServiceDipSolution solutionService;

    // =========================================================
    // 1. VI PHẠM DIP
    // OrderService tự tạo MySqlOrderDatabase
    // =========================================================

    @PostMapping("/violation/orders")
    public ResponseEntity<ApiResponse<Order>> createOrderViolation(
            @NotBlank @RequestParam String customerName,
            @PositiveOrZero @RequestParam double totalAmount) {

        Order data = violationService.createOrder(customerName, totalAmount);

        return ResponseEntity.ok(
                ApiResponse.success(
                        200, "Mô phỏng OrderService phụ thuộc trực tiếp vào MySQL", data));
    }

    // =========================================================
    // 2. TUÂN THỦ DIP
    // OrderService chỉ phụ thuộc OrderStorage
    // =========================================================

    @PostMapping("/solution/orders")
    public ResponseEntity<ApiResponse<Order>> createOrderSolution(
            @NotBlank @RequestParam String customerName,
            @PositiveOrZero @RequestParam double totalAmount) {

        Order data = solutionService.createOrder(customerName, totalAmount);

        return ResponseEntity.ok(
                ApiResponse.success(200, "Mô phỏng OrderService lưu thông qua abstraction", data));
    }
}
