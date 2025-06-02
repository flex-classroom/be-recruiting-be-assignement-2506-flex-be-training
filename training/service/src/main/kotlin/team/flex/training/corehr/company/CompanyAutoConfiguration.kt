/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import team.flex.training.corehr.company.repository.CompanyRepository

@AutoConfiguration
class CompanyAutoConfiguration {
    @Bean
    fun companyLookUpServiceImpl(
        companyRepository: CompanyRepository,
    ): CompanyLookUpService = CompanyLookUpServiceImpl(
        companyRepository = companyRepository,
    )
}
