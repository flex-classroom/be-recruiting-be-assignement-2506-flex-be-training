package team.flex.training.corehr.assignment.job

import team.flex.training.corehr.AuditProps
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentProps
import team.flex.training.corehr.company.jobrole.JobRoleIdentity
import team.flex.training.corehr.employee.EmployeeIdentity
import java.time.LocalDate

interface EmployeeJobAssignmentProps {
    val startDate: LocalDate
    val endDate: LocalDate
}

interface EmployeeJobAssignmentModel :
    EmployeeJobAssignmentIdentity,
    JobRoleIdentity,
    EmployeeIdentity,
    EmployeeDepartmentAssignmentProps,
    AuditProps
