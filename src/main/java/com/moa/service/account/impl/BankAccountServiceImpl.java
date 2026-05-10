package com.moa.service.account.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moa.dao.account.AccountDao;
import com.moa.dao.openbanking.AccountVerificationMapper;
import com.moa.domain.Account;
import com.moa.domain.openbanking.AccountVerification;
import com.moa.domain.openbanking.VerificationStatus;
import com.moa.dto.openbanking.InquiryReceiveResponse;
import com.moa.dto.openbanking.InquiryVerifyResponse;
import com.moa.service.account.BankAccountService;
import com.moa.service.mail.EmailService;
import com.moa.service.openbanking.MockOpenBankingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private static final int VERIFY_EXPIRY_MINUTES = 10;
    private static final Map<String, String> BANK_NAMES = Map.ofEntries(
            Map.entry("001", "한국은행"),
            Map.entry("002", "산업은행"),
            Map.entry("003", "IBK기업은행"),
            Map.entry("004", "KB국민은행"),
            Map.entry("007", "수협은행"),
            Map.entry("011", "NH농협은행"),
            Map.entry("020", "우리은행"),
            Map.entry("023", "SC제일은행"),
            Map.entry("027", "한국씨티은행"),
            Map.entry("031", "DGB대구은행"),
            Map.entry("032", "부산은행"),
            Map.entry("034", "광주은행"),
            Map.entry("035", "제주은행"),
            Map.entry("037", "전북은행"),
            Map.entry("039", "경남은행"),
            Map.entry("045", "새마을금고"),
            Map.entry("048", "신협"),
            Map.entry("050", "저축은행"),
            Map.entry("071", "우체국"),
            Map.entry("081", "하나은행"),
            Map.entry("088", "신한은행"),
            Map.entry("089", "케이뱅크"),
            Map.entry("090", "카카오뱅크"),
            Map.entry("092", "토스뱅크"));

    private final AccountDao accountDao;
    private final AccountVerificationMapper verificationMapper;
    private final MockOpenBankingService mockOpenBankingService;
    private final EmailService emailService;

    @Value("${bank-account.verification.provider:mock}")
    private String verificationProvider;

    private String getBankName(String bankCode) {
        return BANK_NAMES.getOrDefault(bankCode, "은행");
    }

    private String maskAccountNumber(String accountNum) {
        if (accountNum == null || accountNum.length() < 6) {
            return accountNum;
        }
        int len = accountNum.length();
        return accountNum.substring(0, 3) + "-***-***" + accountNum.substring(len - 3);
    }

    @Override
    @Transactional
    public InquiryReceiveResponse requestVerification(String userId, String bankCode, String accountNum,
            String accountHolder) {
        log.info("[bank-account] verification requested. userId={}, bankCode={}, provider={}",
                userId, bankCode, verificationProvider);

        int expiredCount = verificationMapper.expirePendingByUserId(userId);
        if (expiredCount > 0) {
            log.info("[bank-account] expired pending verifications. userId={}, count={}", userId, expiredCount);
        }

        return requestMockOneWonVerification(userId, bankCode, accountNum, accountHolder);
    }

    private InquiryReceiveResponse requestMockOneWonVerification(String userId, String bankCode, String accountNum,
            String accountHolder) {
        String verifyCode = mockOpenBankingService.generateVerifyCode();
        String bankTranId = "MOCK" + System.currentTimeMillis();
        AccountVerification verification = AccountVerification.builder()
                .userId(userId)
                .bankTranId(bankTranId)
                .bankCode(bankCode)
                .accountNum(accountNum)
                .accountHolder(accountHolder)
                .verifyCode(verifyCode)
                .attemptCount(0)
                .status(VerificationStatus.PENDING)
                .expiredAt(LocalDateTime.now().plusMinutes(VERIFY_EXPIRY_MINUTES))
                .build();

        verificationMapper.insert(verification);
        emailService.sendBankVerificationEmail(userId, getBankName(bankCode), maskAccountNumber(accountNum), verifyCode);

        return InquiryReceiveResponse.success(
                bankTranId,
                verifyCode,
                maskAccountNumber(accountNum),
                verification.getExpiredAt());
    }

    @Override
    @Transactional
    public InquiryVerifyResponse verifyAndRegister(String userId, String bankTranId, String verifyCode) {
        log.info("[bank-account] one-won verification code submitted. userId={}, bankTranId={}", userId, bankTranId);

        AccountVerification verification = verificationMapper.findByBankTranId(bankTranId);

        if (verification == null) {
            return InquiryVerifyResponse.fail("A0004", "인증 요청 정보를 찾을 수 없습니다.");
        }
        if (!verification.getUserId().equals(userId)) {
            return InquiryVerifyResponse.fail("A0004", "접근할 수 없는 인증 요청입니다.");
        }
        if (VerificationStatus.VERIFIED == verification.getStatus()) {
            return InquiryVerifyResponse.success(verification.getBankTranId());
        }
        if (LocalDateTime.now().isAfter(verification.getExpiredAt())) {
            verificationMapper.updateStatus(verification.getVerificationId(), "EXPIRED");
            return InquiryVerifyResponse.fail("A0004", "인증 시간이 만료되었습니다.");
        }
        if (VerificationStatus.PENDING != verification.getStatus()) {
            return InquiryVerifyResponse.fail("A0004", "이미 처리된 인증 요청입니다.");
        }

        verificationMapper.incrementAttemptCount(verification.getVerificationId());
        int newAttemptCount = verification.getAttemptCount() + 1;

        if (!verification.getVerifyCode().equals(verifyCode)) {
            if (newAttemptCount >= 3) {
                verificationMapper.updateStatus(verification.getVerificationId(), "FAILED");
                return InquiryVerifyResponse.fail("A0005", "인증번호 입력 횟수를 초과했습니다.");
            }
            return InquiryVerifyResponse.fail("A0003", "인증번호가 일치하지 않습니다.");
        }

        String fintechUseNum = UUID.randomUUID().toString().replace("-", "").substring(0, 24);
        verificationMapper.updateStatus(verification.getVerificationId(), "VERIFIED");
        upsertVerifiedAccount(
                userId,
                verification.getBankCode(),
                verification.getAccountNum(),
                verification.getAccountHolder(),
                fintechUseNum);

        return InquiryVerifyResponse.success(fintechUseNum);
    }

    @Override
    public Account getAccount(String userId) {
        return accountDao.findActiveByUserId(userId).orElse(null);
    }

    @Override
    @Transactional
    public void deleteAccount(String userId) {
        Optional<Account> account = accountDao.findActiveByUserId(userId);
        account.ifPresent(acc -> accountDao.updateStatus(acc.getAccountId(), "INACTIVE"));
    }

    @Override
    @Transactional
    public InquiryReceiveResponse changeAccount(String userId, String bankCode, String accountNum,
            String accountHolder) {
        return requestVerification(userId, bankCode, accountNum, accountHolder);
    }

    private void upsertVerifiedAccount(String userId, String bankCode, String accountNum, String accountHolder,
            String fintechUseNum) {
        Account account = Account.builder()
                .userId(userId)
                .bankCode(bankCode)
                .bankName(getBankName(bankCode))
                .accountNumber(accountNum)
                .accountHolder(accountHolder)
                .fintechUseNum(fintechUseNum)
                .status("ACTIVE")
                .isVerified("Y")
                .build();

        if (accountDao.findByUserId(userId).isPresent()) {
            accountDao.updateAccountByUserId(account);
        } else {
            accountDao.insertAccount(account);
        }
    }
}
