package team.flex.training.corehr.support.fake.employee

import team.flex.training.corehr.company.CompanyModel
import team.flex.training.corehr.employee.Employee
import team.flex.training.corehr.employee.EmployeeModel
import team.flex.training.corehr.support.fixture.company.CompanyFixture
import team.flex.training.corehr.support.fixture.employee.EmployeeFixture

class FakeEmployeeRepository {
    val db: MutableMap<CompanyEmployeeMapping, EmployeeModel> = mutableMapOf()

    fun get(companyId: Long, employeeId: Long): EmployeeModel? = db[CompanyEmployeeMapping(companyId, employeeId)]

    fun `사원 생성`(
        company: CompanyModel = CompanyFixture.기본,
        employee: EmployeeModel = EmployeeFixture.기본,
    ): EmployeeModel {
        val fixture = Employee(
            db.keys.size.toLong() + 1,
            company.companyId,
            employee.employeeNumber,
            employee.name,
            employee.createdAt,
            employee.updatedAt,
        )

        db[CompanyEmployeeMapping(fixture.companyId, fixture.employeeId)] = fixture

        return fixture
    }

    data class CompanyEmployeeMapping(val companyId: Long, val employeeId: Long)
}
