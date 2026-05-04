-- ============================================================
-- 03_sample_data.sql
-- MOA OTT 구독 공유 서비스
-- 전체 샘플 데이터 (27개 테이블)
-- ============================================================

USE moa;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- USERS
-- ============================================================

INSERT INTO USERS (
  USER_ID, PASSWORD, NICKNAME, PHONE, PROFILE_IMAGE, ROLE, USER_STATUS,
  REG_DATE, CI, PASS_CERTIFIED_AT, LAST_LOGIN_DATE, LOGIN_FAIL_COUNT,
  AGREE_MARKETING, PROVIDER, OTP_SECRET, OTP_ENABLED
) VALUES
('user001@gmail.com', NULL, '유저001', '010-1111-1111', NULL, 'USER', 'ACTIVE',
 '2025-12-01 10:00:00', 'CI_USER001', '2025-12-01 10:05:00', '2025-12-20 12:00:00', 0,
 0, 'LOCAL', NULL, 0),

('user002@gmail.com', NULL, '유저002', '010-2222-2222', NULL, 'USER', 'ACTIVE',
 '2025-12-02 11:00:00', 'CI_USER002', '2025-12-02 11:05:00', '2025-12-21 13:00:00', 0,
 1, 'KAKAO', 'OTPSECRET002', 1),

('admin@moa.com', NULL, '관리자', '010-9999-9999', NULL, 'ADMIN', 'ACTIVE',
 '2025-11-30 09:00:00', 'CI_ADMIN', '2025-11-30 09:05:00', '2025-12-22 14:00:00', 0,
 0, 'LOCAL', NULL, 0);

-- ============================================================
-- OAUTH_ACCOUNT
-- ============================================================

INSERT INTO OAUTH_ACCOUNT
(OAUTH_ID, PROVIDER, PROVIDER_USER_ID, USER_ID, CONNECTED_DATE)
VALUES
('OAUTH_KAKAO_001','KAKAO','KAKAO_UID_001','user002@gmail.com','2025-12-02 11:02:00');

-- ============================================================
-- BLACKLIST
-- ============================================================

INSERT INTO BLACKLIST
(USER_ID, REASON, STATUS, REG_DATE, RELEASE_DATE)
VALUES
('user001@gmail.com','테스트 블락','INACTIVE','2025-12-10','2025-12-20');

-- ============================================================
-- EMAIL_VERIFICATION
-- ============================================================

INSERT INTO EMAIL_VERIFICATION
(USER_ID, TOKEN, EXPIRES_AT, VERIFIED_AT)
VALUES
('user001@gmail.com','EMAIL_TOKEN_001',DATE_ADD(NOW(),INTERVAL 30 MINUTE),NOW());

-- ============================================================
-- LOGIN_HISTORY
-- ============================================================

INSERT INTO LOGIN_HISTORY
(USER_ID, LOGIN_AT, SUCCESS, LOGIN_IP, USER_AGENT, LOGIN_TYPE)
VALUES
('user001@gmail.com','2025-12-20 12:00:00',1,'1.1.1.1','Chrome','LOCAL'),
('user001@gmail.com','2025-12-20 12:05:00',0,'1.1.1.1','Chrome','LOCAL'),
('user002@gmail.com','2025-12-21 13:00:00',1,'2.2.2.2','Chrome','KAKAO');

-- ============================================================
-- USER_OTP_BACKUP_CODE
-- ============================================================

INSERT INTO USER_OTP_BACKUP_CODE
(USER_ID, CODE_HASH, USED)
VALUES
('user002@gmail.com','$2a$10$backup001',0),
('user002@gmail.com','$2a$10$backup002',1);

-- ============================================================
-- ACCOUNT
-- ============================================================

INSERT INTO ACCOUNT
(USER_ID, BANK_CODE, BANK_NAME, ACCOUNT_NUMBER, ACCOUNT_HOLDER,
 IS_VERIFIED, FINTECH_USE_NUM, STATUS, REG_DATE, VERIFY_DATE)
VALUES
('user001@gmail.com','004','KB국민은행','ENC_ACC_001','유저001','Y','FIN_001','ACTIVE',NOW(),NOW()),
('user002@gmail.com','088','신한은행','ENC_ACC_002','유저002','Y','FIN_002','ACTIVE',NOW(),NOW());

-- ============================================================
-- USER_CARD
-- ============================================================

INSERT INTO USER_CARD
(USER_ID, BILLING_KEY, CARD_COMPANY, CARD_NUMBER)
VALUES
('user001@gmail.com','BILLKEY001','삼성','1111-****-****-1111'),
('user002@gmail.com','BILLKEY002','현대','2222-****-****-2222');

-- ============================================================
-- SUBSCRIPTION
-- ============================================================

