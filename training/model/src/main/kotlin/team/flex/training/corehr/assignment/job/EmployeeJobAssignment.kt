package team.flex.training.corehr.assignment.job

import java.time.Instant
import java.time.LocalDate

data class EmployeeJobAssignment(
    override val employeeJobAssignmentId: Long,
    override val employeeId: Long,
    override val jobRoleId: Long,
    override val startDate: LocalDate,
    override val endDate: LocalDate,
    override val createdAt: Instant,
    override val updatedAt: Instant,
) : EmployeeJobAssignmentModel
