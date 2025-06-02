/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.employee

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import team.flex.training.corehr.employee.repository.EmployeeRepository

@AutoConfiguration
class EmployeeAutoConfiguration {
    @Bean
    fun employeeLookUpServiceImpl(
        employeeRepository: EmployeeRepository,
    ): EmployeeLookUpService = EmployeeLookUpServiceImpl(
        employeeRepository = employeeRepository,
    )
}
