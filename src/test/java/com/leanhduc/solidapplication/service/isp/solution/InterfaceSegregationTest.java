package com.leanhduc.solidapplication.service.isp.solution;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InterfaceSegregationTest {

    @Test
    void customerAndAdminDependOnlyOnTheirOwnContracts() {
        assertTrue(VoucherApplicationService.class.isAssignableFrom(CustomerVoucherService.class));
        assertFalse(VoucherManagementService.class.isAssignableFrom(CustomerVoucherService.class));

        assertTrue(VoucherManagementService.class.isAssignableFrom(AdminVoucherService.class));
        assertFalse(VoucherApplicationService.class.isAssignableFrom(AdminVoucherService.class));
    }
}
