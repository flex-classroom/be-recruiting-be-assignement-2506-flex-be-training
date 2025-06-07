package team.flex.training.corehr.support.fixture.company

import team.flex.training.corehr.company.jobrole.JobRoleModel
import team.flex.training.corehr.company.jobrole.repository.JobRoleEntity
import java.time.Instant

sealed class JobRoleFixture(
    override val jobRoleId: Long = 0,
    override val companyId: Long = 0,
    override val name: String = "직무 1",
    override val createdAt: Instant = Instant.now(),
    override val updatedAt: Instant = Instant.now(),
) : JobRoleModel {
    data object 기본 : JobRoleFixture()

    fun toEntity(companyId: Long = this.companyId): JobRoleEntity = JobRoleEntity(
        companyId,
        name,
        createdAt,
        updatedAt,
    )
}
