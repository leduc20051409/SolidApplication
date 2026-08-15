package com.leanhduc.solidapplication.service.lsp.violation;

import com.leanhduc.solidapplication.model.Order;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

// Class cha: kỳ vọng applyDiscount luôn hoạt động bình thường
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class VoucherViolation {
    protected String code;

    public double applyDiscount(Order order) {
        return order.getTotalAmount();
    }
}
