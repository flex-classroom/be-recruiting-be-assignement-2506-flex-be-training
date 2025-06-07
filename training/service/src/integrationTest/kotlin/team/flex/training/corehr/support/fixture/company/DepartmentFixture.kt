package team.flex.training.corehr.support.fixture.company

import team.flex.training.corehr.company.department.DepartmentModel
import team.flex.training.corehr.company.department.repository.DepartmentEntity
import java.time.Instant

sealed class DepartmentFixture(
    override val departmentId: Long = 0,
    override val companyId: Long = 0,
    override val parentDepartmentId: Long? = 0,
    override val name: String = "부서 1",
    override val createdAt: Instant = Instant.now(),
    override val updatedAt: Instant = Instant.now(),
) : DepartmentModel {
    data object 기본 : DepartmentFixture()
    data object 개발팀 : DepartmentFixture(name = "개발팀")

    fun toEntity(companyId: Long = this.companyId): DepartmentEntity =
        DepartmentEntity(
            companyId,
            parentDepartmentId,
            name,
            createdAt,
            updatedAt,
        )
}
