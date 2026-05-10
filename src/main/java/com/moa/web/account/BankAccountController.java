package com.moa.web.account;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moa.common.exception.ApiResponse;
import com.moa.common.exception.ErrorCode;
import com.moa.domain.Account;
import com.moa.dto.openbanking.InquiryReceiveResponse;
import com.moa.dto.openbanking.InquiryVerifyResponse;
import com.moa.service.account.BankAccountService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/bank-account")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("/verify-request")
    public ResponseEntity<ApiResponse<InquiryReceiveResponse>> requestVerification(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VerifyRequestDto request) {
        String userId = userDetails.getUsername();
        log.info("[API] bank account verification requested. userId={}", userId);

        InquiryReceiveResponse response = bankAccountService.requestVerification(
                userId,
                request.getBankCode(),
                request.getAccountNum(),
                request.getAccountHolder());

        if ("A0000".equals(response.getRspCode())) {
            return ResponseEntity.ok(ApiResponse.success(response));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.BAD_REQUEST, response.getRspMessage()));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<InquiryVerifyResponse>> verifyAndRegister(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VerifyCodeDto request) {
        String userId = userDetails.getUsername();
        InquiryVerifyResponse response = bankAccountService.verifyAndRegister(
                userId,
                request.getBankTranId(),
                request.getVerifyCode());

        if (response.isVerified()) {
            return ResponseEntity.ok(ApiResponse.success(response));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.BAD_REQUEST, response.getRspMessage()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AccountResponseDto>> getAccount(
            @AuthenticationPrincipal UserDetails userDetails) {
        Account account = bankAccountService.getAccount(userDetails.getUsername());
        if (account == null) {
            return ResponseEntity.ok(ApiResponse.success(null));
        }
        return ResponseEntity.ok(ApiResponse.success(AccountResponseDto.from(account)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteAccount(
            @AuthenticationPrincipal UserDetails userDetails) {
        bankAccountService.deleteAccount(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/change")
    public ResponseEntity<ApiResponse<InquiryReceiveResponse>> changeAccount(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody VerifyRequestDto request) {
        String userId = userDetails.getUsername();
        InquiryReceiveResponse response = bankAccountService.changeAccount(
                userId,
                request.getBankCode(),
                request.getAccountNum(),
                request.getAccountHolder());

        if ("A0000".equals(response.getRspCode())) {
            return ResponseEntity.ok(ApiResponse.success(response));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCode.BAD_REQUEST, response.getRspMessage()));
    }

    @Data
    public static class VerifyRequestDto {
        @NotBlank(message = "은행 코드는 필수입니다.")
        @Size(min = 3, max = 3, message = "은행 코드는 3자리여야 합니다.")
        private String bankCode;

        @NotBlank(message = "계좌번호는 필수입니다.")
        private String accountNum;

        @NotBlank(message = "예금주명은 필수입니다.")
        private String accountHolder;
    }

    @Data
    public static class VerifyCodeDto {
        @NotBlank(message = "거래 인증 ID는 필수입니다.")
        private String bankTranId;

        @NotBlank(message = "인증번호는 필수입니다.")
        @Size(min = 4, max = 4, message = "인증번호는 4자리여야 합니다.")
        private String verifyCode;
    }

    @Data
    public static class AccountResponseDto {
        private Integer accountId;
        private String bankCode;
        private String bankName;
        private String maskedAccountNumber;
        private String accountHolder;
        private String status;

        public static AccountResponseDto from(Account account) {
            AccountResponseDto dto = new AccountResponseDto();
            dto.setAccountId(account.getAccountId());
            dto.setBankCode(account.getBankCode());
            dto.setBankName(account.getBankName());
            dto.setMaskedAccountNumber(account.getMaskedAccountNumber());
            dto.setAccountHolder(account.getAccountHolder());
            dto.setStatus(account.getStatus());
            return dto;
        }
    }
}
