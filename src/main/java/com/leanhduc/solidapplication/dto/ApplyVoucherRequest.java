package com.leanhduc.solidapplication.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApplyVoucherRequest {
    private List<String> voucherCodes;
}
