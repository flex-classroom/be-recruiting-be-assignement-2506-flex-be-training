/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.employee

import team.flex.training.corehr.AuditProps
import team.flex.training.corehr.company.CompanyIdentity

interface EmployeeProps {
    val employeeNumber: String
    val name: String
}

interface EmployeeModel :
    EmployeeIdentity,
    CompanyIdentity,
    EmployeeProps,
    AuditProps
