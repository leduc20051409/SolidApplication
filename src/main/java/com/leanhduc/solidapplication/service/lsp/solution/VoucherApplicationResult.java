package com.leanhduc.solidapplication.service.lsp.solution;

public record VoucherApplicationResult(
        boolean applied,
        double finalAmount,
        boolean shouldMarkUsed
) {

    public VoucherApplicationResult {
        if (!Double.isFinite(finalAmount) || finalAmount < 0) {
            throw new IllegalArgumentException("Số tiền sau giảm giá phải hữu hạn và không âm");
        }
        if (!applied && shouldMarkUsed) {
            throw new IllegalArgumentException("Voucher bị từ chối không thể được đánh dấu đã dùng");
        }
    }

    public static VoucherApplicationResult applied(double finalAmount, boolean shouldMarkUsed) {
        return new VoucherApplicationResult(true, finalAmount, shouldMarkUsed);
    }

    public static VoucherApplicationResult rejected(double currentAmount) {
        return new VoucherApplicationResult(false, currentAmount, false);
    }
}
