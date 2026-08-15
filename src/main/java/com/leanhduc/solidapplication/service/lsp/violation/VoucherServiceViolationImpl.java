package com.leanhduc.solidapplication.service.lsp.violation;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.enums.ErrorCode;
import com.leanhduc.solidapplication.exception.AppException;
import com.leanhduc.solidapplication.model.Order;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.repository.OrderRepository;
import com.leanhduc.solidapplication.repository.VoucherRepository;
import com.leanhduc.solidapplication.service.VoucherOrderer;
import com.leanhduc.solidapplication.service.VoucherService;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("lspViolationVoucherService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServiceViolationImpl implements VoucherService {

    OrderRepository orderRepository;
    VoucherRepository voucherRepository;

    @Override
    @Transactional
    public OrderResponse applyVouchers(Long orderId, ApplyVoucherRequest request) {
        Order order =
                orderRepository
                        .findByIdForUpdate(orderId)
                        .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        double originalAmount = order.getTotalAmount();
        double currentAmount = originalAmount;

        List<Voucher> fetchedVouchers = voucherRepository.findByCodeIn(request.getVoucherCodes());
        List<Voucher> vouchers =
                VoucherOrderer.followRequestOrder(request.getVoucherCodes(), fetchedVouchers);

        List<String> appliedVouchers = new ArrayList<>();
        List<Voucher> updatedVouchers = new ArrayList<>();

        for (Voucher voucher : vouchers) {
            // Biến kiểu cha chứa object của lớp con
            VoucherViolation strategy = createViolationStrategy(voucher);

            currentAmount =
                    strategy.applyDiscount(
                            Order.builder()
                                    .id(order.getId())
                                    .customerName(order.getCustomerName())
                                    .totalAmount(currentAmount)
                                    .build());

            if ("ONE_TIME".equalsIgnoreCase(voucher.getType())) {
                voucher.setUsed(true);
                updatedVouchers.add(voucher);
            }
            appliedVouchers.add(voucher.getCode());
        }

        order.setTotalAmount(currentAmount);

        orderRepository.save(order);
        voucherRepository.saveAll(updatedVouchers);

        return OrderResponse.builder()
                .orderId(order.getId())
                .customerName(order.getCustomerName())
                .originalAmount(originalAmount)
                .finalAmount(currentAmount)
                .appliedVouchers(appliedVouchers)
                .rejectedVouchers(List.of())
                .build();
    }

    private VoucherViolation createViolationStrategy(Voucher voucher) {
        if ("PERCENTAGE".equalsIgnoreCase(voucher.getType())) {
            return PercentageVoucherViolation.builder()
                    .code(voucher.getCode())
                    .discountPercent(voucher.getDiscountPercent())
                    .build();
        }

        if ("ONE_TIME".equalsIgnoreCase(voucher.getType())) {
            return OneTimeVoucherViolation.builder()
                    .code(voucher.getCode())
                    .discountAmount(voucher.getDiscountAmount())
                    .used(voucher.isUsed())
                    .build();
        }

        throw new AppException(ErrorCode.INVALID_VOUCHER_TYPE);
    }
}
