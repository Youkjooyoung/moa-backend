package com.moa.dto.openbanking;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryReceiveResponse {

    private String rspCode;
    private String rspMessage;
    private String bankTranId;
    private String printContent;
    private String maskedAccount;
    private LocalDateTime expiresAt;
    private boolean verified;
    private String verificationType;

    public static InquiryReceiveResponse success(String bankTranId, String printContent) {
        return InquiryReceiveResponse.builder()
                .rspCode("A0000")
                .rspMessage("\uC131\uACF5")
                .bankTranId(bankTranId)
                .printContent(printContent)
                .verified(false)
                .verificationType("ONE_WON")
                .build();
    }

    public static InquiryReceiveResponse success(String bankTranId, String printContent, String maskedAccount,
            LocalDateTime expiresAt) {
        return InquiryReceiveResponse.builder()
                .rspCode("A0000")
                .rspMessage("\uC131\uACF5")
                .bankTranId(bankTranId)
                .printContent(printContent)
                .maskedAccount(maskedAccount)
                .expiresAt(expiresAt)
                .verified(false)
                .verificationType("ONE_WON")
                .build();
    }

    public static InquiryReceiveResponse error(String rspCode, String rspMessage) {
        return InquiryReceiveResponse.builder()
                .rspCode(rspCode)
                .rspMessage(rspMessage)
                .build();
    }
}
