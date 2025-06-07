package team.flex.training.corehr.assignment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import team.flex.training.corehr.support.fake.assignment.FakeEmployeeDepartmentAssignmentRepository
import team.flex.training.corehr.support.fake.assignment.FakeEmployeeJobAssignmentRepository
import team.flex.training.corehr.support.fake.company.FakeCompanyLookUpService
import team.flex.training.corehr.support.fake.company.department.FakeDepartmentLookUpService
import team.flex.training.corehr.support.fake.company.job.FakeJobRoleLookUpService
import team.flex.training.corehr.support.fake.employee.FakeEmployeeLookUpService
import java.time.LocalDate

class AssignmentQueryServiceImplTest {
    private val companyLookUpService = FakeCompanyLookUpService()
    private val employeeLookUpService = FakeEmployeeLookUpService()
    private val departmentLookUpService = FakeDepartmentLookUpService()
    private val jobRoleLookUpService = FakeJobRoleLookUpService()
    private val employeeDepartmentAssignmentRepository = FakeEmployeeDepartmentAssignmentRepository()
    private val employeeJobAssignmentRepository = FakeEmployeeJobAssignmentRepository()

    private val service: AssignmentQueryService = AssignmentQueryServiceImpl(
        companyLookUpService,
        employeeLookUpService,
        departmentLookUpService,
        jobRoleLookUpService,
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
            val department = departmentLookUpService.fakeDepartmentRepository.`부서 생성`(company)
            employeeDepartmentAssignmentRepository.`부서 발령 생성`(employee, department)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = service.getAssignment(company.companyId, employee.employeeId, targetDate)

            // then
            assertThat(actual.departmentAssignment).isNotNull
                .extracting(
                    "startDate",
                    "endDate",
                    "departmentId",
                    "departmentName",
                ).containsExactly(
                    LocalDate.parse("2025-01-01"),
                    LocalDate.parse("2025-01-31"),
                    1L,
                    "부서 1",
                )
        }

        @ParameterizedTest
        @ValueSource(strings = ["2024-12-31", "2025-02-01"])
        fun `부서 발령 정보가 없다면 부서 발령 정보는 null 로 반환 한다`(targetDateString: String) {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            val department = departmentLookUpService.fakeDepartmentRepository.`부서 생성`(company)
            employeeDepartmentAssignmentRepository.`부서 발령 생성`(employee, department)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = service.getAssignment(company.companyId, employee.employeeId, targetDate)

            // then
            assertThat(actual.departmentAssignment).isNull()
        }

        @ParameterizedTest
        @ValueSource(strings = ["2025-01-01", "2025-01-31"])
        fun `직무 발령 정보가 있다면 직무 발령 정보를 포함하여 반환 한다`(targetDateString: String) {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            val jobRole = jobRoleLookUpService.fakeJobRoleRepository.`직무 생성`(company)
            employeeJobAssignmentRepository.`직무 발령 생성`(employee, jobRole)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = service.getAssignment(company.companyId, employee.employeeId, targetDate)

            // then
            assertThat(actual.jobRoleAssignment).isNotNull
                .extracting(
                    "startDate",
                    "endDate",
                    "jobRoleId",
                    "jobRoleName",
                ).containsExactly(
                    LocalDate.parse("2025-01-01"),
                    LocalDate.parse("2025-01-31"),
                    1L,
                    "직무 1",
                )
        }

        @ParameterizedTest
        @ValueSource(strings = ["2024-12-31", "2025-02-01"])
        fun `직무 발령 정보가 없다면 직무 발령 정보는 null 로 반환 한다`(targetDateString: String) {
            // given
            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            val jobRole = jobRoleLookUpService.fakeJobRoleRepository.`직무 생성`(company)
            employeeJobAssignmentRepository.`직무 발령 생성`(employee, jobRole)
            val targetDate = LocalDate.parse(targetDateString)

            // when
            val actual = service.getAssignment(company.companyId, employee.employeeId, targetDate)

            // then
            assertThat(actual.jobRoleAssignment).isNull()
        }
    }
}