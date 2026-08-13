package com.leanhduc.solidapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
