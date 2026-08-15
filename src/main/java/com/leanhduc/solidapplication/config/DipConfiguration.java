package com.leanhduc.solidapplication.config;

import com.leanhduc.solidapplication.service.dip.solution.OrderStorage;
import com.leanhduc.solidapplication.service.dip.solution.PostgreSqlOrderStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DipConfiguration {

    @Bean
    public OrderStorage orderStorage() {
        return new PostgreSqlOrderStorage();
    }
}
