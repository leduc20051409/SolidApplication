package com.leanhduc.solidapplication.service.lsp.violation;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.exception.ResourceNotFoundException;
import com.leanhduc.solidapplication.model.Order;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.repository.OrderRepository;
import com.leanhduc.solidapplication.repository.VoucherRepository;
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
                        .findById(orderId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Không tìm thấy đơn hàng ID: " + orderId));

        double originalAmount = order.getTotalAmount();
        double currentAmount = originalAmount;

        List<Voucher> vouchers = voucherRepository.findByCodeIn(request.getVoucherCodes());

        List<String> appliedVouchers = new ArrayList<>();

        for (Voucher voucher : vouchers) {
            // Biến kiểu cha chứa object của lớp con
            VoucherViolation strategy = createViolationStrategy(voucher);

            currentAmount =
                    strategy.applyDiscount(
                            new Order(order.getId(), order.getCustomerName(), currentAmount));

            voucher.setUsed(true);
            appliedVouchers.add(voucher.getCode());
        }

        order.setTotalAmount(currentAmount);

        orderRepository.save(order);
        voucherRepository.saveAll(vouchers);

        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                originalAmount,
                currentAmount,
                appliedVouchers,
                List.of());
    }

    private VoucherViolation createViolationStrategy(Voucher voucher) {
        if ("PERCENTAGE".equalsIgnoreCase(voucher.getType())) {
            return new PercentageVoucherViolation(voucher.getCode(), voucher.getDiscountPercent());
        }

        if ("ONE_TIME".equalsIgnoreCase(voucher.getType())) {
            return new OneTimeVoucherViolation(
                    voucher.getCode(), voucher.getDiscountAmount(), voucher.isUsed());
        }

        throw new IllegalArgumentException("Loại voucher không hợp lệ: " + voucher.getType());
    }
}
