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
        if (!Double.isFinite(discountAmount) || discountAmount < 0) {
            throw new IllegalArgumentException("Số tiền giảm giá phải hữu hạn và không âm");
        }
        this.code = code;
        this.discountAmount = discountAmount;
        this.used = used;
    }

    @Override
    public VoucherApplicationResult apply(Order order) {
        if (used) {
            return VoucherApplicationResult.rejected(order.getTotalAmount());
        }

        used = true;
        double finalAmount = Math.max(order.getTotalAmount() - discountAmount, 0);
        return VoucherApplicationResult.applied(finalAmount, true);
    }

    public boolean isUsed() {
        return used;
    }
}
