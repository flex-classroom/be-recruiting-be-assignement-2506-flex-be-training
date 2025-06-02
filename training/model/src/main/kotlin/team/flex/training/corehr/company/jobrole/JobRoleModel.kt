/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company.jobrole

import team.flex.training.corehr.AuditProps
import team.flex.training.corehr.company.CompanyIdentity

interface JobRoleProps {
    val name: String
}

interface JobRoleModel :
    JobRoleIdentity,
    CompanyIdentity,
    JobRoleProps,
    AuditProps
