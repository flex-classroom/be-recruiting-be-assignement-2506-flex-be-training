package team.flex.training.corehr.assignment.job.repository

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentModel
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentRepository
import team.flex.training.corehr.assignment.job.dto.JobRoleAssignmentDto
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

    @Query(
        """
            select eja.id, eja.start_date, eja.end_date, eja.job_role_id, jr.name as job_name
            from employee_job_assignment eja
            join job_role jr on eja.job_role_id = jr.id
            where eja.employee_id = :employeeId
                and (:targetDate BETWEEN eja.start_date AND eja.end_date)""",
    )
    fun findByEmployeeIdAndDateBetween(employeeId: Long, targetDate: LocalDate): JobRoleAssignmentDto?

    @Query(
        """
            select eja.id, eja.start_date, eja.end_date, eja.job_role_id, jr.name as job_name
            from employee_job_assignment eja
            join job_role jr on eja.job_role_id = jr.id
            where eja.employee_id = :employeeId
            ;
        """,
    )
    fun findByEmployeeId(employeeId: Long): List<JobRoleAssignmentDto>
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

    override fun findByEmployeeIdAndDateBetween(
        employeeIdentity: EmployeeIdentity,
        targetDate: LocalDate,
    ): JobRoleAssignmentDto? =
        employeeJobAssignmentJdbcRepository.findByEmployeeIdAndDateBetween(
            employeeIdentity.employeeId,
            targetDate,
        )

    override fun findByEmployeeId(employeeIdentity: EmployeeIdentity): List<JobRoleAssignmentDto> =
        employeeJobAssignmentJdbcRepository.findByEmployeeId(employeeIdentity.employeeId)

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
