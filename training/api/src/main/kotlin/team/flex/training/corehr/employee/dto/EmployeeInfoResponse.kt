/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.employee.dto

import java.time.LocalDate

class EmployeeAssignmentResponse(
    val employee: EmployeeDto,
    val assignment: AssignmentInfoDto,
)

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
)

class AssignmentInfoDto(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val departmentId: Long?,
    val departmentName: String?,
    val jobRoleId: Long?,
    val jobRoleName: String?,
)
