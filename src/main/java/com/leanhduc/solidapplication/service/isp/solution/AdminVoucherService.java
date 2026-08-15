package com.leanhduc.solidapplication.service.isp.solution;

import com.leanhduc.solidapplication.dto.CreateVoucherRequest;
import com.leanhduc.solidapplication.exception.ResourceNotFoundException;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.repository.VoucherRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminVoucherService implements VoucherManagementService {

    VoucherRepository voucherRepository;

    @Override
    public Voucher createVoucher(CreateVoucherRequest request) {
        Voucher voucher =
                Voucher.builder()
                        .code(request.getCode())
                        .type(request.getType())
                        .discountPercent(request.getDiscountPercent())
                        .discountAmount(request.getDiscountAmount())
                        .used(false)
                        .build();

        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher resetVoucher(String code) {
        Voucher voucher =
                voucherRepository
                        .findByCode(code)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Không tìm thấy voucher: " + code));

        voucher.setUsed(false);
        return voucherRepository.save(voucher);
    }
}
