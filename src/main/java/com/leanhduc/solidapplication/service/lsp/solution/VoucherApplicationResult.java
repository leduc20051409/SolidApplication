package com.leanhduc.solidapplication.service.lsp.solution;

import lombok.Builder;

@Builder
public record VoucherApplicationResult(
        boolean applied, double finalAmount, boolean shouldMarkUsed) {

    public VoucherApplicationResult {
        if (!Double.isFinite(finalAmount) || finalAmount < 0) {
            throw new IllegalArgumentException("Số tiền sau giảm giá phải hữu hạn và không âm");
        }
        if (!applied && shouldMarkUsed) {
            throw new IllegalArgumentException(
                    "Voucher bị từ chối không thể được đánh dấu đã dùng");
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
