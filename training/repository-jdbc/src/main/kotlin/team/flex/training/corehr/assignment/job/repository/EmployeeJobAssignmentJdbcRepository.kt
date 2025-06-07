package team.flex.training.corehr.assignment.job.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentModel
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentRepository
import team.flex.training.corehr.employee.EmployeeIdentity
import java.time.LocalDate

interface EmployeeJobAssignmentJdbcRepository : CrudRepository<EmployeeJobAssignmentEntity, Long> {
    @Query(
        """
            select exists(
                select 1
                from employee_job_assignment
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

class EmployeeJobAssignmentRepositoryImpl(
    private val employeeJobAssignmentJdbcRepository: EmployeeJobAssignmentJdbcRepository,
) : EmployeeJobAssignmentRepository {
    override fun existsByEmployeeIdAndPeriod(
        employeeIdentity: EmployeeIdentity,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Boolean =
        employeeJobAssignmentJdbcRepository.existsByEmployeeIdAndPeriod(
            employeeIdentity.employeeId,
            startDate,
            endDate,
        )

    override fun save(jobAssignment: EmployeeJobAssignmentModel): EmployeeJobAssignmentModel =
        employeeJobAssignmentJdbcRepository.save(jobAssignment.toEntity())

    private fun EmployeeJobAssignmentModel.toEntity(): EmployeeJobAssignmentEntity =
        EmployeeJobAssignmentEntity(
            employeeId = employeeId,
            jobRoleId = jobRoleId,
            startDate = startDate,
            endDate = endDate,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
