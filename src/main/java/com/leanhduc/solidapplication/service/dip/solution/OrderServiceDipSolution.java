package com.leanhduc.solidapplication.service.dip.solution;

import com.leanhduc.solidapplication.enums.ErrorCode;
import com.leanhduc.solidapplication.exception.AppException;
import com.leanhduc.solidapplication.model.Order;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceDipSolution {

    OrderStorage orderStorage;

    public Order createOrder(String customerName, double totalAmount) {
        validateTotalAmount(totalAmount);
        Order order = Order.builder().customerName(customerName).totalAmount(totalAmount).build();

        return orderStorage.save(order);
    }

    private void validateTotalAmount(double totalAmount) {
        if (!Double.isFinite(totalAmount) || totalAmount < 0) {
            throw new AppException(ErrorCode.INVALID_ORDER_AMOUNT);
        }
    }
}
