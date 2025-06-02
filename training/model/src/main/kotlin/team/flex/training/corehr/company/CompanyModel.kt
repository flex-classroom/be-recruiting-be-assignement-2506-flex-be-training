/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company

import team.flex.training.corehr.AuditProps

interface CompanyProps {
    val name: String
}

interface CompanyModel :
    CompanyIdentity,
    CompanyProps,
    AuditProps
