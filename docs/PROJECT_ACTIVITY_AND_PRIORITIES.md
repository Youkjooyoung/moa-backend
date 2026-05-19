# MOA 백엔드 작업 이력 및 고도화 우선순위

문서 기준일: 2026-05-19

## 2026-05-19 자동화 재점검 결과

- Git 상태: `dev` 브랜치가 `origin/dev`와 동기화되어 있고 워킹트리는 clean 상태다.
- 새 코드 이슈: 이번 점검에서 백엔드 소스의 신규 미커밋 변경은 발견하지 못했다.
- 민감 파일 상태: `application-secret.properties`, `src/main/resources/keystore/`, `MoA_Bank V2 APi Key.txt`는 Git 추적 대상이 아니며 `.gitignore` 규칙으로 막혀 있다.
- 검증 제한: Maven `clean verify`는 현재 네트워크 제한으로 Spring Boot parent POM을 받을 수 없어 로컬 실행이 막혔다.
- 남은 보안 리스크: 민감 파일이 로컬 디스크에는 남아 있으므로 운영 secret의 GitHub Secrets 이관 여부와 키 회전 여부를 별도로 확정해야 한다.

## 완료한 고도화 작업

1. 백엔드 로컬 설정 분리
   - `application-local.properties.example`을 추가해 필요한 로컬 설정 키를 문서화했다.
   - 실제 `application-local.properties`는 `.gitignore`에 추가해 민감 정보가 커밋되지 않도록 했다.

2. CI/CD 검증 강화
   - GitHub Actions에서 Maven `clean verify`를 먼저 수행하도록 배포 workflow를 분리했다.
   - artifact 업로드/다운로드 단계를 추가해 검증 산출물과 배포 산출물을 명확히 연결했다.
   - `main` push에서만 배포가 실행되도록 제한하고, `dev`와 PR에서는 검증만 수행하도록 했다.

3. Storybook 정리 연계
   - 프론트 Storybook/CI 검증이 백엔드 배포와 분리되어도 함께 release gate 역할을 하도록 문서화했다.

4. README 및 문서 인코딩 복구
   - README를 UTF-8 한국어 문서로 재작성했다.
   - 로컬 설정, CI/CD, 배포 전제, 문서 링크를 정리했다.

5. UI 회귀 테스트 연계
   - 프론트 단위 테스트가 백엔드 변경과 함께 검증될 수 있도록 CI 문서 기준을 정리했다.

## 남은 고도화 우선순위

| 우선순위 | 작업 | 이유 | 다음 액션 |
| --- | --- | --- | --- |
| P0 | GitHub Secrets 및 키 회전 점검 | DB/JWT/PASS/은행 API/배포 키가 Actions secret과 서버 환경변수로 완전히 이전되어야 한다. | 운영 secret 목록, workflow 참조 키, 서버 환경변수를 대조하고 필요한 키를 회전 |
| P0 | 로컬 민감 파일 보관 정책 확정 | 파일은 Git에서 제외되어 있지만 로컬 디스크에 남아 있어 백업/공유 과정에서 노출될 수 있다. | `application-secret.properties`, keystore, 은행 API 키 파일의 보관 위치와 삭제/암호화 정책 확정 |
| P1 | Maven 의존성 캐시/미러 정리 | 네트워크 제한 환경에서는 parent POM을 받을 수 없어 자동화 검증이 재현되지 않는다. | 사내/로컬 Maven mirror 또는 검증용 dependency cache 정책 확정 |
| P1 | 배포 health check 표준화 | 현재 smoke check는 `/actuator/health` 또는 `/` fallback 구조다. | actuator 노출 정책 확정 후 health endpoint 고정 |
| P1 | 테스트 프로파일 정리 | 로컬/CI/운영 설정 경계가 명확해야 재현 가능한 테스트가 가능하다. | `application-test.properties`와 CI DB/mock 전략 확정 |
| P2 | 배포 rollback/runbook 문서화 | systemd 배포 실패 시 복구 절차가 필요하다. | artifact 보관, 이전 WAR 복구, journal 확인 절차 문서화 |

## Git 활동 요약

이번 재점검 기준: 2026-05-19T09:36:33+09:00

- 2026-05-19: `90e4717`에서 backend CI/CD gate, artifact 전달, main-only deploy, local config ignore 정책을 강화했다.
- 2026-05-18: `bae5b74`에서 백엔드 우선순위 문서와 민감 계정 파일 제거 작업을 정리했다.
- 2026-05-19 재점검: 이후 신규 커밋은 없고 `dev`와 `origin/dev`는 동기화 상태다.

- 2026-05-19: backend CI/CD workflow에 build/test gate, artifact 전달, main-only deploy, smoke check를 추가했다.
- 2026-05-19: 로컬 설정 example 파일을 추가하고 실제 local properties ignore 정책을 보강했다.
- 2026-05-19: README와 작업 이력 문서를 UTF-8 한국어로 복구했다.
- 2026-05-18: 민감 설정 파일 커밋 위험과 ignore 정책 필요성을 점검했다.
- 2026-05-17: GitHub Actions 배포 workflow, systemd 실행 전제, Maven wrapper 실행 조건을 점검했다.

## 날짜별 작업 이력

### 2026-05-19

- 자동화 재점검에서 Git 상태, ignore 정책, 로컬 민감 파일 추적 여부를 확인했다.
- `application-secret.properties`, keystore, 은행 API 키 파일이 Git 추적 대상이 아니며 ignore 처리되어 있음을 확인했다.
- Maven 검증은 네트워크 제한으로 Spring Boot parent POM 다운로드가 막혀 완료하지 못했다.
- 남은 고도화 우선순위를 로컬 설정 추적 해제에서 secret 운영/키 회전 점검으로 갱신했다.
- `application-local.properties.example`을 추가했다.
- 실제 `application-local.properties`를 ignore 대상으로 추가했다.
- GitHub Actions backend workflow를 검증 job과 배포 job으로 분리했다.
- `./mvnw -B clean verify`를 CI gate로 추가했다.
- 배포 후 smoke check를 추가했다.
- README와 작업 이력 문서를 UTF-8 한국어로 복구했다.

### 2026-05-18

- 백엔드 로컬 민감 설정 추적 위험을 점검했다.
- 배포/보안/문서화 관점의 고도화 우선순위를 정리했다.

### 2026-05-17

- GitHub Actions deploy workflow와 systemd 배포 전제 조건을 점검했다.
- Maven wrapper 실행 조건을 정리했다.
- PASS 연동 환경 분리와 문서화 필요 사항을 정리했다.

### 2026-05-14

- IDE 실행 환경과 README/Notion 연결 문서를 정리했다.
- WAR packaging과 GitHub Actions 배포 workflow를 보강했다.
- 백엔드 민감 정보 관리와 ignore 정책을 점검했다.

### 2026-05-11

- 백엔드 배포 smoke test를 추가하고 실행 흐름을 점검했다.

### 2026-05-10

- mock 인증 흐름과 로컬 테스트 전제를 정리했다.

### 2026-05-05

- 로그인 intent, 계정 등록/검증, 계정 설정 흐름의 백엔드 연동 전제를 점검했다.
