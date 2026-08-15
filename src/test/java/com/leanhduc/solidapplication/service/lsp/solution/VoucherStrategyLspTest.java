package com.leanhduc.solidapplication.service.lsp.solution;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.leanhduc.solidapplication.model.Order;
import java.util.List;
import org.junit.jupiter.api.Test;

class VoucherStrategyLspTest {

    @Test
    void everyStrategyCanReplaceTheParentContract() {
        Order order =
                Order.builder().id(1L).customerName("Nguyen Van A").totalAmount(500_000).build();
        List<VoucherStrategy> strategies =
                List.of(
                        PercentageVoucher.builder().code("SALE15").discountPercent(15).build(),
                        OneTimeVoucher.builder().code("ONETIME50K").discountAmount(50_000).build());

        for (VoucherStrategy strategy : strategies) {
            VoucherApplicationResult result = assertDoesNotThrow(() -> apply(strategy, order));

            assertTrue(result.applied());
            assertTrue(Double.isFinite(result.finalAmount()));
            assertTrue(result.finalAmount() >= 0);
            assertTrue(result.finalAmount() <= order.getTotalAmount());
            assertFalse(result.shouldMarkUsed() && !result.applied());
        }
    }

    @Test
    void usedOneTimeVoucherReturnsRejectionInsteadOfBreakingTheContract() {
        Order order =
                Order.builder().id(1L).customerName("Nguyen Van A").totalAmount(500_000).build();
        VoucherStrategy strategy =
                OneTimeVoucher.builder()
                        .code("ONETIME50K")
                        .discountAmount(50_000)
                        .used(true)
                        .build();

        VoucherApplicationResult result = assertDoesNotThrow(() -> apply(strategy, order));

        assertFalse(result.applied());
        assertEquals(500_000, result.finalAmount(), 0.001);
        assertFalse(result.shouldMarkUsed());
    }

    @Test
    void oneTimeVoucherCanBeCalledAgainWithoutBreakingTheParentContract() {
        Order order =
                Order.builder().id(1L).customerName("Nguyen Van A").totalAmount(500_000).build();
        VoucherStrategy strategy =
                OneTimeVoucher.builder().code("ONETIME50K").discountAmount(50_000).build();

        VoucherApplicationResult firstResult = assertDoesNotThrow(() -> apply(strategy, order));
        VoucherApplicationResult secondResult = assertDoesNotThrow(() -> apply(strategy, order));

        assertTrue(firstResult.applied());
        assertEquals(450_000, firstResult.finalAmount(), 0.001);
        assertTrue(firstResult.shouldMarkUsed());
        assertFalse(secondResult.applied());
        assertEquals(500_000, secondResult.finalAmount(), 0.001);
    }

    @Test
    void strategiesRejectInvalidDiscountConfiguration() {
        assertThrows(
                IllegalArgumentException.class,
                () -> PercentageVoucher.builder().code("INVALID").discountPercent(101).build());
        assertThrows(
                IllegalArgumentException.class,
                () -> OneTimeVoucher.builder().code("INVALID").discountAmount(-1).build());
    }

    private VoucherApplicationResult apply(VoucherStrategy strategy, Order order) {
        return strategy.apply(order);
    }
}
