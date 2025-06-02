/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company.repository

import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.company.CompanyModel

interface CompanyRepository {
    fun findByCompanyIdentity(companyIdentity: CompanyIdentity): CompanyModel?
}
