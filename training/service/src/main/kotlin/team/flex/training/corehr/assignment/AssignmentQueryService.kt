package team.flex.training.corehr.assignment

import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentRepository
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentRepository
import team.flex.training.corehr.assignment.query.AssignmentDto
import team.flex.training.corehr.assignment.query.CompanyDto
import team.flex.training.corehr.assignment.query.DepartmentAssignmentDto
import team.flex.training.corehr.assignment.query.EmployeeAssignmentResult
import team.flex.training.corehr.assignment.query.EmployeeAssignmentsResult
import team.flex.training.corehr.assignment.query.EmployeeDto
import team.flex.training.corehr.assignment.query.JobAssignmentDto
import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.company.CompanyLookUpService
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

    fun getAssignments(
        companyId: Long,
        employeeId: Long,
    ): EmployeeAssignmentsResult
}

class AssignmentQueryServiceImpl(
    private val companyLookUpService: CompanyLookUpService,
    private val employeeLookUpService: EmployeeLookUpService,
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
            AssignmentDto.of(departmentAssignment, jobAssignment),
        )
    }

    private fun queryDepartmentAssignment(
        employee: EmployeeModel,
        targetDate: LocalDate,
    ): DepartmentAssignmentDto? {
        val departmentAssignment =
            employeeDepartmentAssignmentRepository.findByEmployeeIdAndDateBetween(employee, targetDate)
                ?: return null

        return DepartmentAssignmentDto(
            departmentAssignment.startDate,
            departmentAssignment.endDate,
            departmentAssignment.departmentId,
            departmentAssignment.departmentName,
        )
    }

    private fun queryJobRoleAssignment(
        employee: EmployeeModel,
        targetDate: LocalDate,
    ): JobAssignmentDto? {
        val jobAssignment =
            employeeJobAssignmentRepository.findByEmployeeIdAndDateBetween(employee, targetDate)
                ?: return null

        return JobAssignmentDto(
            jobAssignment.startDate,
            jobAssignment.endDate,
            jobAssignment.jobRoleId,
            jobAssignment.jobName,
        )
    }

    override fun getAssignments(
        companyId: Long,
        employeeId: Long,
    ): EmployeeAssignmentsResult {
        val company = companyLookUpService.get(CompanyIdentity.of(companyId))
        val employee = employeeLookUpService.get(company, EmployeeIdentity.of(employeeId))

        val assignments = queryAssignments(employee)

        return EmployeeAssignmentsResult(
            EmployeeDto.from(employee),
            CompanyDto.from(company),
            assignments,
        )
    }

    private fun queryAssignments(employee: EmployeeModel): List<AssignmentDto> {
        val departmentAssignments = employeeDepartmentAssignmentRepository.findByEmployeeId(employee)
            .map {
                AssignmentDto(
                    it.startDate,
                    it.endDate,
                    it.departmentId,
                    it.departmentName,
                )
            }

        val jobAssignments = employeeJobAssignmentRepository.findByEmployeeId(employee)
            .map {
                AssignmentDto(
                    it.startDate,
                    it.endDate,
                    jobRoleId = it.jobRoleId,
                    jobRoleName = it.jobName,
                )
            }

        return (departmentAssignments union jobAssignments)
            .sortedWith(
                compareByDescending(AssignmentDto::startDate)
                    .thenByDescending(AssignmentDto::endDate),
            )
    }
}
