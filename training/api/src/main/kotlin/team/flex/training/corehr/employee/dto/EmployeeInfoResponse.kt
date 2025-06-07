/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.employee.dto

import team.flex.training.corehr.assignment.query.CompanyDto
import team.flex.training.corehr.assignment.query.DepartmentAssignmentDto
import team.flex.training.corehr.assignment.query.EmployeeAssignmentResult
import team.flex.training.corehr.assignment.query.JobAssignmentDto
import java.time.LocalDate

class EmployeeAssignmentResponse(
    val employee: EmployeeDto,
    val assignment: AssignmentInfoDto,
) {
    companion object {
        fun of(result: EmployeeAssignmentResult): EmployeeAssignmentResponse =
            EmployeeAssignmentResponse(
                EmployeeDto.of(result.employee, result.company),
                AssignmentInfoDto.of(result.departmentAssignment, result.jobRoleAssignment),
            )
    }
}

class EmployeeAssignmentHistoryResponse(
    val employee: EmployeeDto,
    val assignments: List<AssignmentInfoDto>,
)

class EmployeeDto(
    val employeeId: Long,
    val employeeNumber: String,
    val employeeName: String,
    val companyId: Long,
    val companyName: String,
) {
    companion object {
        fun of(
            employee: team.flex.training.corehr.assignment.query.EmployeeDto,
            company: CompanyDto,
        ): EmployeeDto =
            EmployeeDto(
                employee.employeeId,
                employee.employeeNumber,
                employee.employeeName,
                company.companyId,
                company.companyName,
            )
    }
}

class AssignmentInfoDto(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val departmentId: Long?,
    val departmentName: String?,
    val jobRoleId: Long?,
    val jobRoleName: String?,
) {
    companion object {
        fun of(departmentAssignment: DepartmentAssignmentDto?, jobAssignment: JobAssignmentDto?): AssignmentInfoDto =
            AssignmentInfoDto(
                departmentAssignment?.startDate ?: jobAssignment?.startDate ?: LocalDate.MIN,
                departmentAssignment?.endDate ?: jobAssignment?.endDate ?: LocalDate.MIN,
                departmentAssignment?.departmentId,
                departmentAssignment?.departmentName,
                jobAssignment?.jobRoleId,
                jobAssignment?.jobRoleName,
            )
    }
}
