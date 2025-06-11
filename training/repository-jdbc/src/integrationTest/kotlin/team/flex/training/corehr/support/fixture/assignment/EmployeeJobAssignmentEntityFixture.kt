package team.flex.training.corehr.support.fixture.assignment

import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentModel
import team.flex.training.corehr.assignment.job.repository.EmployeeJobAssignmentEntity
import java.time.Instant
import java.time.LocalDate

sealed class EmployeeJobAssignmentEntityFixture(
    override val employeeJobAssignmentId: Long = 0,
    override val employeeId: Long = 0,
    override val jobRoleId: Long = 1,
    override val startDate: LocalDate = LocalDate.parse("2025-01-01"),
    override val endDate: LocalDate = LocalDate.parse("2025-01-31"),
    override val createdAt: Instant = Instant.now(),
    override val updatedAt: Instant = Instant.now(),
) : EmployeeJobAssignmentModel {
    data object 기본 : EmployeeJobAssignmentEntityFixture()

    fun toEntity(jobRoleId: Long = this.jobRoleId): EmployeeJobAssignmentEntity =
        EmployeeJobAssignmentEntity(
            employeeId,
            jobRoleId,
            startDate,
            endDate,
            createdAt,
            updatedAt,
        )
}
