![MOA 프로젝트 로고](./moa_logo.png)

# MOA (모아) - OTT 구독 공유 플랫폼 (Backend)

👉 **[📖 MOA 프로젝트 상세 노션 기획서 및 포트폴리오 보기](https://www.notion.so/MOA-35fabc1c2725808eb034d06eff0132fe?source=copy_link)**

## 1. 프로젝트 선정 이유

최근 여러 OTT 서비스를 동시에 구독하는 사용자가 늘어나면서 **구독료 부담**이 크게 증가하고 있습니다. 이를 해결하기 위해 계정을 공유하는 커뮤니티가 활성화되었으나, 신뢰 문제와 수동 정산의 번거로움이 존재했습니다.

| 기존 방식의 문제점 | MOA(모아)의 해결책 | 기대 효과 |
| :--- | :--- | :--- |
| **신뢰 부족** (파티장 잠적, 파티원 미납) | **보증금 시스템 도입** (가입 시 예치, 위약 시 패널티) | 안전하고 신뢰할 수 있는 공유 환경 조성 |
| **수동 정산의 번거로움** (매월 입금 확인) | **자동 결제 및 정산 아키텍처** (빌링키 기반) | 사용자의 개입 없이 매월 자동으로 요금 처리 |

MOA 프로젝트는 이러한 시스템적 해결책을 통해 사용자들이 안전하고 편리하게 OTT 구독을 공유할 수 있는 중개 플랫폼을 구축하고자 선정되었습니다.

---

## 2. 요구사항 정의서

본 프로젝트의 핵심 비즈니스 및 기술 요구사항은 다음과 같습니다.

| 구분 | 요구사항 명 | 상세 내용 |
| :--- | :--- | :--- |
| **비즈니스** | 파티 매칭 및 보증금 | 파티장/파티원은 가입 시 보증금을 결제하며, 규칙 위반 시 보증금을 차감함. |
| **비즈니스** | 자동 월회비 결제 | 등록된 카드를 통해 파티원에게 매월 지정된 날짜에 월회비가 자동 청구됨. |
| **비즈니스** | 자동 정산 처리 | 파티장에게 매월 수수료를 제외한 금액이 자동으로 정산됨. |
| **금융/결제** | PG사 연동 및 빌링키 | 토스 페이먼츠 API를 연동하여 카드 빌링키 발급 및 스케줄러 기반 결제 처리. |
| **보안/인증** | 1원 계좌 인증 | 오픈뱅킹 API를 연동해 정산 계좌의 실소유주를 검증함. |
| **보안/인증** | 하이브리드 인증 시스템 | JWT(Stateless) 기반 API 인증 및 OAuth 2.0, 2FA(Google OTP) 다중 인증 지원. |

---

## 3. 분석 (기술 스택)

요구사항을 충족하기 위해 최적의 성능과 확장성을 고려하여 기술 스택을 선정했습니다.

| 분류 | 기술 스택 (Tech Stack) | 선정 이유 |
| :--- | :--- | :--- |
| **Backend** | ![Java](https://img.shields.io/badge/Java-17-007396?style=flat-square&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.8-6DB33F?style=flat-square&logo=springboot&logoColor=white) | 엔터프라이즈 환경에서의 안정성 및 방대한 생태계 활용 |
| **DB / ORM** | ![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat-square&logo=mysql&logoColor=white) ![MyBatis](https://img.shields.io/badge/MyBatis-3.0.3-black?style=flat-square) | 복잡한 금융/정산 통계 쿼리의 세밀한 튜닝 및 제어 용이 |
| **Frontend** | ![React](https://img.shields.io/badge/React-19-61DAFB?style=flat-square&logo=react&logoColor=black) ![Vite](https://img.shields.io/badge/Vite-646CFF?style=flat-square&logo=vite&logoColor=white) ![TailwindCSS](https://img.shields.io/badge/Tailwind-38B2AC?style=flat-square&logo=tailwind-css&logoColor=white) | 빠른 렌더링 속도와 컴포넌트 기반 UI 개발의 효율성 극대화 |
| **State** | ![Zustand](https://img.shields.io/badge/Zustand-764ABC?style=flat-square) | Redux 대비 보일러플레이트가 적고 직관적인 전역 상태 관리 |
| **External API** | ![Toss Payments](https://img.shields.io/badge/Toss_Payments-3182F6?style=flat-square&logo=toss&logoColor=white) ![Google OTP](https://img.shields.io/badge/Google_Authenticator-4285F4?style=flat-square&logo=google-authenticator&logoColor=white) | 압도적인 개발자 경험(DX)을 제공하는 토스 페이먼츠 및 보안 강화 |
| **Infra & Deploy** | ![MobaXterm](https://img.shields.io/badge/MobaXterm-0040FF?style=flat-square) ![AWS CloudFront](https://img.shields.io/badge/AWS_CloudFront-232F3E?style=flat-square&logo=amazon-aws&logoColor=white) ![AWS S3](https://img.shields.io/badge/AWS_S3-569A31?style=flat-square&logo=amazon-s3&logoColor=white) | MobaXterm을 통한 운영 백엔드 배포 및 S3(이미지 스토리지)와 연동된 CloudFront를 통한 프론트엔드 최적화 배포 |
| **IDE & AI** | ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=flat-square&logo=intellij-idea&logoColor=white) ![VS Code](https://img.shields.io/badge/VS_Code-007ACC?style=flat-square&logo=visual-studio-code&logoColor=white) ![Antigravity](https://img.shields.io/badge/Antigravity_AI-FF6F00?style=flat-square) | 개발 환경 구축 및 AI 페어 프로그래밍(Antigravity)을 통한 개발 생산성 및 보안/아키텍처 고도화 |

---

## 4. 설계

### 4.1 시스템 및 데이터베이스 설계
시스템은 총 27개의 테이블로 고도로 정규화되어 있으며, 각 도메인별 관심사가 철저히 분리되어 있습니다.

| 도메인 | 주요 테이블 | 설계 의도 및 역할 |
| :--- | :--- | :--- |
| **유저 (User)** | `USERS`, `ACCOUNT`, `USER_CARD` | 유저 기본 정보와 정산용 계좌, 자동결제용 카드를 분리하여 보안성 강화 |
| **파티 (Party)** | `PARTY`, `PARTY_MEMBER` | 파티장과 파티원 간의 N:M 관계를 해소하고 멤버별 상태(위약, 정상) 관리 |
| **결제 (Finance)**| `PAYMENT`, `DEPOSIT`, `SETTLEMENT` | 월회비, 보증금, 정산금의 트랜잭션을 철저히 분리하여 회계 무결성 확보 |

### 4.2 스케줄러 (자동화 배치) 설계
수동 개입을 최소화하기 위해 8개의 핵심 스케줄러를 설계하여 운영합니다.

| 스케줄러 명 | 실행 주기 | 주요 역할 |
| :--- | :--- | :--- |
| **SettlementScheduler** | 매월 1일 04:00 | 활성화된 파티의 정산 금액을 계산하여 월간 정산 데이터 생성 |
| **PaymentScheduler** | 파티 지정일 | 멤버별로 등록된 빌링키를 호출하여 월회비 자동 결제 수행 |
| **PaymentTimeoutScheduler** | 매 30분 | 잔액 부족 등으로 실패한 결제를 찾아 **최대 4회 점진적 재시도** 수행 |
| **PartyCloseScheduler** | 매일 자정 | 약정 기간이 종료된 파티를 자동 해산하고 보증금을 환불 처리 |

---

## 5. 구현

설계를 바탕으로 다음과 같은 핵심 아키텍처와 로직을 구현했습니다.

### 5.1 자동 결제 및 재시도(Retry) 메커니즘
토스 페이먼츠 API를 활용하여 카드 빌링키 발급 및 결제를 구현했습니다. 특히, 잔액 부족이나 외부 API 통신 장애로 결제가 실패할 경우를 대비하여 `PAYMENT_RETRY_HISTORY` 이력 테이블을 구현하고, 최대 4회까지 재시도하는 **견고한(Robust) 결제 파이프라인**을 완성했습니다.

### 5.2 보안 및 1원 인증
Access Token(1시간)과 Refresh Token(7일)을 사용하는 JWT 인증 체계를 도입했습니다. XSS 방어를 위해 토큰은 HttpOnly 쿠키로 관리하며, 파티장 정산의 정확성을 위해 금융결제원 오픈뱅킹 API를 연동해 **'1원 송금 및 인증번호 검증' 모듈**을 구현하여 계좌 실소유주를 완벽히 검증합니다.

---

## 6. 프로젝트 보고서 (성과 및 트러블슈팅)

### 6.1 주요 트러블슈팅: 대량 결제 트랜잭션의 롤백 방어
- **문제 인식**: 스케줄러가 여러 건의 자동 결제를 한 번의 트랜잭션으로 처리할 때, 단 하나의 외부 API(토스) 타임아웃만 발생해도 **정상 결제된 다른 건들까지 모두 롤백(Rollback)**되는 치명적인 문제가 발생했습니다.
- **문제 해결**: `@Transactional(propagation = Propagation.REQUIRES_NEW)` 옵션을 활용해 각 사용자의 결제 건마다 **독립적인 트랜잭션(격리)**을 부여했습니다. 이를 통해 일부 결제가 실패하더라도 나머지 정상 결제 건은 안전하게 DB에 커밋되도록 아키텍처를 개선했습니다.

### 6.2 향후 발전 방향
- 현재 구현된 1원 인증 기술을 기반으로, 추후 오픈뱅킹 API의 '자동 펌뱅킹(이체)' 기능을 연동하여 파티장에게 정산금을 입금해 주는 과정을 완전히 시스템화할 계획입니다.
- AI 챗봇(RAG)을 도입하여 자주 묻는 질문(FAQ) 및 파티 규칙 안내에 대한 CS 대응을 자동화할 예정입니다.
