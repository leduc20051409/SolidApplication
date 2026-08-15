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

        Order order =
                Order.builder().id(1L).customerName("Nguyen Van A").totalAmount(500_000).build();
        Voucher usedOneTime =
                Voucher.builder()
                        .code("ONETIME50K")
                        .type("ONE_TIME")
                        .discountAmount(50_000)
                        .used(true)
                        .build();
        Voucher percentage =
                Voucher.builder()
                        .code("SALE10")
                        .type("PERCENTAGE")
                        .discountPercent(10)
                        .used(false)
                        .build();
        ApplyVoucherRequest request =
                ApplyVoucherRequest.builder().voucherCodes(List.of("ONETIME50K", "SALE10")).build();

        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
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

    @Test
    void appliesVouchersInTheOrderRequestedByTheClient() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        VoucherRepository voucherRepository = mock(VoucherRepository.class);
        VoucherServiceImpl service = new VoucherServiceImpl(orderRepository, voucherRepository);

        Order order =
                Order.builder().id(1L).customerName("Nguyen Van A").totalAmount(500_000).build();
        Voucher percentage =
                Voucher.builder().code("SALE10").type("PERCENTAGE").discountPercent(10).build();
        Voucher oneTime =
                Voucher.builder()
                        .code("ONETIME50K")
                        .type("ONE_TIME")
                        .discountAmount(50_000)
                        .build();
        ApplyVoucherRequest request =
                ApplyVoucherRequest.builder().voucherCodes(List.of("SALE10", "ONETIME50K")).build();

        when(orderRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(order));
        when(voucherRepository.findByCodeIn(request.getVoucherCodes()))
                .thenReturn(List.of(oneTime, percentage));

        OrderResponse response = service.applyVouchers(1L, request);

        assertEquals(400_000, response.getFinalAmount(), 0.001);
        assertEquals(List.of("SALE10", "ONETIME50K"), response.getAppliedVouchers());
    }
}
