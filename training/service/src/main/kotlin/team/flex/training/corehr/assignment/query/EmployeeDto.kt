package team.flex.training.corehr.assignment.query

import team.flex.training.corehr.employee.EmployeeModel

data class EmployeeDto(
    val employeeId: Long,
    val employeeNumber: String,
    val employeeName: String,
) {
    companion object {
        fun from(employee: EmployeeModel): EmployeeDto =
            EmployeeDto(
                employee.employeeId,
                employee.employeeNumber,
                employee.name,
            )
    }
}
