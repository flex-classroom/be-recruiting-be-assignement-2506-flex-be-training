package team.flex.training.corehr.assignment.department

import java.time.Instant
import java.time.LocalDate

data class EmployeeDepartmentAssignment(
    override val employeeDepartmentAssignmentId: Long,
    override val employeeId: Long,
    override val departmentId: Long,
    override val startDate: LocalDate,
    override val endDate: LocalDate,
    override val createdAt: Instant,
    override val updatedAt: Instant,
) : EmployeeDepartmentAssignmentModel
