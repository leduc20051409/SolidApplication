package com.leanhduc.solidapplication.service.isp.violation;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.CreateVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerVoucherServiceViolation implements VoucherOperationsViolation {

    @Qualifier("lspSolutionVoucherService") VoucherService voucherService;

    @Override
    public OrderResponse applyVouchers(Long orderId, ApplyVoucherRequest request) {
        return voucherService.applyVouchers(orderId, request);
    }

    @Override
    public Voucher createVoucher(CreateVoucherRequest request) {
        throw new UnsupportedOperationException("Khách hàng không được tạo voucher");
    }

    @Override
    public Voucher resetVoucher(String code) {
        throw new UnsupportedOperationException("Khách hàng không được reset voucher");
    }
}
