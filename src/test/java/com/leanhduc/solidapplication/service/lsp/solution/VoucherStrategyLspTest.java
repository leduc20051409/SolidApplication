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
        Order order = new Order(1L, "Nguyen Van A", 500_000);
        List<VoucherStrategy> strategies = List.of(
                new PercentageVoucher("SALE15", 15),
                new OneTimeVoucher("ONETIME50K", 50_000)
        );

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
        Order order = new Order(1L, "Nguyen Van A", 500_000);
        VoucherStrategy strategy = new OneTimeVoucher("ONETIME50K", 50_000, true);

        VoucherApplicationResult result = assertDoesNotThrow(() -> apply(strategy, order));

        assertFalse(result.applied());
        assertEquals(500_000, result.finalAmount(), 0.001);
        assertFalse(result.shouldMarkUsed());
    }

    @Test
    void oneTimeVoucherCanBeCalledAgainWithoutBreakingTheParentContract() {
        Order order = new Order(1L, "Nguyen Van A", 500_000);
        VoucherStrategy strategy = new OneTimeVoucher("ONETIME50K", 50_000);

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
        assertThrows(IllegalArgumentException.class,
                () -> new PercentageVoucher("INVALID", 101));
        assertThrows(IllegalArgumentException.class,
                () -> new OneTimeVoucher("INVALID", -1));
    }

    private VoucherApplicationResult apply(VoucherStrategy strategy, Order order) {
        return strategy.apply(order);
    }
}
