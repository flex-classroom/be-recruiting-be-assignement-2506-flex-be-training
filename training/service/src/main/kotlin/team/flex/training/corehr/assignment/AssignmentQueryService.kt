package team.flex.training.corehr.assignment

import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentRepository
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentRepository
import team.flex.training.corehr.assignment.query.CompanyDto
import team.flex.training.corehr.assignment.query.DepartmentAssignmentDto
import team.flex.training.corehr.assignment.query.EmployeeAssignmentResult
import team.flex.training.corehr.assignment.query.EmployeeDto
import team.flex.training.corehr.assignment.query.JobAssignmentDto
import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.company.CompanyLookUpService
import team.flex.training.corehr.company.department.DepartmentIdentity
import team.flex.training.corehr.company.department.DepartmentLookUpService
import team.flex.training.corehr.company.department.of
import team.flex.training.corehr.company.jobrole.JobRoleIdentity
import team.flex.training.corehr.company.jobrole.JobRoleLookUpService
import team.flex.training.corehr.company.jobrole.of
import team.flex.training.corehr.company.of
import team.flex.training.corehr.employee.EmployeeIdentity
import team.flex.training.corehr.employee.EmployeeLookUpService
import team.flex.training.corehr.employee.EmployeeModel
import team.flex.training.corehr.employee.of
import java.time.LocalDate

interface AssignmentQueryService {
    fun getAssignment(
        companyId: Long,
        employeeId: Long,
        targetDate: LocalDate,
    ): EmployeeAssignmentResult
}

class AssignmentQueryServiceImpl(
    private val companyLookUpService: CompanyLookUpService,
    private val employeeLookUpService: EmployeeLookUpService,
    private val departmentLookUpService: DepartmentLookUpService,
    private val jobRoleLookUpService: JobRoleLookUpService,
    private val employeeDepartmentAssignmentRepository: EmployeeDepartmentAssignmentRepository,
    private val employeeJobAssignmentRepository: EmployeeJobAssignmentRepository,
) : AssignmentQueryService {
    override fun getAssignment(
        companyId: Long,
        employeeId: Long,
        targetDate: LocalDate,
    ): EmployeeAssignmentResult {
        val company = companyLookUpService.get(CompanyIdentity.of(companyId))
        val employee = employeeLookUpService.get(company, EmployeeIdentity.of(employeeId))

        val departmentAssignment = queryDepartmentAssignment(employee, targetDate)
        val jobAssignment = queryJobRoleAssignment(employee, targetDate)

        return EmployeeAssignmentResult(
            EmployeeDto.from(employee),
            CompanyDto.from(company),
            departmentAssignment,
            jobAssignment,
        )
    }

    private fun queryDepartmentAssignment(
        employee: EmployeeModel,
        targetDate: LocalDate,
    ): DepartmentAssignmentDto? {
        val departmentAssignment =
            employeeDepartmentAssignmentRepository.findByEmployeeIdAndDateBetween(employee, targetDate)
                ?: return null

        val department =
            departmentLookUpService.get(employee, DepartmentIdentity.of(departmentAssignment.departmentId))

        return DepartmentAssignmentDto.of(departmentAssignment, department)
    }

    private fun queryJobRoleAssignment(
        employee: EmployeeModel,
        targetDate: LocalDate,
    ): JobAssignmentDto? {
        val jobAssignment =
            employeeJobAssignmentRepository.findByEmployeeIdAndDateBetween(employee, targetDate)
                ?: return null

        val jobRole = jobRoleLookUpService.get(employee, JobRoleIdentity.of(jobAssignment.jobRoleId))

        return JobAssignmentDto.of(jobAssignment, jobRole)
    }
}
