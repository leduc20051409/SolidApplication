package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.model.Order;

// CHUẨN LSP: Interface định nghĩa thuật toán giảm giá (Strategy)
public interface VoucherStrategy {
    boolean canApply(Order order);
    double applyDiscount(Order order);
}
