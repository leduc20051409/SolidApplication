package com.leanhduc.solidapplication.service.lsp.violation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.leanhduc.solidapplication.dto.ApplyVoucherRequest;
import com.leanhduc.solidapplication.model.Order;
import com.leanhduc.solidapplication.model.Voucher;
import com.leanhduc.solidapplication.repository.OrderRepository;
import com.leanhduc.solidapplication.repository.VoucherRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class VoucherServiceViolationImplTest {

    @Test
    void onlyOneTimeVouchersAreMarkedAsUsed() {
        OrderRepository orderRepository = mock(OrderRepository.class);
        VoucherRepository voucherRepository = mock(VoucherRepository.class);
        VoucherServiceViolationImpl service =
                new VoucherServiceViolationImpl(orderRepository, voucherRepository);

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
                .thenReturn(List.of(percentage, oneTime));

        service.applyVouchers(1L, request);

        assertFalse(percentage.isUsed());
        assertTrue(oneTime.isUsed());
    }
}
