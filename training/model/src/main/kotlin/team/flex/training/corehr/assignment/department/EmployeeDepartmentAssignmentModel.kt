package team.flex.training.corehr.assignment.department

import team.flex.training.corehr.AuditProps
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentProps
import team.flex.training.corehr.company.department.DepartmentIdentity
import team.flex.training.corehr.employee.EmployeeIdentity
import java.time.LocalDate

interface EmployeeDepartmentAssignmentProps {
    val startDate: LocalDate
    val endDate: LocalDate
}

interface EmployeeDepartmentAssignmentModel :
    EmployeeDepartmentAssignmentIdentity,
    DepartmentIdentity,
    EmployeeIdentity,
    EmployeeJobAssignmentProps,
    AuditProps
