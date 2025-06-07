package team.flex.training.corehr.assignment.query

import java.time.LocalDate

data class JobAssignmentDto(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val jobRoleId: Long,
    val jobRoleName: String,
)
