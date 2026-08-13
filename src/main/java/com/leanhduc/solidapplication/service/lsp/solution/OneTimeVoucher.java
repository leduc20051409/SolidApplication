package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.model.Order;

public class OneTimeVoucher implements VoucherStrategy {
    private final String code;
    private final double discountAmount;
    private boolean used;

    public OneTimeVoucher(String code, double discountAmount) {
        this(code, discountAmount, false);
    }

    public OneTimeVoucher(String code, double discountAmount, boolean used) {
        this.code = code;
        this.discountAmount = discountAmount;
        this.used = used;
    }

    @Override
    public boolean canApply(Order order) {
        return !used; // Báo false thay vì ném Exception
    }

    @Override
    public double applyDiscount(Order order) {
        used = true;
        return Math.max(order.getTotalAmount() - discountAmount, 0);
    }

    public boolean isUsed() {
        return used;
    }
}
