package team.flex.training.corehr.assignment.job.repository

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import team.flex.training.corehr.assignment.job.EmployeeJobAssignmentModel
import java.time.Instant
import java.time.LocalDate

@Table("employee_job_assignment")
class EmployeeJobAssignmentEntity(

    @Column
    override val employeeId: Long,

    @Column
    override val jobRoleId: Long,

    @Column
    override val startDate: LocalDate,

    @Column
    override val endDate: LocalDate,

    @Column
    override val createdAt: Instant,

    @Column
    override val updatedAt: Instant,

) : EmployeeJobAssignmentModel {

    @Id
    var id: Long = 0L

    override val employeeJobAssignmentId: Long
        get() = id
}
