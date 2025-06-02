/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company.department.repository

import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.company.department.DepartmentIdentity
import team.flex.training.corehr.company.department.DepartmentModel

interface DepartmentRepository {
    fun findByDepartmentIdentity(
        companyIdentity: CompanyIdentity,
        departmentIdentity: DepartmentIdentity,
    ): DepartmentModel?
    fun findAllByCompanyIdentity(companyIdentity: CompanyIdentity): List<DepartmentModel>
}
