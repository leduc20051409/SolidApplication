package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.model.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OneTimeVoucher implements VoucherStrategy {
    final String code;
    final double discountAmount;
    @Getter boolean used;

    @Override
    public VoucherApplicationResult apply(Order order) {
        if (used) {
            return VoucherApplicationResult.rejected(order.getTotalAmount());
        }

        used = true;
        double finalAmount = Math.max(order.getTotalAmount() - discountAmount, 0);
        return VoucherApplicationResult.applied(finalAmount, true);
    }
}
