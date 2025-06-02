/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company

import team.flex.training.corehr.company.repository.CompanyRepository
import team.flex.training.corehr.exception.CompanyNotFoundException

interface CompanyLookUpService {
    fun get(companyIdentity: CompanyIdentity): CompanyModel
}

internal class CompanyLookUpServiceImpl(
    private val companyRepository: CompanyRepository,
) : CompanyLookUpService {
    override fun get(companyIdentity: CompanyIdentity): CompanyModel =
        companyRepository.findByCompanyIdentity(companyIdentity = companyIdentity) ?: throw CompanyNotFoundException()
}
