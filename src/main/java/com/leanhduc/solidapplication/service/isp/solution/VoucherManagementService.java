package com.leanhduc.solidapplication.service.isp.solution;

import com.leanhduc.solidapplication.dto.CreateVoucherRequest;
import com.leanhduc.solidapplication.model.Voucher;

public interface VoucherManagementService {
    Voucher createVoucher(CreateVoucherRequest request);

    Voucher resetVoucher(String code);
}
