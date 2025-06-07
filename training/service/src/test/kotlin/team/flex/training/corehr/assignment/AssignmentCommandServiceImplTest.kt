package team.flex.training.corehr.assignment

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import team.flex.training.corehr.assignment.command.EmployeeAssignmentCreateCommand
import team.flex.training.corehr.support.TimeUtils
import team.flex.training.corehr.support.UTCTimeZoneSupport
import team.flex.training.corehr.support.fake.assignment.FakeEmployeeDepartmentAssignmentRepository
import team.flex.training.corehr.support.fake.assignment.FakeEmployeeJobAssignmentRepository
import team.flex.training.corehr.support.fake.company.FakeCompanyLookUpService
import team.flex.training.corehr.support.fake.company.department.FakeDepartmentLookUpService
import team.flex.training.corehr.support.fake.company.job.FakeJobRoleLookUpService
import team.flex.training.corehr.support.fake.employee.FakeEmployeeLookUpService
import java.time.LocalDate

class AssignmentCommandServiceImplTest : UTCTimeZoneSupport {
    private val companyLookUpService = FakeCompanyLookUpService()
    private val employeeLookUpService = FakeEmployeeLookUpService()
    private val departmentLookUpService = FakeDepartmentLookUpService()
    private val jobRoleLookUpService = FakeJobRoleLookUpService()
    private val employeeDepartmentAssignmentRepository = FakeEmployeeDepartmentAssignmentRepository()
    private val employeeJobAssignmentRepository = FakeEmployeeJobAssignmentRepository()

    private val service: AssignmentCommandService = AssignmentCommandServiceImpl(
        TimeUtils.clock(),
        companyLookUpService,
        employeeLookUpService,
        departmentLookUpService,
        jobRoleLookUpService,
        employeeDepartmentAssignmentRepository,
        employeeJobAssignmentRepository,
    )

    @AfterEach
    fun cleanUp() {
        employeeLookUpService.cleanUp()
        companyLookUpService.cleanUp()
        departmentLookUpService.cleanUp()
        jobRoleLookUpService.cleanUp()
        employeeDepartmentAssignmentRepository.cleanUp()
        employeeJobAssignmentRepository.cleanUp()
    }

    @Nested
    inner class `구성원의 새로운 부서를 발령할 때` {
        @ParameterizedTest
        @CsvSource(
            "'2024-12-31', '2025-01-01'",
            "'2025-01-31', '2025-02-01'",
        )
        fun `요구된 기간에 겹치는 부서 발령이 있다면 예외를 던진다`(startDateString: String, endDateString: String) {
            // given
            val startDate = LocalDate.parse(startDateString)
            val endDate = LocalDate.parse(endDateString)

            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            val department = departmentLookUpService.fakeDepartmentRepository.`부서 생성`(company)
            employeeDepartmentAssignmentRepository.`부서 발령 생성`(employee, department)

            // when then
            assertThatThrownBy {
                service.appointJob(
                    EmployeeAssignmentCreateCommand(
                        company.companyId,
                        employee.employeeId,
                        startDate,
                        endDate,
                        null,
                        department.departmentId,
                    ),
                )
            }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("이미 해당 기간에 발령된 부서가 있습니다.")
        }

        @ParameterizedTest
        @CsvSource(
            "'2024-12-30', '2024-12-31'",
            "'2025-02-01', '2025-02-02'",
        )
        fun `요구된 기간에 겹치는 부서 발령이 없다면 새로운 부서를 발령 한다`(startDateString: String, endDateString: String) {
            // given
            val startDate = LocalDate.parse(startDateString)
            val endDate = LocalDate.parse(endDateString)

            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            val department1 = departmentLookUpService.fakeDepartmentRepository.`부서 생성`(company)
            val department2 = departmentLookUpService.fakeDepartmentRepository.`부서 생성`(company)
            employeeDepartmentAssignmentRepository.`부서 발령 생성`(employee, department1)

            // when
            service.appointJob(
                EmployeeAssignmentCreateCommand(
                    company.companyId,
                    employee.employeeId,
                    startDate,
                    endDate,
                    null,
                    department2.departmentId,
                ),
            )

            // then
            val actual = employeeDepartmentAssignmentRepository.db.values
            assertAll(
                { assertThat(actual).hasSize(2) },
                {
                    assertThat(actual.last())
                        .extracting(
                            "employeeDepartmentAssignmentId",
                            "employeeId",
                            "departmentId",
                            "startDate",
                            "endDate",
                        ).containsExactly(
                            2L,
                            1L,
                            2L,
                            LocalDate.parse(startDateString),
                            LocalDate.parse(endDateString),
                        )
                },
            )
        }
    }

    @Nested
    inner class `구성원의 새로운 직무를 발령할 때` {
        @ParameterizedTest
        @CsvSource(
            "'2024-12-31', '2025-01-01'",
            "'2025-01-31', '2025-02-01'",
        )
        fun `요구된 기간에 겹치는 직무 발령이 있다면 예외를 던진다`(startDateString: String, endDateString: String) {
            // given
            val startDate = LocalDate.parse(startDateString)
            val endDate = LocalDate.parse(endDateString)

            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            val jobRole = jobRoleLookUpService.fakeJobRoleRepository.`직무 생성`(company)
            employeeJobAssignmentRepository.`직무 발령 생성`(employee, jobRole)

            // when then
            assertThatThrownBy {
                service.appointJob(
                    EmployeeAssignmentCreateCommand(
                        company.companyId,
                        employee.employeeId,
                        startDate,
                        endDate,
                        jobRole.jobRoleId,
                        null,
                    ),
                )
            }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("이미 해당 기간에 발령된 직무가 있습니다.")
        }

        @ParameterizedTest
        @CsvSource(
            "'2024-12-30', '2024-12-31'",
            "'2025-02-01', '2025-02-02'",
        )
        fun `요구된 기간에 겹치는 직무 발령이 없다면 새로운 직무를 발령 한다`(startDateString: String, endDateString: String) {
            // given
            val startDate = LocalDate.parse(startDateString)
            val endDate = LocalDate.parse(endDateString)

            val company = companyLookUpService.fakeCompanyRepository.`회사 생성`()
            val employee = employeeLookUpService.fakeEmployeeRepository.`사원 생성`(company)
            val jobRole1 = jobRoleLookUpService.fakeJobRoleRepository.`직무 생성`(company)
            val jobRole2 = jobRoleLookUpService.fakeJobRoleRepository.`직무 생성`(company)
            employeeJobAssignmentRepository.`직무 발령 생성`(employee, jobRole1)

            // when
            service.appointJob(
                EmployeeAssignmentCreateCommand(
                    company.companyId,
                    employee.employeeId,
                    startDate,
                    endDate,
                    jobRole2.jobRoleId,
                    null,
                ),
            )

            // then
            val actual = employeeJobAssignmentRepository.db.values
            assertAll(
                { assertThat(actual).hasSize(2) },
                {
                    assertThat(actual.last())
                        .extracting(
                            "employeeJobAssignmentId",
                            "employeeId",
                            "startDate",
                            "endDate",
                        ).containsExactly(
                            2L,
                            1L,
                            LocalDate.parse(startDateString),
                            LocalDate.parse(endDateString),
                        )
                },
            )
        }
    }
}
