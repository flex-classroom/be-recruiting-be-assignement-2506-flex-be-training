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
        employeeLookUpService: EmployeeLookUpService,
        companyLookUpService: CompanyLookUpService,
        departmentLookUpService: DepartmentLookUpService,
        jobRoleLookUpService: JobRoleLookUpService,
        employeeDepartmentAssignmentRepository: EmployeeDepartmentAssignmentRepository,
        employeeJobAssignmentRepository: EmployeeJobAssignmentRepository,
    ): AssignmentCommandService =
        AssignmentCommandServiceImpl(
            clock,
            employeeLookUpService,
            companyLookUpService,
            departmentLookUpService,
            jobRoleLookUpService,
            employeeDepartmentAssignmentRepository,
            employeeJobAssignmentRepository,
        )
}
