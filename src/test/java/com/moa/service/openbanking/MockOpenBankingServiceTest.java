package com.moa.service.openbanking;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MockOpenBankingServiceTest {

    @Test
    void generateVerifyCodeReturnsFourDigits() {
        MockOpenBankingService service = new MockOpenBankingService(null, null);

        String code = service.generateVerifyCode();

        assertThat(code).matches("\\d{4}");
    }
}
