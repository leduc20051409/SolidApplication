package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.model.Order;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PercentageVoucher implements VoucherStrategy {
    String code;
    double discountPercent;

    @Override
    public VoucherApplicationResult apply(Order order) {
        double discount = order.getTotalAmount() * (discountPercent / 100);
        double finalAmount = order.getTotalAmount() - discount;
        return VoucherApplicationResult.applied(finalAmount, false);
    }
}
