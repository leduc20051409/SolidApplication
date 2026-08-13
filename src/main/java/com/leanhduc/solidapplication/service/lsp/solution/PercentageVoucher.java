package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.model.Order;

public class PercentageVoucher implements VoucherStrategy {
    private final String code;
    private final double discountPercent;

    public PercentageVoucher(String code, double discountPercent) {
        this.code = code;
        this.discountPercent = discountPercent;
    }

    @Override
    public boolean canApply(Order order) {
        return true;
    }

    @Override
    public double applyDiscount(Order order) {
        double discount = order.getTotalAmount() * (discountPercent / 100);
        return order.getTotalAmount() - discount;
    }
}
