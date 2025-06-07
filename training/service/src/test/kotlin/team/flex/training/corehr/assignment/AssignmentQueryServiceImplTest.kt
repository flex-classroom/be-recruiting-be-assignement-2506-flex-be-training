package team.flex.training.corehr.assignment

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import team.flex.training.corehr.support.fake.assignment.FakeEmployeeDepartmentAssignmentRepository
import team.flex.training.corehr.support.fake.assignment.FakeEmployeeJobAssignmentRepository
import team.flex.training.corehr.support.fake.company.FakeCompanyLookUpService
import team.flex.training.corehr.support.fake.employee.FakeEmployeeLookUpService
import team.flex.training.corehr.support.fixture.assignment.EmployeeJobAssignmentEntityFixture
import java.time.LocalDate

class AssignmentQueryServiceImplTest {
    private val companyLookUpService = FakeCompanyLookUpService()
    private val employeeLookUpService = FakeEmployeeLookUpService()
    private val employeeDepartmentAssignmentRepository = FakeEmployeeDepartmentAssignmentRepository()
    private val employeeJobAssignmentRepository = FakeEmployeeJobAssignmentRepository()

    private val service: AssignmentQueryService = AssignmentQueryServiceImpl(
        companyLookUpService,
        employeeLookUpService,
        employeeDepartmentAssignmentRepository,
        employeeJobAssignmentRepository,
    )

    @Nested
    inner class `요청일에 해당하는 구성원 인사 정보를 조회할 때` {

        @ParameterizedTest
        @ValueSource(strings = ["2025-01-01", "2025-01-31"])
        fun `부서 발령 정보가 있다면 부서 발령 정보를 포함하여 반환 한다`(targetDateString: String) {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            employeeDepartmentAssignmentRepository.`부서 발령 생성`(employee)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = service.getAssignment(company.companyId, employee.employeeId, targetDate)

            // then
            assertThat(actual.assignment).isNotNull
                .extracting(
                    "startDate",
                    "endDate",
                    "departmentId",
                    "departmentName",
                    "jobRoleId",
                    "jobRoleName",
                ).containsExactly(
                    LocalDate.parse("2025-01-01"),
                    LocalDate.parse("2025-01-31"),
                    0L,
                    "부서",
                    null,
                    null,
                )
        }

        @ParameterizedTest
        @ValueSource(strings = ["2024-12-31", "2025-02-01"])
        fun `부서 발령 정보가 없다면 부서 발령 정보는 null 로 반환 한다`(targetDateString: String) {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            employeeDepartmentAssignmentRepository.`부서 발령 생성`(employee)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = service.getAssignment(company.companyId, employee.employeeId, targetDate)

            // then
            assertThat(actual.assignment)
                .extracting("departmentId", "departmentName")
                .containsExactly(null, null)
        }

        @ParameterizedTest
        @ValueSource(strings = ["2025-01-01", "2025-01-31"])
        fun `직무 발령 정보가 있다면 직무 발령 정보를 포함하여 반환 한다`(targetDateString: String) {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            employeeJobAssignmentRepository.`직무 발령 생성`(employee)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = service.getAssignment(company.companyId, employee.employeeId, targetDate)

            // then
            assertThat(actual.assignment).isNotNull
                .extracting(
                    "startDate",
                    "endDate",
                    "departmentId",
                    "departmentName",
                    "jobRoleId",
                    "jobRoleName",
                ).containsExactly(
                    LocalDate.parse("2025-01-01"),
                    LocalDate.parse("2025-01-31"),
                    null,
                    null,
                    0L,
                    "직무",
                )
        }

        @ParameterizedTest
        @ValueSource(strings = ["2024-12-31", "2025-02-01"])
        fun `직무 발령 정보가 없다면 직무 발령 정보는 null 로 반환 한다`(targetDateString: String) {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            employeeJobAssignmentRepository.`직무 발령 생성`(employee)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = service.getAssignment(company.companyId, employee.employeeId, targetDate)

            // then
            assertThat(actual.assignment)
                .extracting("jobRoleId", "jobRoleName")
                .containsExactly(null, null)
        }
    }

