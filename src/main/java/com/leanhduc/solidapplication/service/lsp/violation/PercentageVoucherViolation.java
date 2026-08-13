package com.leanhduc.solidapplication.service.lsp.violation;

import com.leanhduc.solidapplication.model.Order;

public class PercentageVoucherViolation extends VoucherViolation {
    private final double discountPercent;

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
