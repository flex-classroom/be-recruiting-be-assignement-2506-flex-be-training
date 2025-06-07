package team.flex.training.corehr.support.fake.company.job

import team.flex.training.corehr.company.CompanyModel
import team.flex.training.corehr.company.jobrole.JobRole
import team.flex.training.corehr.company.jobrole.JobRoleModel
import team.flex.training.corehr.support.fixture.company.CompanyFixture
import team.flex.training.corehr.support.fixture.company.JobRoleFixture

class FakeJobRoleRepository {
    val db: MutableMap<CompanyJobRoleMapping, JobRoleModel> = mutableMapOf()

    fun get(companyId: Long, jobRoleId: Long): JobRoleModel? =
        db[CompanyJobRoleMapping(companyId, jobRoleId)]

    fun getAll(): List<JobRoleModel> = db.values.toList()

    fun `직무 생성`(
        company: CompanyModel = CompanyFixture.기본,
        jobRole: JobRoleModel = JobRoleFixture.기본,
    ): JobRoleModel {
        val fixture = JobRole(
            db.keys.size.toLong() + 1,
            company.companyId,
            jobRole.name,
            jobRole.createdAt,
            jobRole.updatedAt,
        )
        db[CompanyJobRoleMapping(fixture.companyId, fixture.jobRoleId)] = fixture

        return fixture
    }

    data class CompanyJobRoleMapping(val companyId: Long, val jobRoleId: Long)
}
