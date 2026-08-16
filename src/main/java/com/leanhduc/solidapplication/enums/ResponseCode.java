package com.leanhduc.solidapplication.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ResponseCode {
    DATABASE_INITIALIZED(200, "Khởi tạo dữ liệu MySQL thành công"),
    LSP_VIOLATION_APPLIED(200, "Áp dụng voucher theo phiên bản vi phạm LSP thành công"),
    LSP_SOLUTION_APPLIED(200, "Áp dụng voucher theo phiên bản LSP thành công"),
    ISP_VIOLATION_APPLIED(200, "Áp dụng voucher bằng interface vi phạm ISP"),
    VOUCHER_CREATED(200, "Tạo voucher thành công"),
    VOUCHER_RESET(200, "Reset voucher thành công"),
    CUSTOMER_VOUCHER_APPLIED(200, "Khách hàng áp dụng voucher thành công"),
    ADMIN_VOUCHER_CREATED(200, "Admin tạo voucher thành công"),
    ADMIN_VOUCHER_RESET(200, "Admin reset voucher thành công"),
    DIP_VIOLATION_ORDER_CREATED(200, "Mô phỏng OrderService phụ thuộc trực tiếp vào MySQL"),
    DIP_SOLUTION_ORDER_CREATED(200, "Mô phỏng OrderService lưu thông qua abstraction");

    int code;
    String message;
}
