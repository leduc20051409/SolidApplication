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
        CreateVoucherRequest request =
                CreateVoucherRequest.builder()
                        .code("SALE10")
                        .type("PERCENTAGE")
                        .discountPercent(10.0)
                        .discountAmount(0.0)
                        .build();

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void acceptsValidOneTimeVoucher() {
        CreateVoucherRequest request =
                CreateVoucherRequest.builder()
                        .code("ONETIME50K")
                        .type("ONE_TIME")
                        .discountPercent(0.0)
                        .discountAmount(50_000.0)
                        .build();

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void rejectsInvalidBasicFields() {
        CreateVoucherRequest request =
                CreateVoucherRequest.builder()
                        .code("sale 10")
                        .type("FIXED")
                        .discountPercent(101.0)
                        .discountAmount(-1.0)
                        .build();

        Set<ConstraintViolation<CreateVoucherRequest>> violations = validator.validate(request);

        assertThat(violations)
                .extracting(violation -> violation.getPropertyPath().toString())
                .contains("code", "type", "discountPercent", "discountAmount");
    }

    @Test
    void acceptsPercentageBoundaryValues() {
        CreateVoucherRequest request =
                CreateVoucherRequest.builder()
                        .code("SALE100")
                        .type("PERCENTAGE")
                        .discountPercent(100.0)
                        .discountAmount(0.0)
                        .build();

        assertThat(validator.validate(request)).isEmpty();
    }
}
