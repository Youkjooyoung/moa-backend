-- ============================================================
-- 02_master_data.sql
-- MOA OTT 구독 공유 서비스
-- 코드 / 마스터 샘플 데이터
-- ============================================================

USE moa;

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- BANK_CODE
-- ============================================================

INSERT INTO BANK_CODE (BANK_CODE, BANK_NAME, IS_ACTIVE) VALUES
('003','IBK기업은행','Y'),
('004','KB국민은행','Y'),
('011','NH농협은행','Y'),
('020','우리은행','Y'),
('023','SC제일은행','Y'),
('027','한국씨티은행','Y'),
('032','대구은행','Y'),
('034','광주은행','Y'),
('035','제주은행','Y'),
('037','전북은행','Y'),
('039','경남은행','Y'),
('045','새마을금고','Y'),
('048','신협','Y'),
('071','우체국','Y'),
('081','하나은행','Y'),
('088','신한은행','Y'),
('089','케이뱅크','Y'),
('090','카카오뱅크','Y'),
('092','토스뱅크','Y');

-- ============================================================
-- COMMUNITY_CODE
-- ============================================================

INSERT INTO COMMUNITY_CODE (COMMUNITY_CODE_ID, CATEGORY, CODE_NAME) VALUES
(1,'INQUIRY','회원'),
(2,'INQUIRY','결제'),
(3,'INQUIRY','기타'),
(4,'POST','FAQ'),
(5,'POST','회원'),
(6,'POST','결제'),
(7,'POST','구독'),
(8,'POST','파티'),
(9,'POST','정산'),
(10,'POST','시스템');

-- ============================================================
-- PUSH_CODE
-- ============================================================

INSERT INTO PUSH_CODE (CODE_NAME, TITLE_TEMPLATE, CONTENT_TEMPLATE) VALUES
('INQUIRY_ANSWER','문의 답변 완료','{nickname}님 문의에 답변이 등록되었습니다.'),
('PARTY_JOIN','파티 가입 완료','{productName} 파티에 참여하셨습니다.'),
('PARTY_WITHDRAW','파티 탈퇴','{productName} 파티에서 탈퇴되었습니다.'),
('PARTY_START','파티 시작','{productName} 파티가 시작되었습니다.'),
('PARTY_CLOSED','파티 종료','{productName} 파티가 종료되었습니다.'),
('PARTY_MEMBER_JOIN','파티원 참여','새 파티원이 참여했습니다.'),
('PARTY_MEMBER_WITHDRAW','파티원 탈퇴','파티원이 탈퇴했습니다.'),
('PAY_UPCOMING','결제 예정','내일 결제가 예정되어 있습니다.'),
('PAY_SUCCESS','결제 완료','결제가 정상 처리되었습니다.'),
('PAY_FAILED','결제 실패','결제가 실패했습니다.'),
('PAY_RETRY_SUCCESS','재결제 성공','재시도 결제가 성공했습니다.'),
('PAY_FINAL_FAILED','결제 최종 실패','결제가 최종 실패했습니다.'),
('DEPOSIT_REFUNDED','보증금 환불','보증금이 환불되었습니다.'),
('DEPOSIT_FORFEITED','보증금 몰수','보증금이 몰수되었습니다.'),
('SETTLE_COMPLETED','정산 완료','정산금이 입금되었습니다.'),
('SETTLE_FAILED','정산 실패','정산 처리에 실패했습니다.'),
('ACCOUNT_REQUIRED','계좌 등록 필요','계좌 등록이 필요합니다.'),
('VERIFY_REQUESTED','1원 인증 요청','1원 인증이 요청되었습니다.'),
('ACCOUNT_VERIFIED','계좌 인증 완료','계좌 인증이 완료되었습니다.'),
('VERIFY_EXPIRED','인증 만료','인증 시간이 만료되었습니다.');

-- ============================================================
-- CATEGORY
-- ============================================================

INSERT INTO CATEGORY (CATEGORY_ID, CATEGORY_NAME) VALUES
(1,'AI'),
(2,'MEDIA'),
(3,'EDU'),
(4,'MEMBER');

-- ============================================================
-- PRODUCT
-- ============================================================

INSERT INTO PRODUCT
(PRODUCT_ID, CATEGORY_ID, PRODUCT_NAME, PRODUCT_STATUS, PRICE, IMAGE, MAX_SHARE)
VALUES
(1,1,'Google AI Pro','ACTIVE',17000,'/uploads/product-image/google_ai.png',NULL),
(2,1,'ChatGPT Plus','ACTIVE',29000,'/uploads/product-image/chatgpt.png',NULL),
(3,2,'Netflix Basic','ACTIVE',9500,'/uploads/product-image/netflix.png',4),
(4,2,'Netflix Standard','ACTIVE',14500,'/uploads/product-image/netflix.png',4),
(5,2,'Netflix Premium','ACTIVE',19000,'/uploads/product-image/netflix.png',4),
(6,2,'Disney+ Standard','ACTIVE',9900,'/uploads/product-image/disney.png',4),
(7,2,'Disney+ Premium','ACTIVE',13900,'/uploads/product-image/disney.png',4),
(8,2,'Tving Standard','ACTIVE',10900,'/uploads/product-image/tving.png',4),
(9,2,'Wavve Premium','ACTIVE',13900,'/uploads/product-image/wavve.png',4),
(10,4,'Naver Plus 1개월','ACTIVE',3000,'/uploads/product-image/naver.png',NULL),
(11,4,'Naver Plus 12개월','ACTIVE',30000,'/uploads/product-image/naver.png',NULL),
(12,3,'Skillshare Monthly','ACTIVE',20600,'/uploads/product-image/skillshare.png',NULL),
(13,3,'LinkedIn Learning','ACTIVE',58900,'/uploads/product-image/linkedin.png',NULL);

SET FOREIGN_KEY_CHECKS = 1;
