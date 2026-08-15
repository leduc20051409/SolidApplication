package com.leanhduc.solidapplication.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CreateVoucherRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void acceptsValidPercentageVoucher() {
        CreateVoucherRequest request = new CreateVoucherRequest("SALE10", "PERCENTAGE", 10.0, 0.0);

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void acceptsValidOneTimeVoucher() {
        CreateVoucherRequest request =
                new CreateVoucherRequest("ONETIME50K", "ONE_TIME", 0.0, 50_000.0);

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void rejectsInvalidBasicFields() {
        CreateVoucherRequest request = new CreateVoucherRequest("sale 10", "FIXED", 101.0, -1.0);

        Set<ConstraintViolation<CreateVoucherRequest>> violations = validator.validate(request);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("code", "type", "discountPercent", "discountAmount");
    }

    @Test
    void acceptsPercentageBoundaryValues() {
        CreateVoucherRequest request =
                new CreateVoucherRequest("SALE100", "PERCENTAGE", 100.0, 0.0);

        assertThat(validator.validate(request)).isEmpty();
    }
}
