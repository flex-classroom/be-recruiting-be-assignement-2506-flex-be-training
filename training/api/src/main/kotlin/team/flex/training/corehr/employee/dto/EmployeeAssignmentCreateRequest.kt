/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.employee.dto

import java.time.LocalDate

class EmployeeAssignmentCreateRequest(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val jobRoleId: Long?,
    val departmentId: Long?,
)
