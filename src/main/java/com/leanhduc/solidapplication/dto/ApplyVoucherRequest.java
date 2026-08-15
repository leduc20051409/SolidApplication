package com.leanhduc.solidapplication.dto;

import java.util.List;
import lombok.Data;

@Data
public class ApplyVoucherRequest {
    private List<String> voucherCodes;
}
