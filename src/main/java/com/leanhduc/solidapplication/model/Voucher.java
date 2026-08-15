package com.leanhduc.solidapplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vouchers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    /** Loại voucher: PERCENTAGE hoặc ONE_TIME */
    private String type;

    private double discountPercent;

    private double discountAmount;

    private boolean used;
}
