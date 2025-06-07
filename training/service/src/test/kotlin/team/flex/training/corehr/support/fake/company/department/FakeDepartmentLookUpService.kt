package team.flex.training.corehr.support.fake.company.department

import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.company.department.DepartmentIdentity
import team.flex.training.corehr.company.department.DepartmentLookUpService
import team.flex.training.corehr.company.department.DepartmentModel

class FakeDepartmentLookUpService(
    val fakeDepartmentRepository: FakeDepartmentRepository = FakeDepartmentRepository(),
) : DepartmentLookUpService {
    override fun get(companyIdentity: CompanyIdentity, departmentIdentity: DepartmentIdentity): DepartmentModel {
        return fakeDepartmentRepository.get(companyIdentity.companyId, departmentIdentity.departmentId) ?: throw RuntimeException()
    }

    override fun getAll(companyIdentity: CompanyIdentity): List<DepartmentModel> {
        return fakeDepartmentRepository.getAll()
    }

    fun cleanUp() {
        fakeDepartmentRepository.db.clear()
    }
}
