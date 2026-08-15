package com.leanhduc.solidapplication.controller.isp;

import com.leanhduc.solidapplication.dto.ApiResponse;
import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.service.isp.solution.VoucherApplicationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/isp/solution")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerVoucherController {

    VoucherApplicationService applicationService;

    @PostMapping("/apply-vouchers/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> applyVouchers(
            @PathVariable Long orderId, @Valid @RequestBody ApplyVoucherRequest request) {
        OrderResponse data = applicationService.applyVouchers(orderId, request);
        return ResponseEntity.ok(
                ApiResponse.success(200, "Khách hàng áp dụng voucher thành công", data));
    }
}
