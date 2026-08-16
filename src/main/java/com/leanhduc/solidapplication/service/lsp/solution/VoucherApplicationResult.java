package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.enums.ErrorCode;
import com.leanhduc.solidapplication.exception.AppException;
import lombok.Builder;

@Builder
public record VoucherApplicationResult(
        boolean applied, double finalAmount, boolean shouldMarkUsed) {

    public VoucherApplicationResult {
        if (!Double.isFinite(finalAmount) || finalAmount < 0) {
            throw new AppException(ErrorCode.INVALID_VOUCHER_RESULT);
        }
        if (!applied && shouldMarkUsed) {
            throw new AppException(ErrorCode.INVALID_VOUCHER_RESULT);
        }
    }

    public static VoucherApplicationResult applied(double finalAmount, boolean shouldMarkUsed) {
        return VoucherApplicationResult.builder()
                .applied(true)
                .finalAmount(finalAmount)
                .shouldMarkUsed(shouldMarkUsed)
                .build();
    }

    public static VoucherApplicationResult rejected(double currentAmount) {
        return VoucherApplicationResult.builder()
                .applied(false)
                .finalAmount(currentAmount)
                .shouldMarkUsed(false)
                .build();
    }
}
