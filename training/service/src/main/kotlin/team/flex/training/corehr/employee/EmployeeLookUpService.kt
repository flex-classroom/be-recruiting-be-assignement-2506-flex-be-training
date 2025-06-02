/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.employee

import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.employee.repository.EmployeeRepository
import team.flex.training.corehr.exception.EmployeeNotFoundException

interface EmployeeLookUpService {
    fun get(companyIdentity: CompanyIdentity, employeeIdentity: EmployeeIdentity): EmployeeModel
}

internal class EmployeeLookUpServiceImpl(
    private val employeeRepository: EmployeeRepository,
) : EmployeeLookUpService {
    override fun get(companyIdentity: CompanyIdentity, employeeIdentity: EmployeeIdentity): EmployeeModel =
        employeeRepository.findByEmployeeIdentity(
            companyIdentity = companyIdentity,
            employeeIdentity = employeeIdentity,
        )
            ?: throw EmployeeNotFoundException()
}
