/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import team.flex.training.corehr.company.Company
import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.company.CompanyModel

interface CompanyJdbcRepository : CrudRepository<CompanyEntity, Long>

class CompanyRepositoryImpl(
    private val companyJdbcRepository: CompanyJdbcRepository,
) : CompanyRepository {
    override fun findByCompanyIdentity(companyIdentity: CompanyIdentity): CompanyModel? {
        return companyJdbcRepository.findByIdOrNull(id = companyIdentity.companyId)?.toModel()
    }

    private fun CompanyEntity.toModel() =
        Company(
            companyId = companyId,
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
