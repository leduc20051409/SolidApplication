package com.leanhduc.solidapplication.controller;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.model.Order;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.repository.OrderRepository;
import com.leanhduc.solidapplication.repository.VoucherRepository;
import com.leanhduc.solidapplication.service.VoucherService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lsp")
public class LspController {

    private final VoucherService solutionService;
    private final VoucherService violationService;
    private final OrderRepository orderRepository;
    private final VoucherRepository voucherRepository;

    public LspController(@Qualifier("lspSolutionVoucherService") VoucherService solutionService,
                         @Qualifier("lspViolationVoucherService") VoucherService violationService,
                         OrderRepository orderRepository,
                         VoucherRepository voucherRepository) {
        this.solutionService = solutionService;
        this.violationService = violationService;
        this.orderRepository = orderRepository;
        this.voucherRepository = voucherRepository;
    }

    // =========================================================
    // 1. Reset dữ liệu trong MySQL DB để test
    // =========================================================
    @PostMapping("/init-db")
    public Map<String, Object> initSampleData() {
        orderRepository.deleteAll();
        voucherRepository.deleteAll();

        Order order = orderRepository.save(new Order(null, "Nguyen Van A", 500_000));

        Voucher v1 = voucherRepository.save(new Voucher(null, "SALE10", "PERCENTAGE", 10, 0, false));
        Voucher v2 = voucherRepository.save(new Voucher(null, "ONETIME50K", "ONE_TIME", 0, 50_000, false));

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Khởi tạo dữ liệu MySQL thành công!");
        response.put("order", order);
        response.put("vouchers", List.of(v1, v2));
        return response;
    }

    // =========================================================
    // 2. API VI PHẠM LSP THỰC TẾ (Chạy trên MySQL qua VoucherService Interface)
    // Sẽ bị ném Exception 500 khi voucher ONETIME50K đã được dùng (used = true)
    // =========================================================
    @PostMapping("/violation/apply-vouchers/{orderId}")
    public OrderResponse applyVouchersViolation(@PathVariable Long orderId, @RequestBody ApplyVoucherRequest request) {
        return violationService.applyVouchers(orderId, request);
    }

    // =========================================================
    // 3. API CHUẨN LSP THỰC TẾ (Chạy trên MySQL qua VoucherService Interface)
    // Tự động bỏ qua voucher đã dùng, trả về HTTP 200 OK mượt mà
    // =========================================================
    @PostMapping("/solution/apply-vouchers/{orderId}")
    public OrderResponse applyVouchersSolution(@PathVariable Long orderId, @RequestBody ApplyVoucherRequest request) {
        return solutionService.applyVouchers(orderId, request);
    }
}
