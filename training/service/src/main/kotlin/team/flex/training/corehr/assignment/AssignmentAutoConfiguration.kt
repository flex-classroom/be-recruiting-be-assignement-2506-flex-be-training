package team.flex.training.corehr.assignment

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentRepository
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentRepository
import team.flex.training.corehr.company.CompanyLookUpService
import team.flex.training.corehr.company.department.DepartmentLookUpService
import team.flex.training.corehr.company.jobrole.JobRoleLookUpService
import team.flex.training.corehr.employee.EmployeeLookUpService
import java.time.Clock

@AutoConfiguration
class AssignmentAutoConfiguration {
    @Bean
    fun assignmentCommandService(
        clock: Clock,
        companyLookUpService: CompanyLookUpService,
        employeeLookUpService: EmployeeLookUpService,
        departmentLookUpService: DepartmentLookUpService,
        jobRoleLookUpService: JobRoleLookUpService,
        employeeDepartmentAssignmentRepository: EmployeeDepartmentAssignmentRepository,
        employeeJobAssignmentRepository: EmployeeJobAssignmentRepository,
    ): AssignmentCommandService =
        AssignmentCommandServiceImpl(
            clock,
            companyLookUpService,
            employeeLookUpService,
            departmentLookUpService,
            jobRoleLookUpService,
            employeeDepartmentAssignmentRepository,
            employeeJobAssignmentRepository,
        )

    @Bean
    fun assignmentQueryService(
        companyLookUpService: CompanyLookUpService,
        employeeLookUpService: EmployeeLookUpService,
        employeeDepartmentAssignmentRepository: EmployeeDepartmentAssignmentRepository,
        employeeJobAssignmentRepository: EmployeeJobAssignmentRepository,
    ): AssignmentQueryServiceImpl =
        AssignmentQueryServiceImpl(
            companyLookUpService,
            employeeLookUpService,
            employeeDepartmentAssignmentRepository,
            employeeJobAssignmentRepository,
        )
}
