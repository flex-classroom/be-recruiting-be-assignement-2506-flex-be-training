package team.flex.training.corehr.support.fake.company.department

import team.flex.training.corehr.company.CompanyModel
import team.flex.training.corehr.company.department.Department
import team.flex.training.corehr.company.department.DepartmentModel
import team.flex.training.corehr.support.fixture.company.CompanyFixture
import team.flex.training.corehr.support.fixture.company.DepartmentFixture

class FakeDepartmentRepository {
    val db: MutableMap<CompanyDepartmentMapping, DepartmentModel> = mutableMapOf()

    fun get(companyId: Long, departmentId: Long): DepartmentModel? =
        db[CompanyDepartmentMapping(companyId, departmentId)]

    fun getAll(): List<DepartmentModel> = db.values.toList()

    fun `부서 생성`(
        company: CompanyModel = CompanyFixture.기본,
        department: DepartmentModel = DepartmentFixture.기본,
    ): DepartmentModel {
        val fixture = Department(
            db.keys.size.toLong() + 1,
            company.companyId,
            department.parentDepartmentId,
            department.name,
            department.createdAt,
            department.updatedAt,
        )
        db[CompanyDepartmentMapping(fixture.companyId, fixture.departmentId)] = fixture

        return fixture
    }

    data class CompanyDepartmentMapping(val companyId: Long, val department: Long)
}
