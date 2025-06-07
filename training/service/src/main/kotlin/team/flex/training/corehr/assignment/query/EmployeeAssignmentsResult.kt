package team.flex.training.corehr.assignment.query

data class EmployeeAssignmentsResult(
    val employee: EmployeeDto,
    val company: CompanyDto,
    val assignments: List<AssignmentDto>,
)
