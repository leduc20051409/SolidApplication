package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.model.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PercentageVoucher implements VoucherStrategy {
    String code;
    double discountPercent;

    @Builder
    public PercentageVoucher(String code, double discountPercent) {
        if (!Double.isFinite(discountPercent) || discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Phần trăm giảm phải nằm trong khoảng từ 0 đến 100");
        }
        this.code = code;
        this.discountPercent = discountPercent;
    }

    @Override
    public VoucherApplicationResult apply(Order order) {
        double discount = order.getTotalAmount() * (discountPercent / 100);
        double finalAmount = order.getTotalAmount() - discount;
        return VoucherApplicationResult.applied(finalAmount, false);
    }
}
