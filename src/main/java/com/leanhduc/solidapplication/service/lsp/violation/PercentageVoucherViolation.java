package com.leanhduc.solidapplication.service.lsp.violation;

import com.leanhduc.solidapplication.model.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PercentageVoucherViolation extends VoucherViolation {
    double discountPercent;

    @Builder
    public PercentageVoucherViolation(String code, double discountPercent) {
        super(code);
        this.discountPercent = discountPercent;
    }

    @Override
    public double applyDiscount(Order order) {
        double discount = order.getTotalAmount() * (discountPercent / 100);
        return order.getTotalAmount() - discount;
    }
}
