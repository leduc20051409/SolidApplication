package com.leanhduc.solidapplication.service.lsp.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.dto.OrderResponse;
import com.leanhduc.solidapplication.model.Order;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.repository.OrderRepository;
import com.leanhduc.solidapplication.repository.VoucherRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class VoucherServiceImplTest {

    @Test
    void rejectedStrategyDoesNotStopFollowingStrategies() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        VoucherRepository voucherRepository = mock(VoucherRepository.class);
        VoucherServiceImpl service = new VoucherServiceImpl(orderRepository, voucherRepository);

        Order order = new Order(1L, "Nguyen Van A", 500_000);
        Voucher usedOneTime = new Voucher(null, "ONETIME50K", "ONE_TIME", 0, 50_000, true);
        Voucher percentage = new Voucher(null, "SALE10", "PERCENTAGE", 10, 0, false);
        ApplyVoucherRequest request = new ApplyVoucherRequest();
        request.setVoucherCodes(List.of("ONETIME50K", "SALE10"));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(voucherRepository.findByCodeIn(request.getVoucherCodes()))
                .thenReturn(List.of(usedOneTime, percentage));

        OrderResponse response = service.applyVouchers(1L, request);

        assertEquals(450_000, response.getFinalAmount(), 0.001);
        assertEquals(List.of("SALE10"), response.getAppliedVouchers());
        assertEquals(List.of("ONETIME50K"), response.getRejectedVouchers());
        assertFalse(percentage.isUsed());
        verify(orderRepository).save(order);
        verify(voucherRepository).saveAll(List.of());
    }
}
