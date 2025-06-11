package team.flex.training.corehr.support.fixture.company

import team.flex.training.corehr.company.CompanyModel
import java.time.Instant

sealed class CompanyFixture(
    override val companyId: Long = 0,
    override val name: String = "회사 1",
    override val createdAt: Instant = Instant.now(),
    override val updatedAt: Instant = Instant.now(),
) : CompanyModel {
    data object 기본 : CompanyFixture()
}
