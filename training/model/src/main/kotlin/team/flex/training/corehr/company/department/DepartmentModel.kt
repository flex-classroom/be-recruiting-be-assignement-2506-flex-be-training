/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company.department

import team.flex.training.corehr.AuditProps
import team.flex.training.corehr.company.CompanyIdentity

interface DepartmentProps {
    val name: String
}

interface ParentDepartmentRelationIdentity {
    val parentDepartmentId: Long?
}

interface DepartmentModel :
    DepartmentIdentity,
    ParentDepartmentRelationIdentity,
    CompanyIdentity,
    DepartmentProps,
    AuditProps
