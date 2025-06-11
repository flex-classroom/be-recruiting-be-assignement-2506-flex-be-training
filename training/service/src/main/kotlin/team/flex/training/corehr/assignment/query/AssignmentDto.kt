package team.flex.training.corehr.assignment.query

import java.time.LocalDate

data class AssignmentDto(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val departmentId: Long? = null,
    val departmentName: String? = null,
    val jobRoleId: Long? = null,
    val jobRoleName: String? = null,
) {
    companion object {
        fun of(departmentAssignment: DepartmentAssignmentDto?, jobAssignment: JobAssignmentDto?): AssignmentDto =
            AssignmentDto(
                departmentAssignment?.startDate ?: jobAssignment?.startDate ?: LocalDate.MIN,
                departmentAssignment?.endDate ?: jobAssignment?.endDate ?: LocalDate.MIN,
                departmentAssignment?.departmentId,
                departmentAssignment?.departmentName,
                jobAssignment?.jobRoleId,
                jobAssignment?.jobRoleName,
            )
    }
}
