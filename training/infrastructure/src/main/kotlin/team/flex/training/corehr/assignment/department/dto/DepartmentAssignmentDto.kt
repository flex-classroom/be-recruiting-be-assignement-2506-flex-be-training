package team.flex.training.corehr.assignment.department.dto

import java.time.LocalDate

data class DepartmentAssignmentDto(
    val employeeDepartmentAssignmentId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val departmentId: Long,
    val departmentName: String,
)