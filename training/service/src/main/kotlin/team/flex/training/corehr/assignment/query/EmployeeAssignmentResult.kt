package team.flex.training.corehr.assignment.query

import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentModel
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentModel
import team.flex.training.corehr.company.CompanyModel
import team.flex.training.corehr.company.department.DepartmentModel
import team.flex.training.corehr.company.jobrole.JobRoleModel
import team.flex.training.corehr.employee.EmployeeModel
import java.time.LocalDate

data class EmployeeAssignmentResult(
    val employee: EmployeeDto,
    val company: CompanyDto,
    val departmentAssignment: DepartmentAssignmentDto?,
    val jobRoleAssignment: JobAssignmentDto?,
)

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

data class CompanyDto(
    val companyId: Long,
    val companyName: String,
) {
    companion object {
        fun from(company: CompanyModel): CompanyDto = CompanyDto(company.companyId, company.name)
    }
}

data class DepartmentAssignmentDto(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val departmentId: Long,
    val departmentName: String,
) {
    companion object {
        fun of(
            departmentAssignment: EmployeeDepartmentAssignmentModel,
            department: DepartmentModel,
        ): DepartmentAssignmentDto = DepartmentAssignmentDto(
            departmentAssignment.startDate,
            departmentAssignment.endDate,
            department.departmentId,
            department.name,
        )
    }
}

data class JobAssignmentDto(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val jobRoleId: Long,
    val jobRoleName: String,
) {
    companion object {
        fun of(
            jobAssignment: EmployeeJobAssignmentModel,
            jobRole: JobRoleModel,
        ): JobAssignmentDto = JobAssignmentDto(
            jobAssignment.startDate,
            jobAssignment.endDate,
            jobRole.jobRoleId,
            jobRole.name,
        )
    }
}
