package team.flex.training.corehr.support.fake.company.job

import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.company.jobrole.JobRoleIdentity
import team.flex.training.corehr.company.jobrole.JobRoleLookUpService
import team.flex.training.corehr.company.jobrole.JobRoleModel

class FakeJobRoleLookUpService(
    val fakeJobRoleRepository: FakeJobRoleRepository = FakeJobRoleRepository(),
) : JobRoleLookUpService {
    override fun get(companyIdentity: CompanyIdentity, jobRoleIdentity: JobRoleIdentity): JobRoleModel {
        return fakeJobRoleRepository.get(companyIdentity.companyId, jobRoleIdentity.jobRoleId) ?: throw RuntimeException()
    }

    override fun getAll(companyIdentity: CompanyIdentity): List<JobRoleModel> {
        return fakeJobRoleRepository.getAll()
    }

    fun cleanUp() {
        fakeJobRoleRepository.db.clear()
    }
}
