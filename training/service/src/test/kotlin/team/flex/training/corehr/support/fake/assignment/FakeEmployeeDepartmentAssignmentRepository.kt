package team.flex.training.corehr.support.fake.assignment

import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignment
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentModel
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentRepository
import team.flex.training.corehr.assignment.department.dto.DepartmentAssignmentDto
import team.flex.training.corehr.company.department.DepartmentModel
import team.flex.training.corehr.employee.EmployeeIdentity
import team.flex.training.corehr.employee.EmployeeModel
import team.flex.training.corehr.support.fixture.assignment.EmployeeDepartmentAssignmentEntityFixture
import team.flex.training.corehr.support.fixture.company.DepartmentFixture
import team.flex.training.corehr.support.fixture.employee.EmployeeFixture
import java.time.LocalDate

class FakeEmployeeDepartmentAssignmentRepository : EmployeeDepartmentAssignmentRepository {
    val db: MutableMap<Long, EmployeeDepartmentAssignmentModel> = mutableMapOf()

    override fun existsByEmployeeIdAndPeriod(
        employeeIdentity: EmployeeIdentity,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean {
        return db.values.filter { it.employeeId == employeeIdentity.employeeId }
            .any {
                it.startDate <= endDate && it.endDate >= startDate
            }
    }

    override fun save(departmentAssignment: EmployeeDepartmentAssignmentModel): EmployeeDepartmentAssignmentModel {
        val model = EmployeeDepartmentAssignment(
            db.keys.size.toLong() + 1,
            departmentAssignment.employeeId,
            departmentAssignment.departmentId,
            departmentAssignment.startDate,
            departmentAssignment.endDate,
            departmentAssignment.createdAt,
            departmentAssignment.updatedAt,
        )

        db[model.employeeDepartmentAssignmentId] = model

        return model
    }

    override fun findByEmployeeIdAndDateBetween(
        employeeIdentity: EmployeeIdentity,
        targetDate: LocalDate,
    ): DepartmentAssignmentDto? {
        return db.values.filter { it.employeeId == employeeIdentity.employeeId }
            .map {
                DepartmentAssignmentDto(
                    it.employeeDepartmentAssignmentId,
                    it.startDate,
                    it.endDate,
                    it.departmentId,
                    "부서",
                )
            }.find {
                (targetDate.isEqual(it.startDate) || targetDate.isAfter(it.startDate)) &&
                    (targetDate.isEqual(it.endDate) || targetDate.isBefore(it.endDate))
            }
    }

    override fun findByEmployeeId(employeeIdentity: EmployeeIdentity): List<DepartmentAssignmentDto> {
        return db.values.filter { it.employeeId == employeeIdentity.employeeId }
            .map {
                DepartmentAssignmentDto(
                    it.employeeDepartmentAssignmentId,
                    it.startDate,
                    it.endDate,
                    it.departmentId,
                    "부서",
                )
            }
    }

    fun `부서 발령 생성`(
        employee: EmployeeModel = EmployeeFixture.기본,
        department: DepartmentModel = DepartmentFixture.기본,
        departmentAssignment: EmployeeDepartmentAssignmentModel = EmployeeDepartmentAssignmentEntityFixture.기본,
    ): EmployeeDepartmentAssignmentModel {
        val fixture = EmployeeDepartmentAssignment(
            0,
            employee.employeeId,
            department.departmentId,
            departmentAssignment.startDate,
            departmentAssignment.endDate,
            departmentAssignment.createdAt,
            departmentAssignment.updatedAt,
        )

        return save(fixture)
    }

    fun cleanUp() {
        db.clear()
    }
}
