/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.employee.repository

import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.employee.EmployeeIdentity
import team.flex.training.corehr.employee.EmployeeModel

interface EmployeeRepository {
    fun findByEmployeeIdentity(companyIdentity: CompanyIdentity, employeeIdentity: EmployeeIdentity): EmployeeModel?
    fun findAllByCompanyIdentity(companyIdentity: CompanyIdentity): List<EmployeeModel>
}
