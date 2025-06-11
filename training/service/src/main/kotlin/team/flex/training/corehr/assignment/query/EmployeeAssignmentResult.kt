package team.flex.training.corehr.assignment.query

data class EmployeeAssignmentResult(
    val employee: EmployeeDto,
    val company: CompanyDto,
    val assignment: AssignmentDto,
)
