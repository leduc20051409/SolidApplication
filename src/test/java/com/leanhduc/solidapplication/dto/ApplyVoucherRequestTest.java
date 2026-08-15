package com.leanhduc.solidapplication.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ApplyVoucherRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void rejectsMissingOrBlankVoucherCodes() {
        ApplyVoucherRequest missingCodes = ApplyVoucherRequest.builder().build();
        ApplyVoucherRequest blankCode =
                ApplyVoucherRequest.builder().voucherCodes(List.of(" ")).build();

        assertThat(validator.validate(missingCodes)).isNotEmpty();
        assertThat(validator.validate(blankCode)).isNotEmpty();
    }
}
