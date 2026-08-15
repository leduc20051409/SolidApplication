package com.leanhduc.solidapplication.service.lsp.violation;

import com.leanhduc.solidapplication.model.Order;
import lombok.Builder;

// VI PHẠM LSP: Ném Exception khi đã sử dụng, làm sập API thực tế khi data trong MySQL có used =
// true
public class OneTimeVoucherViolation extends VoucherViolation {
    private final double discountAmount;
    private boolean used;

    @Builder
    public OneTimeVoucherViolation(String code, double discountAmount, boolean used) {
        super(code);
        this.discountAmount = discountAmount;
        this.used = used;
    }

    @Override
    public double applyDiscount(Order order) {
        if (used) {
            // Vi phạm LSP: Ném Exception sập API
            throw new IllegalStateException("VI PHẠM LSP: Voucher [" + code + "] đã được sử dụng!");
        }
        // Ở đây có thể trả về double thay vì ném Exception
        used = true;
        return Math.max(order.getTotalAmount() - discountAmount, 0);
    }
}
