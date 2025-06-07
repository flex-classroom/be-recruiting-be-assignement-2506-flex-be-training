package team.flex.training.corehr.assignment.command

import java.time.LocalDate

data class EmployeeAssignmentCreateCommand(
    val companyId: Long,
    val employeeId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val jobRoleId: Long?,
    val departmentId: Long?,
)
