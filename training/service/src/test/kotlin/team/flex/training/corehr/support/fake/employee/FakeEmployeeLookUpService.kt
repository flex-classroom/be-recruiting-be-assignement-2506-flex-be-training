package team.flex.training.corehr.support.fake.employee

import team.flex.training.corehr.company.CompanyIdentity
import team.flex.training.corehr.employee.EmployeeIdentity
import team.flex.training.corehr.employee.EmployeeLookUpService
import team.flex.training.corehr.employee.EmployeeModel

class FakeEmployeeLookUpService(
    val fakeEmployeeRepository: FakeEmployeeRepository = FakeEmployeeRepository(),
) : EmployeeLookUpService {
    override fun get(companyIdentity: CompanyIdentity, employeeIdentity: EmployeeIdentity): EmployeeModel {
        return fakeEmployeeRepository.get(companyIdentity.companyId, employeeIdentity.employeeId) ?: throw RuntimeException()
    }

    fun cleanUp() {
        fakeEmployeeRepository.db.clear()
    }
}
