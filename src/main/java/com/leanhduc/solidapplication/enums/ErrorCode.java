package com.leanhduc.solidapplication.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, "Đã xảy ra lỗi không mong muốn", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(400, "Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(400, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_VOUCHER_TYPE(400, "Loại voucher không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_DISCOUNT_PERCENT(
            400, "Phần trăm giảm phải nằm trong khoảng từ 0 đến 100", HttpStatus.BAD_REQUEST),
    INVALID_DISCOUNT_AMOUNT(400, "Số tiền giảm phải hữu hạn và không âm", HttpStatus.BAD_REQUEST),
    INVALID_VOUCHER_RESULT(
            500, "Kết quả áp dụng voucher không hợp lệ", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ORDER_AMOUNT(
            400, "Tổng tiền đơn hàng phải hữu hạn và không âm", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(404, "Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND),
    VOUCHER_NOT_FOUND(404, "Không tìm thấy voucher", HttpStatus.NOT_FOUND),
    VOUCHER_ALREADY_USED(409, "Voucher dùng một lần đã được sử dụng", HttpStatus.CONFLICT),
    CUSTOMER_CANNOT_CREATE_VOUCHER(403, "Khách hàng không được tạo voucher", HttpStatus.FORBIDDEN),
    CUSTOMER_CANNOT_RESET_VOUCHER(403, "Khách hàng không được reset voucher", HttpStatus.FORBIDDEN);

    int code;
    String message;
    HttpStatus statusCode;
}