INSERT INTO SUBSCRIPTION
(USER_ID, PRODUCT_ID, SUBSCRIPTION_STATUS, START_DATE)
VALUES
('user001@gmail.com',3,'ACTIVE','2025-12-01'),
('user002@gmail.com',2,'ACTIVE','2025-12-02');

-- ============================================================
-- PARTY
-- ============================================================

INSERT INTO PARTY
(PRODUCT_ID, PARTY_LEADER_ID, PARTY_STATUS, MAX_MEMBERS, CURRENT_MEMBERS,
 MONTHLY_FEE, ACCOUNT_ID, REG_DATE, START_DATE)
VALUES
(3,'user001@gmail.com','ACTIVE',4,2,4750,1,NOW(),'2025-12-05 00:00:00'),
(2,'user002@gmail.com','ACTIVE',3,1,9667,2,NOW(),'2025-12-06 00:00:00');

-- ============================================================
-- PARTY_MEMBER
-- ============================================================

INSERT INTO PARTY_MEMBER
(PARTY_ID, USER_ID, MEMBER_ROLE, MEMBER_STATUS)
VALUES
(1,'user001@gmail.com','LEADER','ACTIVE'),
(1,'user002@gmail.com','MEMBER','ACTIVE'),
(2,'user002@gmail.com','LEADER','ACTIVE');

-- ============================================================
-- DEPOSIT
-- ============================================================

INSERT INTO DEPOSIT
(PARTY_ID, PARTY_MEMBER_ID, USER_ID, DEPOSIT_TYPE, DEPOSIT_AMOUNT,
 DEPOSIT_STATUS, PAYMENT_DATE, TOSS_PAYMENT_KEY, ORDER_ID)
VALUES
(1,1,'user001@gmail.com','LEADER',10000,'PAID',NOW(),'TOSS_DEP_001','ORD_DEP_001'),
(1,2,'user002@gmail.com','SECURITY',5000,'PAID',NOW(),'TOSS_DEP_002','ORD_DEP_002');

-- ============================================================
-- SETTLEMENT
-- ============================================================

INSERT INTO SETTLEMENT
(PARTY_ID, PARTY_LEADER_ID, ACCOUNT_ID, SETTLEMENT_MONTH,
 TOTAL_AMOUNT, COMMISSION_AMOUNT, NET_AMOUNT,
 SETTLEMENT_STATUS, SETTLEMENT_DATE)
VALUES
(1,'user001@gmail.com',1,'2025-12',9500,1425,8075,'COMPLETED',NOW());

-- ============================================================
-- TRANSFER_TRANSACTION
-- ============================================================

INSERT INTO TRANSFER_TRANSACTION
(SETTLEMENT_ID, BANK_TRAN_ID, FINTECH_USE_NUM, TRAN_AMT,
 PRINT_CONTENT, REQ_CLIENT_NAME, STATUS)
VALUES
(1,'BANKTRAN_001','FIN_001',8075,'MOA정산','유저001','SUCCESS');

-- ============================================================
-- PAYMENT
-- ============================================================

INSERT INTO PAYMENT
(PARTY_ID, PARTY_MEMBER_ID, USER_ID, PAYMENT_AMOUNT,
 PAYMENT_STATUS, PAYMENT_METHOD, TARGET_MONTH, SETTLEMENT_ID)
VALUES
(1,1,'user001@gmail.com',4750,'SUCCESS','CARD','2025-12',1),
(1,2,'user002@gmail.com',4750,'SUCCESS','CARD','2025-12',1);

-- ============================================================
-- PAYMENT_RETRY_HISTORY
-- ============================================================

INSERT INTO PAYMENT_RETRY_HISTORY
(PAYMENT_ID, PARTY_ID, PARTY_MEMBER_ID, ATTEMPT_NUMBER, ATTEMPT_DATE,
 RETRY_STATUS)
VALUES
(1,1,1,1,NOW(),'SUCCESS'),
(2,1,2,1,NOW(),'FAILED');

-- ============================================================
-- REFUND_RETRY_HISTORY
-- ============================================================

INSERT INTO REFUND_RETRY_HISTORY
(DEPOSIT_ID, ATTEMPT_NUMBER, ATTEMPT_DATE,
 RETRY_STATUS, RETRY_TYPE, REFUND_AMOUNT)
VALUES
(2,1,NOW(),'FAILED','REFUND',5000);

-- ============================================================
-- SETTLEMENT_RETRY_HISTORY
-- ============================================================

INSERT INTO SETTLEMENT_RETRY_HISTORY
(SETTLEMENT_ID, ATTEMPT_NUMBER, ATTEMPT_DATE,
 RETRY_STATUS, TRANSFER_AMOUNT)
VALUES
(1,1,NOW(),'SUCCESS',8075);

SET FOREIGN_KEY_CHECKS = 1;
