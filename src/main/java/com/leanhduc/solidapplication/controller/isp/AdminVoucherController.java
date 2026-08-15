package com.leanhduc.solidapplication.controller.isp;

import com.leanhduc.solidapplication.dto.ApiResponse;
import com.leanhduc.solidapplication.dto.CreateVoucherRequest;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.service.isp.solution.VoucherManagementService;
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
@RequestMapping("/isp/solution/admin/vouchers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminVoucherController {

    VoucherManagementService managementService;

    @PostMapping
    public ResponseEntity<ApiResponse<Voucher>> createVoucher(
            @Valid @RequestBody CreateVoucherRequest request) {
        Voucher data = managementService.createVoucher(request);
        return ResponseEntity.ok(ApiResponse.success(200, "Admin tạo voucher thành công", data));
    }

    @PatchMapping("/{code}/reset")
    public ResponseEntity<ApiResponse<Voucher>> resetVoucher(@PathVariable String code) {
        Voucher data = managementService.resetVoucher(code);
        return ResponseEntity.ok(ApiResponse.success(200, "Admin reset voucher thành công", data));
    }
}
