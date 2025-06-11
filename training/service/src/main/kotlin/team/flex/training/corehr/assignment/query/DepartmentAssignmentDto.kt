package team.flex.training.corehr.assignment.query

import java.time.LocalDate

data class DepartmentAssignmentDto(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val departmentId: Long,
    val departmentName: String,
)
