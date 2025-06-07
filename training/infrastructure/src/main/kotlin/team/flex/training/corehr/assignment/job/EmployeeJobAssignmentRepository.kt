package team.flex.training.corehr.assignment.job

import team.flex.training.corehr.employee.EmployeeIdentity
import java.time.LocalDate

interface EmployeeJobAssignmentRepository {
    fun existsByEmployeeIdAndPeriod(
        employeeIdentity: EmployeeIdentity,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean

    fun save(jobAssignment: EmployeeJobAssignmentModel): EmployeeJobAssignmentModel
}
