package com.leanhduc.solidapplication.service.lsp.violation;

import com.leanhduc.solidapplication.model.Order;

// Class cha: kỳ vọng applyDiscount luôn hoạt động bình thường
public class VoucherViolation {
    protected String code;

    public VoucherViolation(String code) {
        this.code = code;
    }

    public double applyDiscount(Order order) {
        return order.getTotalAmount();
    }
}
