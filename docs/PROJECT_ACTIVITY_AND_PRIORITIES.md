# MOA 백엔드 작업 이력 및 고도화 우선순위

문서 기준일: 2026-05-19

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
| P0 | 민감 정보 이력 정리 | 이미 추적된 로컬 설정 파일은 워킹트리에서 계속 변경으로 보일 수 있다. | `git rm --cached src/main/resources/application-local.properties` 적용 후 안전하게 추적 해제 |
| P0 | GitHub Secrets 점검 | DB/JWT/PASS/배포 키가 Actions secret으로 완전히 이전되어야 한다. | 운영 secret 목록과 workflow 참조 키를 대조 |
| P1 | 배포 health check 표준화 | 현재 smoke check는 `/actuator/health` 또는 `/` fallback 구조다. | actuator 노출 정책 확정 후 health endpoint 고정 |
| P1 | 테스트 프로파일 정리 | 로컬/CI/운영 설정 경계가 명확해야 재현 가능한 테스트가 가능하다. | `application-test.properties`와 CI DB/mock 전략 확정 |
| P2 | 배포 rollback/runbook 문서화 | systemd 배포 실패 시 복구 절차가 필요하다. | artifact 보관, 이전 WAR 복구, journal 확인 절차 문서화 |

## Git 활동 요약

마지막 자동화 기준: 2026-05-17T16:29:20Z

- 2026-05-19: backend CI/CD workflow에 build/test gate, artifact 전달, main-only deploy, smoke check를 추가했다.
- 2026-05-19: 로컬 설정 example 파일을 추가하고 실제 local properties ignore 정책을 보강했다.
- 2026-05-19: README와 작업 이력 문서를 UTF-8 한국어로 복구했다.
- 2026-05-18: 민감 설정 파일 커밋 위험과 ignore 정책 필요성을 점검했다.
- 2026-05-17: GitHub Actions 배포 workflow, systemd 실행 전제, Maven wrapper 실행 조건을 점검했다.

## 날짜별 작업 이력

### 2026-05-19

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
