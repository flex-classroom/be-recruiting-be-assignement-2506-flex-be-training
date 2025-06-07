package team.flex.training.corehr.assignment.job.dto

import java.time.LocalDate

data class JobRoleAssignmentDto(
    val employeeJobAssignmentId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val jobRoleId: Long,
    val jobName: String,
)
