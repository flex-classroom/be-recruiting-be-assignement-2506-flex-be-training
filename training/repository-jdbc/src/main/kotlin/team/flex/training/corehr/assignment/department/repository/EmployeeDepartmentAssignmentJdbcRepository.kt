package team.flex.training.corehr.assignment.department.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignment
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentModel
import team.flex.training.corehr.assignment.department.EmployeeDepartmentAssignmentRepository
import team.flex.training.corehr.assignment.department.dto.DepartmentAssignmentDto
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

    @Query(
        """
            select eda.id, eda.start_date, eda.end_date, eda.department_id, d.name as department_name
            from employee_department_assignment eda
            join department d on eda.department_id = d.id
            where eda.employee_id = :employeeId 
                and (:targetDate between eda.start_date and eda.end_date)
        """,
    )
    fun findByEmployeeIdAndDateBetween(employeeId: Long, targetDate: LocalDate): DepartmentAssignmentDto?


    @Query(
        """
            select eda.id, eda.start_date, eda.end_date, eda.department_id, d.name as department_name
            from employee_department_assignment eda
            join department d on eda.department_id = d.id
            where eda.employee_id = :employeeId
            ;
        """,
    )
    fun findByEmployeeId(employeeId: Long): List<DepartmentAssignmentDto>
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

    override fun findByEmployeeIdAndDateBetween(
        employeeIdentity: EmployeeIdentity,
        targetDate: LocalDate,
    ): DepartmentAssignmentDto? =
        employeeDepartmentAssignmentJdbcRepository.findByEmployeeIdAndDateBetween(
            employeeIdentity.employeeId,
            targetDate,
        )

    override fun findByEmployeeId(employeeIdentity: EmployeeIdentity): List<DepartmentAssignmentDto> =
        employeeDepartmentAssignmentJdbcRepository.findByEmployeeId(employeeIdentity.employeeId)

    private fun EmployeeDepartmentAssignmentModel.toEntity(): EmployeeDepartmentAssignmentEntity =
        EmployeeDepartmentAssignmentEntity(
            employeeId = employeeId,
            departmentId = departmentId,
            startDate = startDate,
            endDate = endDate,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )

    private fun EmployeeDepartmentAssignmentEntity.toModel(): EmployeeDepartmentAssignmentModel =
        EmployeeDepartmentAssignment(
            employeeDepartmentAssignmentId = employeeDepartmentAssignmentId,
            employeeId = employeeId,
            departmentId = departmentId,
            startDate = startDate,
            endDate = endDate,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
