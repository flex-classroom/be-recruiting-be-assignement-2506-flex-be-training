/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company.department

import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.exception.DepartmentNotFoundException
import team.flex.training.corehr.company.department.repository.DepartmentRepository

interface DepartmentLookUpService {
    fun get(companyIdentity: CompanyIdentity, departmentIdentity: DepartmentIdentity): DepartmentModel
    fun getAll(companyIdentity: CompanyIdentity): List<DepartmentModel>
}

internal class DepartmentLookUpServiceImpl(
    private val departmentRepository: DepartmentRepository,
) : DepartmentLookUpService {
    override fun get(companyIdentity: CompanyIdentity, departmentIdentity: DepartmentIdentity): DepartmentModel =
        departmentRepository.findByDepartmentIdentity(
            companyIdentity = companyIdentity,
            departmentIdentity = departmentIdentity,
        )
            ?: throw DepartmentNotFoundException()

    override fun getAll(companyIdentity: CompanyIdentity): List<DepartmentModel> =
        departmentRepository.findAllByCompanyIdentity(companyIdentity = companyIdentity)
}
