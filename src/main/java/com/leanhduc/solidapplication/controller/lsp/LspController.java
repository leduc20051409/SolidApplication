package com.leanhduc.solidapplication.controller.lsp;

import com.leanhduc.solidapplication.dto.ApiResponse;
import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.enums.ResponseCode;
import com.leanhduc.solidapplication.model.Order;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.repository.OrderRepository;
import com.leanhduc.solidapplication.repository.VoucherRepository;
import com.leanhduc.solidapplication.service.VoucherService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lsp")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LspController {

    @Qualifier("lspSolutionVoucherService") VoucherService solutionService;

    @Qualifier("lspViolationVoucherService") VoucherService violationService;

    OrderRepository orderRepository;
    VoucherRepository voucherRepository;

    // =========================================================
    // 1. Reset dữ liệu trong MySQL DB để test
    // =========================================================
    @PostMapping("/init-db")
    public ResponseEntity<ApiResponse<Map<String, Object>>> initSampleData() {
        orderRepository.deleteAll();
        voucherRepository.deleteAll();

        Order order =
                orderRepository.save(
                        Order.builder().customerName("Nguyen Van A").totalAmount(500_000).build());

        Voucher v1 =
                voucherRepository.save(
                        Voucher.builder()
                                .code("SALE10")
                                .type("PERCENTAGE")
                                .discountPercent(10)
                                .discountAmount(0)
                                .used(false)
                                .build());
        Voucher v2 =
                voucherRepository.save(
                        Voucher.builder()
                                .code("ONETIME50K")
                                .type("ONE_TIME")
                                .discountPercent(0)
                                .discountAmount(50_000)
                                .used(false)
                                .build());

        Map<String, Object> data = Map.of("order", order, "vouchers", List.of(v1, v2));
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.DATABASE_INITIALIZED, data));
    }

    // =========================================================
    // 2. API VI PHẠM LSP THỰC TẾ (Chạy trên MySQL qua VoucherService Interface)
    // Sẽ trả về HTTP 409 khi voucher ONETIME50K đã được dùng (used = true)
    // =========================================================
    @PostMapping("/violation/apply-vouchers/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> applyVouchersViolation(
            @PathVariable Long orderId, @Valid @RequestBody ApplyVoucherRequest request) {
        OrderResponse data = violationService.applyVouchers(orderId, request);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.LSP_VIOLATION_APPLIED, data));
    }

    // =========================================================
    // 3. API CHUẨN LSP THỰC TẾ (Chạy trên MySQL qua VoucherService Interface)
    // Tự động bỏ qua voucher đã dùng, trả về HTTP 200 OK mượt mà
    // =========================================================
    @PostMapping("/solution/apply-vouchers/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> applyVouchersSolution(
            @PathVariable Long orderId, @Valid @RequestBody ApplyVoucherRequest request) {
        OrderResponse data = solutionService.applyVouchers(orderId, request);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.LSP_SOLUTION_APPLIED, data));
    }
}
