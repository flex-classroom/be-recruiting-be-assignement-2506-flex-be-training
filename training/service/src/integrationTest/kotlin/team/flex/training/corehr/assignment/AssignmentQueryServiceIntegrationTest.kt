package team.flex.training.corehr.assignment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import team.flex.training.corehr.support.IntegrationTestSupport
import team.flex.training.corehr.support.fixture.assignment.EmployeeDepartmentAssignmentFixture
import team.flex.training.corehr.support.fixture.assignment.EmployeeJobAssignmentFixture
import team.flex.training.corehr.support.fixture.company.DepartmentFixture
import team.flex.training.corehr.support.fixture.company.JobRoleFixture
import team.flex.training.corehr.support.fixture.employee.EmployeeFixture
import java.time.LocalDate

class AssignmentQueryServiceIntegrationTest(
    private val service: AssignmentQueryService,
) : IntegrationTestSupport() {

    @Test
    fun `구성원의 요청일 기준 부서 발령 정보를 조회할 수 있다`() {
        // given
        val company = `회사 생성`()
        val employee = `사원 생성`(EmployeeFixture.기본.toEntity(company.id))
        val department = `부서 생성`(DepartmentFixture.기본.toEntity(company.id))
        `부서 발령 생성`(EmployeeDepartmentAssignmentFixture.기본.toEntity(employee.id, department.id))

        // when
        val actual = service.getAssignment(company.id, employee.id, LocalDate.parse("2025-01-01"))

        // then
        assertThat(actual.assignment).extracting("departmentId", "departmentName")
            .containsExactly(1L, "부서 1")
    }

    @Test
    fun `구성원의 요청일 기준 직무 발령 정보를 조회할 수 있다`() {
        // given
        val company = `회사 생성`()
        val employee = `사원 생성`(EmployeeFixture.기본.toEntity(company.id))
        val jobRole = `직무 생성`(JobRoleFixture.기본.toEntity(company.id))
        `직무 발령 생성`(EmployeeJobAssignmentFixture.기본.toEntity(employee.id, jobRole.id))

        // when
        val actual = service.getAssignment(company.id, employee.id, LocalDate.parse("2025-01-01"))

        // then
        assertThat(actual.assignment).extracting("jobRoleId", "jobRoleName")
            .containsExactly(1L, "직무 1")
    }

    @Test
    fun `구성원의 모든 부서, 직무 발령을 조회할 수 있다`() {
        // given
        val company = `회사 생성`()
        val employee = `사원 생성`(EmployeeFixture.기본.toEntity(company.id))
        val department = `부서 생성`(DepartmentFixture.기본.toEntity(company.id))
        val jobRole = `직무 생성`(JobRoleFixture.기본.toEntity(company.id))
        `부서 발령 생성`(EmployeeDepartmentAssignmentFixture.기본.toEntity(employee.id, department.id))
        `직무 발령 생성`(EmployeeJobAssignmentFixture.기본.toEntity(employee.id, jobRole.id))

        // when
        val actual = service.getAssignments(company.id, employee.id)

        // then
        assertThat(actual.assignments).hasSize(2)
    }
}