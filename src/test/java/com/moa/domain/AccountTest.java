package com.moa.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void maskedAccountNumberHidesMiddleDigits() {
        Account account = Account.builder()
                .accountNumber("1234567890123")
                .build();

        assertThat(account.getMaskedAccountNumber()).isEqualTo("1234****0123");
    }
}
