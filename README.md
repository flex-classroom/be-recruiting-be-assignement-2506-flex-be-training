# flex-be-training
---

이 프로젝트는 flex team 에서 사전 과제를 진행하기 위해 사용하는 skeleton 입니다.

---

## 사전 준비

1. [sdkman 설치](https://sdkman.io/install/)를 권장합니다.
2. 실행에는 docker 환경이 필요합니다. gradle check 태스크는 integrationTest를 포함하고 있으며, integrationTest는 testcontainers를 이용하고 있습니다.
   1. [mac](https://docs.docker.com/desktop/setup/install/mac-install/)
   2. [windows](https://docs.docker.com/desktop/setup/install/windows-install/)
   3. [linux(ubuntu)](https://docs.docker.com/desktop/setup/install/linux/ubuntu/)
3. IDE는 JetBrains의 [IntelliJ Community 버전](https://www.jetbrains.com/help/idea/installation-guide.html)을 권장합니다.
4. 과제 제출 전 `./gradlew check` 를 실행하시고 성공함을 확인한 뒤 제출하시는 것을 권장합니다.

## 과제 전형 안내
- 과제는 희망하신 일정부터 48시간 내 Github Classroom을 통해 제출되어야 합니다.
  - 예시) 2025년 3월 7일 00시 ~ 2025년 3월 9일 00시
  - 제출시간 이후 커밋은 불가능합니다. 제출이 되더라도 제출 시간 이후 업데이트된 커밋은 평가 범위에서 제외합니다.
- 과제 전형 진행 중 문의 사항은 join@flex.team 혹은 010-7707-9418 (김진경 매니저)에게 연락을 부탁 드립니다.
- 과제 전형의 결과는 마감일로부터 영업일 5일 내 합격/불합격 여부와 관계없이 메일 혹은 유선 안내를 드리겠습니다. 

## 과제 설명

### 배경
- flex 제품에서 다루고 있는 도메인에서 과제를 선별해보았습니다.
- 고객사(company)와 구성원(employee)의 정보를 관리하는 corehr 제품이 있습니다.
- 고객사는 부서(department)를 관리(생성/수정/삭제)할 수 있고 구성원들은 특정 부서에 소속 됩니다.
  - 부서: 개발팀 / 기획팀 / 디자인팀 / ...
- 마찬가지로 고객사는 직무(job role)를 관리(생성/수정/삭제)할 수 있고 구성원들에게 직무를 부여할 수 있습니다.
  - 직무: Product Engineer / Product Designer / Product Manager / ...
- 구성원의 인사 정보 범주는 매우 넓지만 이번 과제에서는 직무와 부서 정보만 한정하여 진행하겠습니다.

### 구현 내용
- 공통
  - [EmployeeController](training/api/src/main/kotlin/team/flex/training/corehr/employee/EmployeeApiController.kt)에 미리 작성해놓은 아래 세 개의 API를 구현하는 것이 이번 과제의 범위입니다.
  - 아래 API 설명에 자세히 설명하였지만, 명확한 이해를 돕기 위해 Request / Response 객체는 미리 정의해두었습니다.
    - **Request, Response, URI, Method는 절대 변경하지 마세요. 과제 결과에 영향을 주게 됩니다.**
  - 인증 관련 또는 보안적인 처리에 대한 요구사항은 없습니다.
  - 모듈 구조에 대한 이해를 돕기 위해 [training/README.md](training/README.md)를 작성하였으니 참고하세요.
    - 현재 구성해둔 모듈 구조에 대한 이해를 기반으로 간단한 비즈니스 요구사항 구현을 검증하기 위한 목적이니 모듈 구조 변경은 지양해주세요.
    - 이후 테크 인터뷰 전형을 진행하게 되면 과제 skeleton에 대한 이해도를 검증하는 시간이 포함될 수 있습니다.
- API 설명
  1. 구성원의 인사 정보 생성 (직무 발령 / 부서 발령) API
    - 시작일 ~ 종료일 기간을 갖는 구성원의 인사 정보를 추가하는 API입니다.
    - 구현의 복잡도를 낮추기 위해 아래와 같은 제약 조건을 추가합니다.
      - 대량 변경은 고려하지 않고 구성원 1명에 대한 변경으로 한정합니다.
      - 직무와 부서는 각각 최대 1개까지만 가질 수 있는 것으로 정하도록 하겠습니다.
        - 직무 또는 부서가 모두 없을 수도 있습니다.
      - 취소에 대한 동작은 고려하지 않습니다. 인사 담당자는 실수가 없다고 가정합니다.
      - 예시를 통해 유효성에 대해 부연 설명합니다.
        - 데이터
          - 구성원A / 2024-01-01 ~ 2024-05-31 / 서버 개발1팀, Product Engineer 
          - 구성원A / 2024-06-01 ~ 2024-06-30 / 서버 개발1팀, Backend Engineer
          - 구성원A / 2024-07-01 ~ 2024-11-30 / 기획팀, Product Manager
        - 설명
          - 구성원A에 대해 2024-12-01 ~ 2025-03-31 / 서버 개발2팀을 추가하려고 할 때 정상 동작 합니다.
            - 구성원A는 2024-12-01부터는 직무는 없는 상태가 됩니다.
          - 구성원A에 대해 2024-11-29 ~ 2024-12-31 / 전략기획팀을 추가하려고 할 때 에러가 발생합니다.
            - 최대 1개까지만 가질 수 있다고 하였는데, 2024-11-29 ~ 2024-11-30 기간에 2개의 부서를 가진 상태가 됩니다.
          - 구성원A에 대해 2025-01-01 ~ 2025-03-31 / 서버 개발2팀, Backend Engineer을 추가하려고 할 때 정상 동작 합니다.
            - 이로 인해 구성원A는 2024-12-01 ~ 2024-12-31 기간 동안 부서와 직무가 맵핑되어 있지 않은 상태이지만 정상적인 상황입니다.
  2. 요청일 기준의 구성원의 인사 정보를 조회하는 API
    - 인사 담당자는 특정 기준 시점의 구성원의 인사 정보를 확인해야 하는 상황이 종종 발생합니다.
    - 위의 유효성에 대한 부연 설명에서 사용한 데이터를 가지고 예시를 들어봅니다.
      - 2024-02-01로 조회할 경우는 서버 개발1팀, Product Engineer로 결과가 나와야합니다.
      - 2024-06-15로 조회할 경우는 서버 개발1팀, Backend Engineer로 결과가 나와야합니다.
      - 2024-07-01 또는 2024-11-30로 조회하면 기획팀, Product Manager가 결과로 나와야합니다.
  3. 구성원의 인사 정보 이력을 조회하는 API
    - 구성원의 지금까지의 인사 정보 변경 이력을 제공하는 API입니다.
