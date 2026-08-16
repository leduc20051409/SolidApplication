package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.enums.ErrorCode;
import com.leanhduc.solidapplication.exception.AppException;
import com.leanhduc.solidapplication.model.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class OneTimeVoucher implements VoucherStrategy {
    final String code;
    final double discountAmount;
    @Getter boolean used;

    @Builder
    public OneTimeVoucher(String code, double discountAmount, boolean used) {
        if (!Double.isFinite(discountAmount) || discountAmount < 0) {
            throw new AppException(ErrorCode.INVALID_DISCOUNT_AMOUNT);
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
}
