package team.flex.training.corehr.support.fake.company

import team.flex.training.corehr.company.Company
import team.flex.training.corehr.company.CompanyModel
import team.flex.training.corehr.support.fixture.company.CompanyFixture

class FakeCompanyRepository {
    val db: MutableMap<Long, CompanyModel> = mutableMapOf()

    fun get(companyId: Long): CompanyModel? = db[companyId]

    fun `회사 생성`(company: CompanyModel = CompanyFixture.기본): CompanyModel {
        val fixture = Company(
            db.keys.size.toLong() + 1,
            company.name,
            company.createdAt,
            company.updatedAt,
        )
        db[fixture.companyId] = fixture

        return fixture
    }
}
