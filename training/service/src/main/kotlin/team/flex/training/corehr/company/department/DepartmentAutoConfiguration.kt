/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company.department

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import team.flex.training.corehr.company.department.repository.DepartmentRepository

@AutoConfiguration
class DepartmentAutoConfiguration {
    @Bean
    fun departmentLookUpServiceImpl(
        departmentRepository: DepartmentRepository,
    ): DepartmentLookUpService = DepartmentLookUpServiceImpl(
        departmentRepository = departmentRepository,
    )
}
