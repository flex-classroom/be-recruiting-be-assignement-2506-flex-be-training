package team.flex.training.corehr.assignment.department

import team.flex.training.corehr.employee.EmployeeIdentity
import java.time.LocalDate

interface EmployeeDepartmentAssignmentRepository {
    fun existsByEmployeeIdAndPeriod(
        employeeIdentity: EmployeeIdentity,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean

    fun save(departmentAssignment: EmployeeDepartmentAssignmentModel): EmployeeDepartmentAssignmentModel
}
