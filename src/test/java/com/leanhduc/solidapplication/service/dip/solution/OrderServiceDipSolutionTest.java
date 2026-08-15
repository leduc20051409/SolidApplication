package com.leanhduc.solidapplication.service.dip.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.leanhduc.solidapplication.config.DipConfiguration;
import com.leanhduc.solidapplication.enums.ErrorCode;
import com.leanhduc.solidapplication.exception.AppException;
import com.leanhduc.solidapplication.model.Order;
import org.junit.jupiter.api.Test;

class OrderServiceDipSolutionTest {

    @Test
    void usesTheInjectedStorageWithoutDependingOnItsConcreteType() {
        OrderStorage storage = mock(OrderStorage.class);
        when(storage.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        OrderServiceDipSolution service = new OrderServiceDipSolution(storage);

        Order result = service.createOrder("Nguyen Van A", 500_000);

        assertEquals("Nguyen Van A", result.getCustomerName());
        assertEquals(500_000, result.getTotalAmount(), 0.001);
        verify(storage).save(result);
    }

    @Test
    void configurationSelectsTheConcreteAdapterOutsideTheBusinessService() {
        OrderStorage storage = new DipConfiguration().orderStorage();

        assertInstanceOf(PostgreSqlOrderStorage.class, storage);
    }

    @Test
    void rejectsInvalidOrderAmountsBeforeCallingStorage() {
        OrderStorage storage = mock(OrderStorage.class);
        OrderServiceDipSolution service = new OrderServiceDipSolution(storage);

        AppException notFinite =
                assertThrows(
                        AppException.class, () -> service.createOrder("Nguyen Van A", Double.NaN));
        AppException negative =
                assertThrows(AppException.class, () -> service.createOrder("Nguyen Van A", -1));

        assertEquals(ErrorCode.INVALID_ORDER_AMOUNT, notFinite.getErrorCode());
        assertEquals(ErrorCode.INVALID_ORDER_AMOUNT, negative.getErrorCode());
    }
}
