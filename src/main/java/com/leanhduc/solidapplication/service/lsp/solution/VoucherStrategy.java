package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.model.Order;

// Mọi implementation đều thay thế được nhau: với Order hợp lệ, apply() luôn trả về
// kết quả hợp lệ và dùng rejected thay cho exception khi voucher không thể áp dụng.
public interface VoucherStrategy {
    VoucherApplicationResult apply(Order order);
}
