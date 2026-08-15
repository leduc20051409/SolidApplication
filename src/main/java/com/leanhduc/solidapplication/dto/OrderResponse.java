package com.leanhduc.solidapplication.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String customerName;
    private double originalAmount;
    private double finalAmount;
    private List<String> appliedVouchers;
    private List<String> rejectedVouchers;
}
