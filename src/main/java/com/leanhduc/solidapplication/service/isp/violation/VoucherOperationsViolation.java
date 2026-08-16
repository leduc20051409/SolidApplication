package com.leanhduc.solidapplication.service.isp.violation;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.CreateVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.model.Voucher;

public interface VoucherOperationsViolation {

    // Khách hàng cần
    OrderResponse applyVouchers(Long orderId, ApplyVoucherRequest request);

    // Chỉ Admin cần
    Voucher createVoucher(CreateVoucherRequest request);

    // Chỉ Admin cần
    Voucher resetVoucher(String code);
}
