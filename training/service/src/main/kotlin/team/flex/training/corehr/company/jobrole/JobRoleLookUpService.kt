/*
 * Copyright 2024 flex Inc. - All Rights Reserved.
 */

package team.flex.training.corehr.company.jobrole

import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.company.jobrole.repository.JobRoleRepository
import team.flex.training.corehr.exception.JobRoleNotFoundException

interface JobRoleLookUpService {
    fun get(companyIdentity: CompanyIdentity, jobRoleIdentity: JobRoleIdentity): JobRoleModel
    fun getAll(companyIdentity: CompanyIdentity): List<JobRoleModel>
}

internal class JobRoleLookUpServiceImpl(
    private val jobRoleRepository: JobRoleRepository,
) : JobRoleLookUpService {
    override fun get(companyIdentity: CompanyIdentity, jobRoleIdentity: JobRoleIdentity): JobRoleModel =
        jobRoleRepository.findByJobRoleIdentity(
            companyIdentity = companyIdentity,
            jobRoleIdentity = jobRoleIdentity,
        ) ?: throw JobRoleNotFoundException()

    override fun getAll(companyIdentity: CompanyIdentity): List<JobRoleModel> =
        jobRoleRepository.findAllByCompanyIdentity(companyIdentity = companyIdentity)
}