    @Nested
    inner class `구성원의 모든 인사 발령 정보를 조회할 때` {

        @Test
        fun `부서 발령 정보가 있다면 포함 한다`() {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            employeeDepartmentAssignmentRepository.`부서 발령 생성`(employee)

            // when
            val actual = service.getAssignments(company.companyId, employee.employeeId)

            // then
            assertThat(actual.assignments).hasSize(1)
                .extracting(
                    "startDate",
                    "endDate",
                    "departmentId",
                    "departmentName",
                    "jobRoleId",
                    "jobRoleName",
                ).containsExactly(
                    Tuple.tuple(
                        LocalDate.parse("2025-01-01"),
                        LocalDate.parse("2025-01-31"),
                        0L,
                        "부서",
                        null,
                        null,
                    ),
                )
        }

        @Test
        fun `직무 발령 정보가 있다면 포함 한다`() {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            employeeJobAssignmentRepository.`직무 발령 생성`(employee)

            // when
            val actual = service.getAssignments(company.companyId, employee.employeeId)

            // then
            assertThat(actual.assignments).hasSize(1)
                .extracting(
                    "startDate",
                    "endDate",
                    "departmentId",
                    "departmentName",
                    "jobRoleId",
                    "jobRoleName",
                ).containsExactly(
                    Tuple.tuple(
                        LocalDate.parse("2025-01-01"),
                        LocalDate.parse("2025-01-31"),
                        null,
                        null,
                        0L,
                        "직무",
                    ),
                )
        }

        @Test
        fun `부서와 직무 발령 모두 있다면 모두 포함 한다`() {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            employeeDepartmentAssignmentRepository.`부서 발령 생성`(employee)
            employeeJobAssignmentRepository.`직무 발령 생성`(employee)

            // when
            val actual = service.getAssignments(company.companyId, employee.employeeId)

            // then
            assertThat(actual.assignments).hasSize(2)
                .extracting(
                    "startDate",
                    "endDate",
                    "departmentId",
                    "departmentName",
                    "jobRoleId",
                    "jobRoleName",
                ).containsExactly(
                    Tuple.tuple(
                        LocalDate.parse("2025-01-01"),
                        LocalDate.parse("2025-01-31"),
                        0L,
                        "부서",
                        null,
                        null,
                    ),
                    Tuple.tuple(
                        LocalDate.parse("2025-01-01"),
                        LocalDate.parse("2025-01-31"),
                        null,
                        null,
                        0L,
                        "직무",
                    ),
                )
        }

        @Test
        fun `부서와 직무 발령 모두 없다면 빈 List 를 한다`() {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)

            // when
            val actual = service.getAssignments(company.companyId, employee.employeeId)

            // then
            assertThat(actual.assignments).isEmpty()
        }

        @Test
        fun `모든 발령 정보는 시작일 기준 내림차순 정렬 된다`() {
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            employeeDepartmentAssignmentRepository.`부서 발령 생성`(employee)
            employeeJobAssignmentRepository.`직무 발령 생성`(
                employee,
                jobAssignment = EmployeeJobAssignmentEntityFixture.`날짜 지정`(
                    LocalDate.parse("2025-01-02"),
                    LocalDate.parse("2025-01-31"),
                ),
            )

            // when
            val actual = service.getAssignments(company.companyId, employee.employeeId)

            // then
            assertThat(actual.assignments).hasSize(2)
                .extracting(
                    "startDate",
                    "endDate",
                    "departmentId",
                    "departmentName",
                    "jobRoleId",
                    "jobRoleName",
                ).containsExactly(
                    Tuple.tuple(
                        LocalDate.parse("2025-01-02"),
                        LocalDate.parse("2025-01-31"),
                        null,
                        null,
                        0L,
                        "직무",
                    ),
                    Tuple.tuple(
                        LocalDate.parse("2025-01-01"),
                        LocalDate.parse("2025-01-31"),
                        0L,
                        "부서",
                        null,
                        null,
                    ),
                )
        }

        @Test
        fun `발령 정보 중 시작일이 같은 발령이 있다면 종료일 기준 오름차순 정렬 한다`() {
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            employeeDepartmentAssignmentRepository.`부서 발령 생성`(employee)
            employeeJobAssignmentRepository.`직무 발령 생성`(
                employee,
                jobAssignment = EmployeeJobAssignmentEntityFixture.`날짜 지정`(
                    LocalDate.parse("2025-01-01"),
                    LocalDate.parse("2025-02-01"),
                ),
            )

            // when
            val actual = service.getAssignments(company.companyId, employee.employeeId)

            // then
            assertThat(actual.assignments).hasSize(2)
                .extracting(
                    "startDate",
                    "endDate",
                    "departmentId",
                    "departmentName",
                    "jobRoleId",
                    "jobRoleName",
                ).containsExactly(
                    Tuple.tuple(
                        LocalDate.parse("2025-01-01"),
                        LocalDate.parse("2025-02-01"),
                        null,
                        null,
                        0L,
                        "직무",
                    ),
                    Tuple.tuple(
                        LocalDate.parse("2025-01-01"),
                        LocalDate.parse("2025-01-31"),
                        0L,
                        "부서",
                        null,
                        null,
                    ),
                )
        }
    }
}
