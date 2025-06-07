package team.flex.training.corehr.assignment

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import team.flex.training.corehr.assignment.command.EmployeeAssignmentCreateCommand
import team.flex.training.corehr.support.IntegrationTestSupport
import team.flex.training.corehr.support.fixture.assignment.EmployeeDepartmentAssignmentFixture
import team.flex.training.corehr.support.fixture.assignment.EmployeeJobAssignmentFixture
import team.flex.training.corehr.support.fixture.company.DepartmentFixture
import team.flex.training.corehr.support.fixture.company.JobRoleFixture
import team.flex.training.corehr.support.fixture.employee.EmployeeFixture
import java.time.LocalDate

class AssignmentCommandServiceIntegrationTest(
    private val service: AssignmentCommandService,
) : IntegrationTestSupport() {

    @Nested
    inner class `구성원의 새로운 부서 또는 직무를 발령할 때` {

        @Test
        fun `부서 발령이 완료 되면 새로운 부서 발령 정보가 생긴다`() {
            // given
            val company = `회사 생성`()
            val employee = `사원 생성`(EmployeeFixture.기본.toEntity(company.id))
            val department = `부서 생성`(DepartmentFixture.기본.toEntity(company.id))
            val startDate = LocalDate.parse("2025-01-01")
            val endDate = LocalDate.parse("2025-01-31")

            // when
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

            // then
            val actual = employeeDepartmentAssignmentRepository.findAll()
            assertThat(actual).hasSize(1)
                .extracting("employeeId", "departmentId")
                .containsExactly(Tuple.tuple(1L, 1L))
        }

        @Test
        fun `직무 발령이 완료 되면 새로운 직무 발령 정보가 생긴다`() {
            // given
            val company = `회사 생성`()
            val employee = `사원 생성`(EmployeeFixture.기본.toEntity(company.id))
            val jobRole = `직무 생성`(JobRoleFixture.기본.toEntity(company.id))
            val startDate = LocalDate.parse("2025-01-01")
            val endDate = LocalDate.parse("2025-01-31")

            // when
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

            // then
            val actual = employeeJobAssignmentRepository.findAll()
            assertThat(actual).hasSize(1)
                .extracting("employeeId", "jobRoleId")
                .containsExactly(Tuple.tuple(1L, 1L))
        }

        @Test
        fun `부서 발령에 실패하면 직무 발령도 실패 한다`() {
            // given
            val company = `회사 생성`()
            val employee = `사원 생성`(EmployeeFixture.기본.toEntity(company.id))
            val department = `부서 생성`(DepartmentFixture.기본.toEntity(company.id))
            `부서 발령 생성`(EmployeeDepartmentAssignmentFixture.기본.toEntity(employee.id, department.id))
            val startDate = LocalDate.parse("2025-01-01")
            val endDate = LocalDate.parse("2025-01-31")

            // when
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
            }

            // then
            val actual = employeeJobAssignmentRepository.findAll()
            assertThat(actual).isEmpty()
        }

        @Test
        fun `직무 발령에 실패하면 부서 발령도 실패 한다`() {
            // given
            val company = `회사 생성`()
            val employee = `사원 생성`(EmployeeFixture.기본.toEntity(company.id))
            val jobRole = `직무 생성`(JobRoleFixture.기본.toEntity(company.id))
            `직무 발령 생성`(EmployeeJobAssignmentFixture.기본.toEntity(employee.id, jobRole.id))
            val startDate = LocalDate.parse("2025-01-01")
            val endDate = LocalDate.parse("2025-01-31")

            // when
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
            }

            // then
            val actual = employeeDepartmentAssignmentRepository.findAll()
            assertThat(actual).isEmpty()
        }
    }
}
