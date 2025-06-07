package team.flex.training.corehr.support.fixture.employee

import team.flex.training.corehr.employee.EmployeeModel
import java.time.Instant

sealed class EmployeeFixture(
    override val employeeId: Long = 0,
    override val companyId: Long = 0,
    override val employeeNumber: String = "0001",
    override val name: String = "홍길동",
    override val createdAt: Instant = Instant.now(),
    override val updatedAt: Instant = Instant.now(),
) : EmployeeModel {
    data object 기본 : EmployeeFixture()
}
