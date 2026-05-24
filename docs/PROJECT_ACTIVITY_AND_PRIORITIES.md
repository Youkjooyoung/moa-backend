# MOA 백엔드 작업 이력 및 고도화 우선순위

문서 기준일: 2026-05-23

## 2026-05-23 자동화 점검 결과

- 적용 지침: `senior-enhancement-lead`, `fullstack-product-ops`를 확인했고, 저장소 내 `AGENTS.md` 지침을 우선 적용했다.
- 생성된 Skill/Agent 검색: 저장소 내부의 별도 `SKILL.md`, `.agents`, `.codex` 작업 Skill은 발견되지 않았다. 현재 작업에는 기존 Skill 조합으로 충분해 새 Skill은 생성하지 않았다.
- Git 상태: `moa-backend`는 독립 Git 저장소이며 현재 브랜치는 `dev`, 원격은 `origin/dev`와 연결되어 있다.
- Git 활동: 마지막 자동화 실행 이후 새 원격 커밋은 확인되지 않았다. 최신 커밋은 `90e4717 Harden backend CI and local config handling`이다.
- AI 도구 흔적 정리: `.cursorrules`는 삭제 상태이며, Codex 기준 지침은 `AGENTS.md`로 정리되어 있다. Claude/Gemini/Anthropic 문자열 검색 결과는 추가 발견되지 않았다.
- 민감 파일 점검: `MoA_Bank V2 APi Key.txt`, `application-local.properties`, `application-secret.properties`, keystore, logs, target 산출물은 ignore 상태이며 이번 커밋 대상에서 제외한다.
- 새 이슈: 백엔드 ignore 목록에 로컬 로그와 `target` 산출물이 대량 존재한다. 커밋 대상은 아니지만 공유/백업 폴더 정책을 분리해야 한다.
- 새 이슈: `src/main/resources/static`과 `src/main/resources/assets`에 프론트 빌드 산출물 경계가 남아 있다. S3/CloudFront 배포와 WAR 정적 리소스 역할을 확정해야 한다.
- 새 이슈: `JwtSecretGenerator`, `UserDummyGenerator` 같은 운영 소스 트리 유틸은 실행 경로와 사용 목적을 분리해야 한다.
- 검증 결과: `.\mvnw.cmd -B clean verify`는 Maven Central 접근 제한으로 Spring Boot parent POM `3.5.8`을 받을 수 없어 실패했다. 현재 실패 원인은 코드 컴파일 오류가 아니라 네트워크 차단이다.

## 현재 고도화 우선순위

| 우선순위 | 작업 | 이유 | 다음 액션 |
| --- | --- | --- | --- |
| P0 | GitHub Secrets 및 서버 환경변수 전체 점검 | DB/JWT/PASS/결제/배포 키가 Actions secret과 서버 환경변수로 안전하게 이관되어야 한다. | 운영 secret 목록과 workflow 참조값 대조 |
| P0 | 로컬 민감 파일 보관 정책 확정 | Git에서는 제외되어도 로컬/백업 공유 과정에서 노출될 수 있다. | secret, keystore, API 키 파일 위치와 삭제/암호화 기준 확정 |
| P0 | 프론트 빌드 산출물 경계 정리 | 백엔드 WAR에 정적 프론트 산출물이 섞이면 S3/CloudFront 배포물과 불일치할 수 있다. | 백엔드에서 서빙할 정적 파일 범위 결정 후 ignore/build 스크립트 정리 |
| P0 | fixture 민감 문자열 표준화 | 샘플 비밀번호/계정 문자열이 실제 secret처럼 보이면 점검 노이즈와 오해가 생긴다. | test SQL fixture임을 명시하고 더미 값 네이밍 정리 |
| P1 | Maven 의존성 캐시/미러 정리 | 네트워크 제한 환경에서 `clean verify` 재현성이 낮다. | 사내/로컬 Maven mirror 또는 dependency cache 절차 문서화 |
| P1 | health check 표준화 | 배포 smoke check 기준이 명확해야 rollback 판단이 가능하다. | actuator 노출 정책과 고정 health endpoint 결정 |
| P1 | 테스트 프로파일 정리 | 로컬/CI/운영 설정 경계가 명확해야 테스트가 안정화된다. | `application-test.properties`와 mock DB 전략 확정 |
| P2 | 배포 rollback/runbook 문서화 | systemd 배포 실패 시 복구 절차가 필요하다. | artifact 보관, 이전 WAR 복구, journal 확인 절차 작성 |
| P2 | 개발 유틸 실행 경로 분리 | secret generator와 dummy generator가 운영 코드처럼 보인다. | `src/test`, 별도 CLI, 또는 문서화된 ops script로 이동 검토 |

