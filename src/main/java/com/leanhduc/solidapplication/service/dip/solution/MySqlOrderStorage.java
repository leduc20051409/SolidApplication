package com.leanhduc.solidapplication.service.dip.solution;

import com.leanhduc.solidapplication.model.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySqlOrderStorage implements OrderStorage {

    @Override
    public Order save(Order order) {
        log.info("MÔ PHỎNG DIP: MySQL adapter nhận đơn hàng của {}", order.getCustomerName());

        return order;
    }
}
