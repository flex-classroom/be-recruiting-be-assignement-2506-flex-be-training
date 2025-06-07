package team.flex.training.corehr.assignment.job

import team.flex.training.corehr.assignment.job.dto.JobRoleAssignmentDto
import team.flex.training.corehr.employee.EmployeeIdentity
import java.time.LocalDate

interface EmployeeJobAssignmentRepository {
    fun existsByEmployeeIdAndPeriod(
        employeeIdentity: EmployeeIdentity,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean

    fun save(jobAssignment: EmployeeJobAssignmentModel): EmployeeJobAssignmentModel

    fun findByEmployeeIdAndDateBetween(employeeIdentity: EmployeeIdentity, targetDate: LocalDate): JobRoleAssignmentDto?

    fun findByEmployeeId(employeeIdentity: EmployeeIdentity): List<JobRoleAssignmentDto>
}