## Git 활동 요약

- 2026-05-23: 자동화 점검에서 새 원격 커밋 없음, AI 도구 흔적 추가 발견 없음, ignore된 민감/산출 파일 대량 존재, Maven 네트워크 차단을 재확인했다.
- 2026-05-19: `90e4717`에서 backend CI/CD gate, artifact 전달, main-only deploy, local config ignore 정책을 강화했다.
- 2026-05-18: `bae5b74`에서 백엔드 우선순위 문서와 민감 계정 파일 제거 작업을 정리했다.

## 날짜별 작업 이력

### 2026-05-23

- 자동화 메모리와 저장소 지침을 확인했다.
- 적용 가능한 Skill과 저장소 내부 Agent/Skill 파일을 검색했고, 별도 새 Skill 생성은 필요 없음을 확인했다.
- `dev` 브랜치와 `origin/dev` 연결 상태, 최근 커밋 이력을 확인했다.
- 마지막 자동화 실행 이후 새 커밋이 없음을 확인했다.
- `.cursorrules` 삭제 상태와 `AGENTS.md` 추가 상태를 확인했다.
- Claude/Gemini/Anthropic 문자열 검색에서 추가 흔적이 없음을 확인했다.
- 신규 이슈로 로컬 로그/target 산출물 보관 정책, 정적 리소스 배포 경계, 개발 유틸 분리를 재기록했다.
- `.\mvnw.cmd -B clean verify`를 실행했으나 네트워크 제한으로 parent POM 다운로드가 막혀 실패했다.

### 2026-05-22

- 자동화 메모리와 저장소 지침을 확인했다.
- 정적 빌드 산출물 경계, 개발 유틸 분리, fixture 민감 문자열 표준화, Maven 검증 재현성을 기록했다.

### 2026-05-21

- 상위 폴더가 Git 저장소가 아니며 `moa-backend`가 독립 Git 저장소임을 확인했다.
- 신규 이슈로 프론트 빌드 산출물 혼재, 개발 유틸 콘솔 출력, Maven 검증 재현성 문제를 기록했다.

### 2026-05-20

- Codex 자동화 점검에서 기존 AI 도구 전용 규칙 파일을 제거하고 `AGENTS.md` 기준 작업 지침으로 정리했다.
- Maven 검증이 네트워크 제한으로 실패할 수 있음을 기록했다.
- `src/main/resources/static`에 추적 중인 프론트 빌드 산출물이 있음을 확인하고 고도화 우선순위에 반영했다.

### 2026-05-19

- `application-local.properties.example`을 추가했다.
- 실제 `application-local.properties`를 ignore 대상으로 추가했다.
- GitHub Actions backend workflow를 검증 job과 배포 job으로 분리했다.
- `./mvnw -B clean verify`를 CI gate로 추가했다.
- 배포 후 smoke check를 추가했다.
- README와 작업 이력 문서를 UTF-8 한국어 기준으로 복구했다.

### 2026-05-18

- 백엔드 로컬 민감 설정 추적 위험을 점검했다.
- 배포, 보안, 문서화 관점의 고도화 우선순위를 정리했다.

### 2026-05-17

- GitHub Actions deploy workflow와 systemd 배포 전제 조건을 점검했다.
- Maven wrapper 실행 조건을 정리했다.
- PASS 연동 환경 분리와 문서화 필요 사항을 정리했다.

### 2026-05-14

- IDE 경고를 정리하고 README/Notion 연결 문서를 업데이트했다.
- WAR packaging과 GitHub Actions 배포 workflow를 보강했다.
- 백엔드 민감 정보 관리와 ignore 정책을 점검했다.

### 2026-05-11

- 백엔드 이미지 계약과 smoke test를 추가했다.

### 2026-05-10

- mock 은행 계좌 인증 흐름을 추가했다.

### 2026-05-05

- 챗봇 intent, 계정 등록/검증, 운영 인증과 업로드 설정을 개선했다.
