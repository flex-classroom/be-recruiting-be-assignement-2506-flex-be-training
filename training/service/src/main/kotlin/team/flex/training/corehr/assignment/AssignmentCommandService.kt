package team.flex.training.corehr.assignment

import team.flex.training.corehr.assignment.command.EmployeeAssignmentCreateCommand
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignment
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentRepository
import team.flex.training.corehr.assignment.job.EmployeeJobAssignment
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentRepository
import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.company.CompanyLookUpService
import team.flex.training.corehr.company.department.DepartmentLookUpService
import team.flex.training.corehr.company.jobrole.JobRoleLookUpService
import team.flex.training.corehr.company.of
import team.flex.training.corehr.employee.EmployeeIdentity
import team.flex.training.corehr.employee.EmployeeLookUpService
import team.flex.training.corehr.employee.of
import team.flex.training.corehr.exception.EmployeeAssignmentExistException
import team.flex.training.corehr.global.annotation.Transactional
import java.time.Clock
import java.time.Instant
import java.time.LocalDate

interface AssignmentCommandService {
    fun appointJob(command: EmployeeAssignmentCreateCommand)
}

@Transactional
class AssignmentCommandServiceImpl(
    private val clock: Clock,
    private val companyLookUpService: CompanyLookUpService,
    private val employeeLookUpService: EmployeeLookUpService,
    private val departmentLookUpService: DepartmentLookUpService,
    private val jobRoleLookUpService: JobRoleLookUpService,
    private val employeeDepartmentAssignmentRepository: EmployeeDepartmentAssignmentRepository,
    private val employeeJobAssignmentRepository: EmployeeJobAssignmentRepository,
) : AssignmentCommandService {
    override fun appointJob(command: EmployeeAssignmentCreateCommand) {
        val company = companyLookUpService.get(CompanyIdentity.of(command.companyId))
        val employee = employeeLookUpService.get(company, EmployeeIdentity.of(command.employeeId))

        val now = Instant.now(clock)

        appointDepartmentAssignment(
            employee,
            company,
            command.departmentId,
            command.startDate,
            command.endDate,
            now,
        )

        appointJobRoleAssignment(
            employee,
            company,
            command.jobRoleId,
            command.startDate,
            command.endDate,
            now,
        )
    }

    private fun appointDepartmentAssignment(
        employeeIdentity: EmployeeIdentity,
        companyIdentity: CompanyIdentity,
        departmentId: Long?,
        startDate: LocalDate,
        endDate: LocalDate,
        now: Instant,
    ) {
        if (departmentId == null) {
            return
        }

        checkExistDepartmentAssignment(employeeIdentity, startDate, endDate)

        val employeeDepartmentAssignment = EmployeeDepartmentAssignment(
            0,
            employeeIdentity.employeeId,
            departmentId,
            startDate,
            endDate,
            now,
            now,
        )

        departmentLookUpService.get(companyIdentity, employeeDepartmentAssignment)
        employeeDepartmentAssignmentRepository.save(employeeDepartmentAssignment)
    }

    private fun checkExistDepartmentAssignment(
        employeeIdentity: EmployeeIdentity,
        startDate: LocalDate,
        endDate: LocalDate,
    ) {
        val existsAssignment =
            employeeDepartmentAssignmentRepository.existsByEmployeeIdAndPeriod(
                employeeIdentity,
                startDate,
                endDate,
            )

        check(!existsAssignment) {
            throw EmployeeAssignmentExistException("이미 해당 기간에 발령된 부서가 있습니다.")
        }
    }

    private fun appointJobRoleAssignment(
        employeeIdentity: EmployeeIdentity,
        companyIdentity: CompanyIdentity,
        jobRoleId: Long?,
        startDate: LocalDate,
        endDate: LocalDate,
        now: Instant,
    ) {
        if (jobRoleId == null) {
            return
        }

        checkExistJobRoleAssignment(employeeIdentity, startDate, endDate)

        val employeeJobRoleAssignment = EmployeeJobAssignment(
            0,
            employeeIdentity.employeeId,
            jobRoleId,
            startDate,
            endDate,
            now,
            now,
        )

        jobRoleLookUpService.get(companyIdentity, employeeJobRoleAssignment)
        employeeJobAssignmentRepository.save(employeeJobRoleAssignment)
    }

    private fun checkExistJobRoleAssignment(
        employeeIdentity: EmployeeIdentity,
        startDate: LocalDate,
        endDate: LocalDate,
    ) {
        val existsAssignment =
            employeeJobAssignmentRepository.existsByEmployeeIdAndPeriod(
                employeeIdentity,
                startDate,
                endDate,
            )

        check(!existsAssignment) {
            throw EmployeeAssignmentExistException("이미 해당 기간에 발령된 직무가 있습니다.")
        }
    }
}
