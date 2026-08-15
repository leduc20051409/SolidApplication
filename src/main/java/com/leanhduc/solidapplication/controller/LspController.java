package com.leanhduc.solidapplication.controller;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.ApiResponse;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.model.Order;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.repository.OrderRepository;
import com.leanhduc.solidapplication.repository.VoucherRepository;
import com.leanhduc.solidapplication.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lsp")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LspController {

    static final int SUCCESS_CODE = 200;
    static final String INIT_SUCCESS_MESSAGE = "Khởi tạo dữ liệu MySQL thành công";
    static final String VIOLATION_SUCCESS_MESSAGE = "Áp dụng voucher theo phiên bản vi phạm LSP thành công";
    static final String SOLUTION_SUCCESS_MESSAGE = "Áp dụng voucher theo phiên bản LSP thành công";

    @Qualifier("lspSolutionVoucherService")
    VoucherService solutionService;

    @Qualifier("lspViolationVoucherService")
    VoucherService violationService;

    OrderRepository orderRepository;
    VoucherRepository voucherRepository;

    // =========================================================
    // 1. Reset dữ liệu trong MySQL DB để test
    // =========================================================
    @PostMapping("/init-db")
    public ApiResponse<Map<String, Object>> initSampleData() {
        orderRepository.deleteAll();
        voucherRepository.deleteAll();

        Order order = orderRepository.save(new Order(null, "Nguyen Van A", 500_000));

        Voucher v1 = voucherRepository.save(new Voucher(null, "SALE10", "PERCENTAGE", 10, 0, false));
        Voucher v2 = voucherRepository.save(new Voucher(null, "ONETIME50K", "ONE_TIME", 0, 50_000, false));

        Map<String, Object> data = Map.of(
                "order", order,
                "vouchers", List.of(v1, v2)
        );
        return ApiResponse.success(SUCCESS_CODE, INIT_SUCCESS_MESSAGE, data);
    }

    // =========================================================
    // 2. API VI PHẠM LSP THỰC TẾ (Chạy trên MySQL qua VoucherService Interface)
    // Sẽ trả về HTTP 409 khi voucher ONETIME50K đã được dùng (used = true)
    // =========================================================
    @PostMapping("/violation/apply-vouchers/{orderId}")
    public ApiResponse<OrderResponse> applyVouchersViolation(
            @PathVariable Long orderId, @RequestBody ApplyVoucherRequest request) {
        OrderResponse data = violationService.applyVouchers(orderId, request);
        return ApiResponse.success(SUCCESS_CODE, VIOLATION_SUCCESS_MESSAGE, data);
    }

    // =========================================================
    // 3. API CHUẨN LSP THỰC TẾ (Chạy trên MySQL qua VoucherService Interface)
    // Tự động bỏ qua voucher đã dùng, trả về HTTP 200 OK mượt mà
    // =========================================================
    @PostMapping("/solution/apply-vouchers/{orderId}")
    public ApiResponse<OrderResponse> applyVouchersSolution(
            @PathVariable Long orderId, @RequestBody ApplyVoucherRequest request) {
        OrderResponse data = solutionService.applyVouchers(orderId, request);
        return ApiResponse.success(SUCCESS_CODE, SOLUTION_SUCCESS_MESSAGE, data);
    }
}
