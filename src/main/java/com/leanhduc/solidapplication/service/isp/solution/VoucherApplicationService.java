package com.leanhduc.solidapplication.service.isp.solution;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;

public interface VoucherApplicationService {

    OrderResponse applyVouchers(Long orderId, ApplyVoucherRequest request);
}
