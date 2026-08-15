package com.leanhduc.solidapplication.controller.isp;

import com.leanhduc.solidapplication.dto.ApiResponse;
import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.CreateVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.service.isp.violation.VoucherOperationsViolation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/isp/violation")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IspViolationController {

    VoucherOperationsViolation violationService;

    @PostMapping("/apply-vouchers/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> applyVouchers(
            @PathVariable Long orderId, @Valid @RequestBody ApplyVoucherRequest request) {
        OrderResponse data = violationService.applyVouchers(orderId, request);
        return ResponseEntity.ok(
                ApiResponse.success(200, "Áp dụng voucher bằng interface vi phạm ISP", data));
    }

    @PostMapping("/create-voucher")
    public ResponseEntity<ApiResponse<Voucher>> createVoucher(
            @Valid @RequestBody CreateVoucherRequest request) {
        Voucher data = violationService.createVoucher(request);
        return ResponseEntity.ok(ApiResponse.success(200, "Tạo voucher thành công", data));
    }

    @PatchMapping("/reset-voucher/{code}")
    public ResponseEntity<ApiResponse<Voucher>> resetVoucher(@PathVariable String code) {
        Voucher data = violationService.resetVoucher(code);
        return ResponseEntity.ok(ApiResponse.success(200, "Reset voucher thành công", data));
    }
}
