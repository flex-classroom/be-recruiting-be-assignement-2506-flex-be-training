package team.flex.training.corehr.support.fixture.assignment

import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentModel
import java.time.Instant
import java.time.LocalDate

sealed class EmployeeDepartmentAssignmentEntityFixture(
    override val employeeDepartmentAssignmentId: Long = 0,
    override val employeeId: Long = 0,
    override val departmentId: Long = 0,
    override val startDate: LocalDate = LocalDate.parse("2025-01-01"),
    override val endDate: LocalDate = LocalDate.parse("2025-01-31"),
    override val createdAt: Instant = Instant.now(),
    override val updatedAt: Instant = Instant.now(),
) : EmployeeDepartmentAssignmentModel {
    data object 기본 : EmployeeDepartmentAssignmentEntityFixture()
}
