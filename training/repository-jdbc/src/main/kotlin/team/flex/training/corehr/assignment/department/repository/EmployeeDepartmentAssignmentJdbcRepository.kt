package team.flex.training.corehr.assignment.department.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentModel
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentRepository
import team.flex.training.corehr.employee.EmployeeIdentity
import java.time.LocalDate

interface EmployeeDepartmentAssignmentJdbcRepository : CrudRepository<EmployeeDepartmentAssignmentEntity, Long> {
    @Query(
        """
            select exists(
                select 1
                from employee_department_assignment
                where employee_id = :employeeId
                    and start_date <= :endDate
                    and end_date >= :startDate
            )
        """,
    )
    fun existsByEmployeeIdAndPeriod(
        employeeId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean
}

class EmployeeDepartmentAssignmentRepositoryImpl(
    private val employeeDepartmentAssignmentJdbcRepository: EmployeeDepartmentAssignmentJdbcRepository,
) : EmployeeDepartmentAssignmentRepository {
    override fun existsByEmployeeIdAndPeriod(
        employeeIdentity: EmployeeIdentity,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean =
        employeeDepartmentAssignmentJdbcRepository.existsByEmployeeIdAndPeriod(
            employeeIdentity.employeeId,
            startDate,
            endDate,
        )

    override fun save(departmentAssignment: EmployeeDepartmentAssignmentModel): EmployeeDepartmentAssignmentModel =
        employeeDepartmentAssignmentJdbcRepository.save(departmentAssignment.toEntity())

    private fun EmployeeDepartmentAssignmentModel.toEntity(): EmployeeDepartmentAssignmentEntity =
        EmployeeDepartmentAssignmentEntity(
            employeeId = employeeId,
            departmentId = departmentId,
            startDate = startDate,
            endDate = endDate,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
