package com.leanhduc.solidapplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyVoucherRequest {
    @NotEmpty(message = "Danh sách mã voucher không được để trống") private List<@NotBlank(message = "Mã voucher không được để trống") String> voucherCodes;
}
