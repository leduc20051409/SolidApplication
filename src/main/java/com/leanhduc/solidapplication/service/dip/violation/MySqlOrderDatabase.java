package com.leanhduc.solidapplication.service.dip.violation;

import com.leanhduc.solidapplication.model.Order;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySqlOrderDatabase {

    public Order save(Order order) {
        log.info(
                "MÔ PHỎNG VI PHẠM DIP: MySQL adapter nhận đơn hàng của {}",
                order.getCustomerName());

        return order;
    }
}
