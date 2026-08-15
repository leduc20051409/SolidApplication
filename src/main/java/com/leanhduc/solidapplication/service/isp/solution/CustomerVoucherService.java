package com.leanhduc.solidapplication.service.isp.solution;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerVoucherService implements VoucherApplicationService {

    @Qualifier("lspSolutionVoucherService") VoucherService voucherService;

    @Override
    public OrderResponse applyVouchers(Long orderId, ApplyVoucherRequest request) {
        return voucherService.applyVouchers(orderId, request);
    }
}
