package team.flex.training.corehr.support.fake.company

import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.company.CompanyLookUpService
import team.flex.training.corehr.company.CompanyModel

class FakeCompanyLookUpService(
    val fakeCompanyRepository: FakeCompanyRepository = FakeCompanyRepository(),
) : CompanyLookUpService {
    override fun get(companyIdentity: CompanyIdentity): CompanyModel {
        return fakeCompanyRepository.get(companyIdentity.companyId) ?: throw RuntimeException()
    }

    fun cleanUp() {
        fakeCompanyRepository.db.clear()
    }
}
