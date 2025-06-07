package team.flex.training.corehr.support.fixture.company

import team.flex.training.corehr.company.department.DepartmentModel
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
}
