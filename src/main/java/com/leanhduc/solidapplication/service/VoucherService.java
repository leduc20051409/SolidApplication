package com.leanhduc.solidapplication.service;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;

public interface VoucherService {
    OrderResponse applyVouchers(Long orderId, ApplyVoucherRequest request);
}
