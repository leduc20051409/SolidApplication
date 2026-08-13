package com.leanhduc.solidapplication.service.lsp.violation;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.model.Order;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.repository.OrderRepository;
import com.leanhduc.solidapplication.repository.VoucherRepository;
import com.leanhduc.solidapplication.service.VoucherService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("lspViolationVoucherService")
public class VoucherServiceViolationImpl implements VoucherService {

    private final OrderRepository orderRepository;
    private final VoucherRepository voucherRepository;

    public VoucherServiceViolationImpl(OrderRepository orderRepository, VoucherRepository voucherRepository) {
        this.orderRepository = orderRepository;
        this.voucherRepository = voucherRepository;
    }

    @Override
    @Transactional
    public OrderResponse applyVouchers(Long orderId, ApplyVoucherRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng ID: " + orderId));

        double originalAmount = order.getTotalAmount();
        double currentAmount = originalAmount;

        List<Voucher> vouchers = voucherRepository.findByCodeIn(request.getVoucherCodes());
        List<String> appliedVouchers = new ArrayList<>();
        List<Voucher> updatedVouchers = new ArrayList<>();

        for (Voucher voucherModel : vouchers) {
            VoucherViolation strategy = createViolationStrategy(voucherModel);
            Order tempOrder = new Order(order.getId(), order.getCustomerName(), currentAmount);

            // ❌ VI PHẠM LSP: Gọi trực tiếp applyDiscount() mà KHÔNG THỂ kiểm tra trước.
            // Nếu voucherModel.isUsed() == true → OneTimeVoucherViolation ném Exception sập API!
            currentAmount = strategy.applyDiscount(tempOrder);

            voucherModel.setUsed(true);
            updatedVouchers.add(voucherModel);
            appliedVouchers.add(voucherModel.getCode());
        }

        order.setTotalAmount(currentAmount);
        orderRepository.save(order);
        voucherRepository.saveAll(updatedVouchers);

        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                originalAmount,
                currentAmount,
                appliedVouchers,
                List.of()
        );
    }

    private VoucherViolation createViolationStrategy(Voucher voucherModel) {
        if ("PERCENTAGE".equalsIgnoreCase(voucherModel.getType())) {
            return new PercentageVoucherViolation(voucherModel.getCode(), voucherModel.getDiscountPercent());
        } else if ("ONE_TIME".equalsIgnoreCase(voucherModel.getType())) {
            return new OneTimeVoucherViolation(voucherModel.getCode(), voucherModel.getDiscountAmount(), voucherModel.isUsed());
        }
        throw new IllegalArgumentException("Loại voucher không hợp lệ: " + voucherModel.getType());
    }
}
