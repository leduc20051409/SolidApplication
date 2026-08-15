package com.leanhduc.solidapplication.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateVoucherRequest {

    @NotBlank(message = "Mã voucher không được để trống") @Size(max = 50, message = "Mã voucher không được vượt quá 50 ký tự") @Pattern(
            regexp = "^[A-Z0-9_-]+$",
            message = "Mã voucher chỉ được chứa chữ in hoa, số, dấu gạch ngang và gạch dưới")
    String code;

    @NotBlank(message = "Loại voucher không được để trống") @Pattern(
            regexp = "^(PERCENTAGE|ONE_TIME)$",
            message = "Loại voucher phải là PERCENTAGE hoặc ONE_TIME")
    String type;

    @DecimalMin(value = "0.0", message = "Phần trăm giảm không được nhỏ hơn 0") @DecimalMax(value = "100.0", message = "Phần trăm giảm không được vượt quá 100") double discountPercent;

    @PositiveOrZero(message = "Số tiền giảm không được nhỏ hơn 0") double discountAmount;
}
