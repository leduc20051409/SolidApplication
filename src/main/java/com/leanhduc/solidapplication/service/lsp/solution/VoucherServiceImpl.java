package com.leanhduc.solidapplication.service.lsp.solution;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.exception.ResourceNotFoundException;
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

@Service("lspSolutionVoucherService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServiceImpl implements VoucherService {

    OrderRepository orderRepository;
    VoucherRepository voucherRepository;

    @Override
    @Transactional
    public OrderResponse applyVouchers(Long orderId, ApplyVoucherRequest request) {
        Order order =
                orderRepository
                        .findByIdForUpdate(orderId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Không tìm thấy đơn hàng ID: " + orderId));

        double originalAmount = order.getTotalAmount();
        double currentAmount = originalAmount;

        List<Voucher> fetchedVouchers = voucherRepository.findByCodeIn(request.getVoucherCodes());
        List<Voucher> vouchers =
                VoucherOrderer.followRequestOrder(request.getVoucherCodes(), fetchedVouchers);
        List<String> appliedVouchers = new ArrayList<>();
        List<String> rejectedVouchers = new ArrayList<>();
        List<Voucher> updatedVouchers = new ArrayList<>();

        for (Voucher voucherModel : vouchers) {
            VoucherStrategy strategy = createStrategy(voucherModel);
            Order tempOrder =
                    Order.builder()
                            .id(order.getId())
                            .customerName(order.getCustomerName())
                            .totalAmount(currentAmount)
                            .build();
            VoucherApplicationResult result = strategy.apply(tempOrder);

            if (result.applied()) {
                currentAmount = result.finalAmount();
                if (result.shouldMarkUsed()) {
                    voucherModel.setUsed(true);
                    updatedVouchers.add(voucherModel);
                }
                appliedVouchers.add(voucherModel.getCode());
            } else {
                rejectedVouchers.add(voucherModel.getCode());
            }
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
                .rejectedVouchers(rejectedVouchers)
                .build();
    }

    private VoucherStrategy createStrategy(Voucher voucherModel) {
        if ("PERCENTAGE".equalsIgnoreCase(voucherModel.getType())) {
            return PercentageVoucher.builder()
                    .code(voucherModel.getCode())
                    .discountPercent(voucherModel.getDiscountPercent())
                    .build();
        } else if ("ONE_TIME".equalsIgnoreCase(voucherModel.getType())) {
            return OneTimeVoucher.builder()
                    .code(voucherModel.getCode())
                    .discountAmount(voucherModel.getDiscountAmount())
                    .used(voucherModel.isUsed())
                    .build();
        }
        throw new IllegalArgumentException("Loại voucher không hợp lệ: " + voucherModel.getType());
    }
}
