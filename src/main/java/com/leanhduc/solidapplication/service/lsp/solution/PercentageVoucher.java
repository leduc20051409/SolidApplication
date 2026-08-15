package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.model.Order;

public class PercentageVoucher implements VoucherStrategy {
    private final String code;
    private final double discountPercent;

    public PercentageVoucher(String code, double discountPercent) {
        if (!Double.isFinite(discountPercent) || discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Phần trăm giảm giá phải nằm trong khoảng 0 đến 100");
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
